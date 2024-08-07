package com.dilivva.googleex

import App
import Places
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dilivva.signin.GoogleSignInConfig

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GoogleSignInConfig.configure("key")
        setContent {
            Places()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}