package com.google.maps.android.compose

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

val hasValidApiKey: Boolean =
    BuildConfig.MAPS_API_KEY.isNotBlank() && BuildConfig.MAPS_API_KEY != "AIzaSyCwJsCQAgAMuzUhNTLrZV8-KMDBAdywTLA"

const val assertRoundingError: Double = 0.01

fun LatLng.assertEquals(other: LatLng) {
    assertEquals(latitude, other.latitude, assertRoundingError)
    assertEquals(longitude, other.longitude, assertRoundingError)
}

fun LatLng.assertNotEquals(other: LatLng) {
    assertNotEquals(latitude, other.latitude, assertRoundingError)
    assertNotEquals(longitude, other.longitude, assertRoundingError)
}