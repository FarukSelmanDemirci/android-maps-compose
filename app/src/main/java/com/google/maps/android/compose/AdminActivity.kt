package com.google.maps.android.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.theme.LocationEntity
import com.google.maps.android.compose.theme.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminActivity : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Ekleme formu
                    var lat by remember { mutableStateOf("") }
                    var lon by remember { mutableStateOf("") }
                    var selectedLocationType by remember { mutableStateOf("Cam ve Kağıt") }
                    var status by remember { mutableStateOf(true) }
                    val locationTypes = listOf("Cam ve Kağıt", "Tekstil")
                    val expanded = remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = lat,
                        onValueChange = { lat = it },
                        label = { Text("Enlem") }
                    )

                    OutlinedTextField(
                        value = lon,
                        onValueChange = { lon = it },
                        label = { Text("Boylam") }
                    )

                    Column {
                        OutlinedTextField(
                            value = selectedLocationType,
                            onValueChange = { selectedLocationType = it },
                            label = { Text("Durum") },
                            trailingIcon = {
                                IconButton(onClick = { expanded.value = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                                }
                            },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            locationTypes.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    selectedLocationType = label
                                    status = label == "Cam ve Kağıt"
                                    expanded.value = false
                                }) {
                                    Text(text = label)
                                }
                            }
                        }
                    }

                    Button(onClick = {
                        val latValue = lat.toDoubleOrNull()
                        val lonValue = lon.toDoubleOrNull()
                        if (latValue != null && lonValue != null) {
                            val locationEntity = LocationEntity(0, "Geri Dönüşüm Noktası", latValue, lonValue, status)
                            viewModel.insert(locationEntity)
                        }
                    }) {
                        Text(text = "Konum Ekle")
                    }

                    // Silme formu
                    var id by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("ID") }
                    )

                    Button(onClick = {
                        val idValue = id.toIntOrNull()
                        if (idValue != null) {
                            viewModel.delete(idValue)
                        }
                    }) {
                        Text(text = "Konum Sil")
                    }
                    TextButton(onClick = { ChooseMapActivity::class.java }) {
                        Text("Konumları görün")
                    }
                }
            }
        }
    }
}
