package com.google.maps.android.compose
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class ChooseMapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(color = MaterialTheme.colors.background) {
                    ChooseMapScreen()
            }
        }
    }
}

@Composable
fun ChooseMapScreen() {

    val context = LocalContext.current
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val intent = Intent(context, MapClusteringActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Textil Geri dönüşüm konumları")
        }

        Button(onClick = {
            val intent = Intent(context, MapClusteringActivity2::class.java)
            context.startActivity(intent)
        }) {
            Text("Kağıt, Cam Geri dönüşüm konumları")
        }
    }
}