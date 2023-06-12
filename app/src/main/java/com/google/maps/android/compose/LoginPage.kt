package com.google.maps.android.compose

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.theme.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Login(
    database: UserDatabase,
    onSignUpNeeded: () -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Kullanıcı Adı") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val user = database.userDao().validateUser(username, password)
                if (user != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Giriş başarılı!", Toast.LENGTH_SHORT).show()

                        val activity = if (user.type == 1) {
                            AdminActivity::class.java
                        } else {
                            ChooseMapActivity::class.java
                        }

                        val intent = Intent(context, activity)
                        context.startActivity(intent)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Kullanıcı adı veya şifre yanlış",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }) {
            Text("Giriş Yap")
        }
        TextButton(onClick = { onSignUpNeeded() }) {
            Text("Hesabınız yok mu?Kayıt olun")
        }

    }
}

