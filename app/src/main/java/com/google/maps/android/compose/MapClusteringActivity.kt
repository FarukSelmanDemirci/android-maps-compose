package com.google.maps.android.compose

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.clustering.Clustering
import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.maps.android.compose.theme.LocationEntity
import com.google.maps.android.compose.theme.LocationViewModel
import com.google.maps.android.compose.theme.locasyon
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

private val TAG = MapClusteringActivity::class.simpleName

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}


@AndroidEntryPoint
class MapClusteringActivity : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()

    private var status: Boolean=false
    private var databaseFilled: Boolean = false
    private val DATABASE_FILLED = "database_filled"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        status = intent.getBooleanExtra("durum", true)

        // Get SharedPreferences
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        databaseFilled = sharedPref.getBoolean(DATABASE_FILLED, false)

        setContent {
            GoogleMapClustering(viewModel, status, databaseFilled)
        }
    }

    override fun onStop() {
        super.onStop()

        // Save to SharedPreferences when the activity is stopped
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(DATABASE_FILLED, true)
            apply()
        }
    }
}



@Composable
fun GoogleMapClustering(viewModel: LocationViewModel, status: Boolean, databaseFilled: Boolean) {
    val context = LocalContext.current

    if (!databaseFilled) {
        val jsonFileString = getJsonDataFromAsset(context, "list.json")
        Log.i("data", jsonFileString!!)
        val gson = Gson()
        val listlocasyonType = object : TypeToken<List<locasyon>>() {}.type

        var lokasyon: List<locasyon> = gson.fromJson(jsonFileString, listlocasyonType)
        lokasyon.forEachIndexed { idx, loc ->
            Log.i("data", "> Item $idx:\n$loc")
            LaunchedEffect(Unit) {
                val lat = loc.Lokasyon?.split(",")?.first()?.toDouble()
                val lon = loc.Lokasyon?.split(",")?.last()?.toDouble()
                // Check it out
                if (lat != null && lon != null) {
                    val locationName = if (loc.durum) "Kağıt ve Cam GND" else "Tekstil Konumu"
                    val locationEntity = LocationEntity(id = loc.id, locationName, lat, lon, loc.durum)
                    viewModel.insert(locationEntity)  // We're adding the location to the database.
                }
            }
        }
    }



    val items = remember { mutableStateListOf<MyItem>() }

    val locationObserver = Observer<List<LocationEntity>> { locations ->
        locations.forEach { locationEntity ->
            if (locationEntity.status == status) { // Add this condition
                val position = LatLng(locationEntity.lat, locationEntity.lon)
                val item = MyItem(position, locationEntity.id, "GND.", " ")
                if (item !in items) items.add(item)
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.getLocationsByDurum(status).observe(context as LifecycleOwner, locationObserver)
    }

    GoogleMapClustering(items = items)
}






@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapClustering(items: List<MyItem>) {
    val lokasyon = LatLng (38.66208, 39.23248)
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {

            position = CameraPosition.fromLatLngZoom(lokasyon, 10f)
        }
    ) {
        Clustering(
            items = items,
            // Optional: Handle clicks on clusters, cluster items, and cluster item info windows
            onClusterClick = {
                Log.d(TAG, "Cluster clicked! $it")
                false
            },
            onClusterItemClick = {
                Log.d(TAG, "Cluster item clicked! $it")
                false
            },
            onClusterItemInfoWindowClick = {
                Log.d(TAG, "Cluster item info window clicked! $it")
            },
            // Optional: Custom rendering for clusters
            clusterContent = { cluster ->
                Surface(
                    Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.Blue,
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "%,d".format(cluster.size),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            // Optional: Custom rendering for non-clustered items
            clusterItemContent = null
        )
        MarkerInfoWindow(
            state = rememberMarkerState(position = lokasyon),
            onClick = {
                Log.d(TAG, "Non-cluster marker clicked! $it")
                true
            }
        )
    }
}

data class MyItem(
    val itemPosition: LatLng,
    val itemId: Int,   // Konum ID'sini saklayan yeni alan.
    val itemTitle: String,
    val itemSnippet: String,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    // Bilgi ekranında hem konum adı hem de konum ID'sini gösteriyoruz.
    override fun getSnippet(): String =
        "$itemSnippet. ID: $itemId"
}
