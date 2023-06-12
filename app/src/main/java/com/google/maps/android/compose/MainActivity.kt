package com.google.maps.android.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.maps.android.compose.theme.SignUp
import com.google.maps.android.compose.theme.UserDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = UserDatabase.getDatabase(this)
        setContent {

                MaterialTheme {
                    var isSignUp by remember { mutableStateOf(false) }
                    if (isSignUp) {
                        SignUp(database, onSignUpCompleted = { isSignUp = false })
                    } else {
                        Login(database, onSignUpNeeded = { isSignUp = true })





                    }
                }

        }
    }
}

