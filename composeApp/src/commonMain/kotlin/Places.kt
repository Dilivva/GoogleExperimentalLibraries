import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilivva.places.GooglePlace
import com.dilivva.places.Place
import com.dilivva.places.PlaceResult
import com.dilivva.places.PlacesConfig
import com.dilivva.places.PlacesFields
import com.dilivva.places.rememberGooglePlaces
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalResourceApi::class, ExperimentalEncodingApi::class)
@Composable
fun Places() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        var error  by remember { mutableStateOf<String?>(null) }
        var googlePlace by remember { mutableStateOf<Place?>(null) }
        val googlePlaces = rememberGooglePlaces(
            config = PlacesConfig(fields = PlacesFields.entries.toList(), countries = listOf("ng")),
            onResult = {
                when(it){
                    is PlaceResult.Cancelled -> println("Closed")
                    is PlaceResult.Failure -> it.error
                    is PlaceResult.Success -> googlePlace = it.place
                }
            })


        LaunchedEffect(Unit){
            if (!showContent) showContent = true
            GooglePlace.initialize("AIzaSyCN1ifxGvD37PRZwmoRqKHgS9p8ccTzBE4")
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TopAppBar(modifier = Modifier.fillMaxWidth(), title = { Text("Google Experiments") })
            googlePlace?.let {
                PlaceItem(it)
            }

            Button(
                onClick = { googlePlaces.launch() },
                enabled = true
            ) {
                Text("Sign In")
            }
        }

        if (error != null){
            ShowPlacesDialog(error.orEmpty()){
                error = null
            }
        }

    }
}

@Composable
private fun ShowPlacesDialog(
    message: String,
    onDismiss: () -> Unit
){
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        title = { Text("Error") },
        text = { Text(message) },
        onDismissRequest = onDismiss,
        buttons = {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
        },
        shape = RoundedCornerShape(15.dp)
    )
}


@Composable
fun PlaceItem(
    place: Place
){
    Box(
        modifier = Modifier.fillMaxWidth().padding(20.dp)
            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(15.dp))
            .padding(8.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)){
            Text(
                text = "Name: ${place.name}",
                fontSize = 14.sp,
                color = Color.Red
            )
            Text(
                text = "Address: ${place.formattedAddress}",
                fontSize = 14.sp,
                color = Color.Blue
            )

            Text(
                text = "PlaceID: ${place.placeId}",
                fontSize = 14.sp,
                color = Color.Blue
            )
            Text(
                text = "Coordinates: ${place.coordinates.toString()}",
                fontSize = 14.sp,
                color = Color.Blue
            )

        }
    }
}

