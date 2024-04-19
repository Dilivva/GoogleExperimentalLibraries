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

enum class PlacesFields{
    NAME, PLACE_ID, COORDINATES, PLUS_CODE, ADDRESS
}

sealed interface PlaceResult{
    data class Success(val place: Place): PlaceResult
    data class Failure(val error: String): PlaceResult
    data object Cancelled: PlaceResult
}

data class Place(
    val name: String? = null,
    val placeId: String? = null,
    val coordinates: Coordinates? = null,
    val plusCode: PlusCode? = null,
    val formattedAddress: String? = null
){
    data class Coordinates(
        val latitude: Double,
        val longitude: Double
    )
    data class PlusCode(
        val compoundCode: String?,
        val globalCode: String
    )
}
class PlacesConfig(
    val fields: List<PlacesFields>,
    val countries: List<String>
)

interface GooglePlaces{
    fun launch()
}