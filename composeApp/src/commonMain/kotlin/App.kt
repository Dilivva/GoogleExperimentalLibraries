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
import com.dilivva.signin.GoogleSignInResult
import com.dilivva.signin.GoogleSignInUser
import com.dilivva.signin.rememberGoogleSignIn
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalResourceApi::class, ExperimentalEncodingApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        var error  by remember { mutableStateOf<String?>(null) }
        var googleSignInUser by remember { mutableStateOf<GoogleSignInUser?>(null) }
        val signIn = rememberGoogleSignIn {
            when(it){
                is GoogleSignInResult.Error -> println("GoogleSignInError: ${it.message}")
                is GoogleSignInUser -> { println("User: $it"); googleSignInUser = it }
                GoogleSignInResult.NoResult -> println("No result")
            }
        }


        LaunchedEffect(Unit){
            if (!showContent) showContent = true
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TopAppBar(modifier = Modifier.fillMaxWidth(), title = { Text("Google Experiments") })
            googleSignInUser?.let {
                UserItem(it)
            }

            Button(
                onClick = { signIn.signIn() },
                enabled = true
            ) {
                Text("Sign In")
            }
            Button(
                onClick = { signIn.restorePreviousSignIn() },
                enabled = true
            ) {
                Text("Restore previous signin")
            }
        }

        if (error != null){
            ShowDialog(error.orEmpty()){
                error = null
            }
        }

    }
}

@Composable
fun ShowDialog(
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
fun UserItem(
    user: GoogleSignInUser
){
    Box(
        modifier = Modifier.fillMaxWidth().padding(20.dp)
            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(15.dp))
            .padding(8.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)){
            Text(
                text = "Name: ${user.name}",
                fontSize = 14.sp,
                color = Color.Red
            )
            Text(
                text = "Email: ${user.email}",
                fontSize = 14.sp,
                color = Color.Blue
            )

            Text(
                text = "TokenID: ${user.idToken.take(10)}",
                fontSize = 14.sp,
                color = Color.Blue
            )
            Text(
                text = "Url: ${user.profilePictureUri}",
                fontSize = 14.sp,
                color = Color.Blue
            )

        }
    }
}

//@Composable
//fun PlaceItem(
//    place: Place
//){
//    Box(
//        modifier = Modifier.fillMaxWidth().padding(20.dp)
//            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(15.dp))
//            .padding(8.dp)
//    ){
//        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)){
//            Text(
//                text = "Name: ${place.name}",
//                fontSize = 14.sp,
//                color = Color.Red
//            )
//            Text(
//                text = "Address: ${place.formattedAddress}",
//                fontSize = 14.sp,
//                color = Color.Blue
//            )
//
//            Text(
//                text = "PlaceID: ${place.placeId}",
//                fontSize = 14.sp,
//                color = Color.Blue
//            )
//            Text(
//                text = "Coordinates: ${place.coordinates.toString()}",
//                fontSize = 14.sp,
//                color = Color.Blue
//            )
//
//        }
//    }
//}

