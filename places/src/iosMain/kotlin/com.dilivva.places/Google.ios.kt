/*
 * Copyright (C) 2024, Send24.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.dilivva.places


import Places.GMSAutocompleteFilter
import Places.GMSAutocompleteViewController
import Places.GMSAutocompleteViewControllerDelegateProtocol
import Places.GMSPlace
import Places.GMSPlaceFieldCoordinate
import Places.GMSPlaceFieldFormattedAddress
import Places.GMSPlaceFieldName
import Places.GMSPlaceFieldPlaceID
import Places.GMSPlaceFieldPlusCode
import Places.GMSPlacesClient
import Places.GMSPlusCode
import androidx.compose.runtime.Composable
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal actual fun initializeWithKey(key: String){
    GMSPlacesClient.provideAPIKey(key)
}

@Composable
actual fun rememberGooglePlaces(config: PlacesConfig, onResult: (PlaceResult) -> Unit): GooglePlaces{
    return IosGooglePlaces(config, onResult)
}

@OptIn(ExperimentalForeignApi::class)
class IosGooglePlaces(
    private val config: PlacesConfig,
    private val onResult: (PlaceResult) -> Unit
): GooglePlaces{

    private lateinit var placesController: GMSAutocompleteViewController
    private val window = UIApplication.sharedApplication.windows.last() as? UIWindow
    private val currentViewController = window?.rootViewController
    private val delegate = PlacesDelegate{
        onResult(it)
        currentViewController?.dismissViewControllerAnimated(true, null)
    }

    override fun launch() {
        setUp()
        if (::placesController.isInitialized){
            window?.makeKeyAndVisible()
            currentViewController?.presentViewController(placesController,animated = true, completion = null)
        }
    }

    private fun setUp(){
        placesController = GMSAutocompleteViewController()
        val filter = GMSAutocompleteFilter()
        filter.countries = config.countries
        placesController.placeFields = mapFields()
        placesController.autocompleteFilter = filter
        if (placesController.delegate == null){
            println("Delegate is null")
            placesController.setDelegate(delegate)
        }
    }

    private fun mapFields(): ULong{
        return config.fields.sumOf {
            when (it) {
                PlacesFields.NAME -> GMSPlaceFieldName
                PlacesFields.PLACE_ID -> GMSPlaceFieldPlaceID
                PlacesFields.COORDINATES -> GMSPlaceFieldCoordinate
                PlacesFields.PLUS_CODE -> GMSPlaceFieldPlusCode
                PlacesFields.ADDRESS -> GMSPlaceFieldFormattedAddress
            }
        }
    }

}

@OptIn(ExperimentalForeignApi::class)
class PlacesDelegate(private val onResult: (PlaceResult) -> Unit): NSObject(), GMSAutocompleteViewControllerDelegateProtocol{
    override fun viewController(
        viewController: GMSAutocompleteViewController,
        didAutocompleteWithPlace: GMSPlace
    ) {
        val place = Place(
            name = didAutocompleteWithPlace.name,
            placeId = didAutocompleteWithPlace.placeID,
            coordinates = toCoordinates(didAutocompleteWithPlace.coordinate),
            plusCode = toPlusCode(didAutocompleteWithPlace.plusCode),
            formattedAddress = didAutocompleteWithPlace.formattedAddress
        )
        onResult(PlaceResult.Success(place))
    }

    private fun toCoordinates(coordinate: CValue<CLLocationCoordinate2D>?): Place.Coordinates?{
        if (coordinate == null) return null
        return coordinate.useContents {
            Place.Coordinates(latitude, longitude)
        }
    }
    private fun toPlusCode(plusCode: GMSPlusCode?): Place.PlusCode?{
        if (plusCode == null) return null
        return Place.PlusCode(plusCode.compoundCode, plusCode.globalCode)
    }

    override fun viewController(
        viewController: GMSAutocompleteViewController,
        didFailAutocompleteWithError: NSError
    ) {
        onResult(PlaceResult.Failure(didFailAutocompleteWithError.localizedFailureReason ?: "Ann error occurred"))
    }

    override fun wasCancelled(viewController: GMSAutocompleteViewController) {
        onResult(PlaceResult.Cancelled)
    }

}