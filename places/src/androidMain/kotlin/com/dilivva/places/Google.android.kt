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

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PlusCode
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.UUID
import com.google.android.libraries.places.api.model.Place.Field as GoogleField


internal lateinit var applicationContext: Context
    private set
internal actual fun initializeWithKey(key: String){
    Places.initialize(applicationContext, key)
}
@Composable
actual fun rememberGooglePlaces(config: PlacesConfig, onResult: (PlaceResult) -> Unit): GooglePlaces{
    val componentActivity = LocalContext.current as ComponentActivity
    applicationContext = componentActivity.applicationContext
    return remember(config,onResult,componentActivity) { AndroidGooglePlaces(config, onResult, componentActivity) }
}

internal fun <I, O> ComponentActivity.registerActivityResultLauncher(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
): ActivityResultLauncher<I> {
    val key = UUID.randomUUID().toString()
    return activityResultRegistry.register(key, contract, callback)
}

class AndroidGooglePlaces(
    private val config: PlacesConfig,
    private val onResult: (PlaceResult) -> Unit,
    componentActivity: ComponentActivity
): GooglePlaces{

    private val startAutocomplete =
        componentActivity.registerActivityResultLauncher(ActivityResultContracts.StartActivityForResult()) { result -> processResult(result) }

    private val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, mapFields())
        .setCountries(config.countries)
        .build(componentActivity)
    override fun launch() {
        startAutocomplete.launch(intent)
    }

    private fun processResult(result: ActivityResult){
        startAutocomplete.unregister()
        when(result.resultCode){
            Activity.RESULT_OK ->{
                val intent = result.data
                if (intent != null) {
                    val placeIntent = Autocomplete.getPlaceFromIntent(intent)
                    val place = Place(
                        name = placeIntent.name,
                        placeId = placeIntent.id,
                        coordinates = toCoordinates(placeIntent.latLng),
                        plusCode = toPlusCode(placeIntent.plusCode),
                        formattedAddress = placeIntent.address
                    )
                    onResult(PlaceResult.Success(place))
                }
            }
            Activity.RESULT_CANCELED ->{
                onResult(PlaceResult.Cancelled)
            }
            else -> { onResult(PlaceResult.Failure("An error occurred")) }
        }

    }

    private fun mapFields(): List<GoogleField>{
        return config.fields.map {
            when(it){
                PlacesFields.NAME -> GoogleField.NAME
                PlacesFields.PLACE_ID -> GoogleField.ID
                PlacesFields.COORDINATES -> GoogleField.LAT_LNG
                PlacesFields.PLUS_CODE -> GoogleField.PLUS_CODE
                PlacesFields.ADDRESS -> GoogleField.ADDRESS
            }
        }
    }

    private fun toCoordinates(latLng: LatLng?): Place.Coordinates?{
        if (latLng == null) return null
        return Place.Coordinates(latLng.latitude, latLng.longitude)
    }
    private fun toPlusCode(plusCode: PlusCode?): Place.PlusCode?{
        if (plusCode == null) return null
        return Place.PlusCode(plusCode.compoundCode, plusCode.globalCode.orEmpty())
    }


}