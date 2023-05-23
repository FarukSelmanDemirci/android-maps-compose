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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import kotlin.random.Random
import android.content.Context
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

data class locasyon(val Lokasyon: String)

class MapClusteringActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleMapClustering()
        }
    }
}

@Composable
fun GoogleMapClustering() {

    val context= LocalContext.current
    val jsonFileString = getJsonDataFromAsset(context, "list.json")
    Log.i("data", jsonFileString!!)
    val gson = Gson()
    val listlocasyonType = object : TypeToken<List<locasyon>>() {}.type

    var lokasyon: List<locasyon> = gson.fromJson(jsonFileString, listlocasyonType)
    val items = remember { mutableStateListOf<MyItem>() }
    lokasyon.forEachIndexed {
            idx, lokasyon -> Log.i("data", "> Item $idx:\n$lokasyon")
        LaunchedEffect(Unit) {
            val lat =   lokasyon.Lokasyon?.split(",")?.first()?.toDouble()
            val log =  lokasyon.Lokasyon?.split(",")?.last()?.toDouble()
            val position = lat?.let {
                log?.let { it1 ->
                    LatLng(
                        it, it1

                    )
                }
            }
            position?.let { MyItem(it, "Marker", "Snippet") }?.let { items.add(it) }

        }
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
    val itemTitle: String,
    val itemSnippet: String,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet
}
