package com.example.cabaggregatorapp.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import android.content.Context
import android.content.Intent
import android.net.Uri


import android.location.Geocoder
import android.location.Location
import java.util.Locale

fun calculateDistanceInKm(
    context: Context,
    pickupAddress: String,
    destinationAddress: String
): Double {
    val geocoder = Geocoder(context, Locale.getDefault())

    try {
        val pickupLocation = geocoder.getFromLocationName(pickupAddress, 1)?.firstOrNull()
        val destinationLocation = geocoder.getFromLocationName(destinationAddress, 1)?.firstOrNull()

        if (pickupLocation != null && destinationLocation != null) {
            val results = FloatArray(1)
            Location.distanceBetween(
                pickupLocation.latitude,
                pickupLocation.longitude,
                destinationLocation.latitude,
                destinationLocation.longitude,
                results
            )
            return results[0] / 1000.0 // meters to km
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return 0.0
}


fun openRideApp(context: Context, pickup: String, dropoff: String) {
    val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=${Uri.encode(pickup)}&destination=${Uri.encode(dropoff)}")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps") // Tries to open in Google Maps app
    }

    // Fallback if Google Maps app is not installed
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        val fallbackIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(fallbackIntent)
    }
}

fun openRideApp(context: Context, service: String, pickup: String, dropoff: String) {
    val encodedPickup = URLEncoder.encode(pickup, StandardCharsets.UTF_8.toString())
    val encodedDrop = URLEncoder.encode(dropoff, StandardCharsets.UTF_8.toString())

    val uri = when (service) {
        "Uber" -> "https://m.uber.com/ul/?action=setPickup&pickup=$encodedPickup&dropoff=$encodedDrop"
        "Ola" -> "https://book.olacabs.com/?pickup=$encodedPickup&dropoff=$encodedDrop"
        "Lyft" -> "https://ride.lyft.com/?id=lyft&pickup=$encodedPickup&dropoff=$encodedDrop"
        else -> ""
    }

    if (uri.isNotBlank()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }
}