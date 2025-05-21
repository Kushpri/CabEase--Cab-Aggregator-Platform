package com.example.cabaggregatorapp.ui

import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.cabaggregatorapp.com.example.cabaggregatorapp.components.CustomButton
import com.example.cabaggregatorapp.com.example.cabaggregatorapp.components.CustomInputField
import com.example.cabaggregatorapp.com.example.cabaggregatorapp.components.FareEstimateCard
import com.example.cabaggregatorapp.com.example.cabaggregatorapp.models.RideEstimate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import kotlin.math.*

@Composable
fun BookingScreen(navController: NavController) {
    val context = LocalContext.current
    var pickupLocation by remember { mutableStateOf("") }
    var destinationLocation by remember { mutableStateOf("") }
    var showFares by remember { mutableStateOf(false) }
    var selectedRide by remember { mutableStateOf<RideEstimate?>(null) }
    var fareList by remember { mutableStateOf<List<RideEstimate>>(emptyList()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                )
            )
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Book Your Ride",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(vertical = 16.dp)
                )
            CustomInputField(
                value = pickupLocation,
                onValueChange = { pickupLocation = it },
                label = "Pickup Location"
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomInputField(
                value = destinationLocation,
                onValueChange = { destinationLocation = it },
                label = "Destination Location"
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showFares && fareList.isNotEmpty()) {
                FareComparison(fareList, selectedRide) { selectedRide = it }
            } else if (!showFares && pickupLocation.isNotBlank() && destinationLocation.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    RideMapView(pickupLocation, destinationLocation)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (pickupLocation.isNotBlank() && destinationLocation.isNotBlank()) {
                        try {
                            val geocoder = Geocoder(context)
                            val pickupCoords = geocoder.getFromLocationName(pickupLocation, 1)?.firstOrNull()
                            val dropCoords = geocoder.getFromLocationName(destinationLocation, 1)?.firstOrNull()

                            if (pickupCoords != null && dropCoords != null) {
                                val distance = calculateDistance(
                                    pickupCoords.latitude, pickupCoords.longitude,
                                    dropCoords.latitude, dropCoords.longitude
                                )

                                fareList = listOf(
                                    // Uber
                                    RideEstimate("Uber", "Bike", (20 + distance * 8).roundToInt().toDouble(), "${(6 + distance / 2).roundToInt()} mins"),
                                    RideEstimate("Uber", "Car", (40 + distance * 12).roundToInt().toDouble(), "${(10 + distance / 2).roundToInt()} mins"),
                                    RideEstimate("Uber", "Auto", (30 + distance * 10).roundToInt().toDouble(), "${(8 + distance / 2).roundToInt()} mins"),

                                    // Ola
                                    RideEstimate("Ola", "Bike", (18 + distance * 7.5).roundToInt().toDouble(), "${(7 + distance / 2).roundToInt()} mins"),
                                    RideEstimate("Ola", "Car", (38 + distance * 11).roundToInt().toDouble(), "${(11 + distance / 2).roundToInt()} mins"),
                                    RideEstimate("Ola", "Auto", (28 + distance * 9).roundToInt().toDouble(), "${(9 + distance / 2).roundToInt()} mins"),

                                    //lyft
                                    RideEstimate("Lyft", "Bike", (22 + distance * 8.5).roundToInt().toDouble(), "${(7 + distance / 2).roundToInt()} mins"),
                                    RideEstimate("Lyft", "Car", (42 + distance * 12.5).roundToInt().toDouble(), "${(12 + distance / 2).roundToInt()} mins"),
                                    RideEstimate("Lyft", "Auto", (32 + distance * 10.5).roundToInt().toDouble(), "${(10 + distance / 2).roundToInt()} mins"),
                                )


                                showFares = true
                            } else {
                                Toast.makeText(context, "Invalid location(s). Try again.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Error fetching location data.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))

            ) {
                Text(text = "Compare Fares", fontSize = 18.sp, color = Color.White)
            }

            if (selectedRide != null) {
                Text(
                    text = "Selected: ${selectedRide!!.service} - ₹${selectedRide!!.fare}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            "confirmRide/${selectedRide!!.service}/${selectedRide!!.fare}/${pickupLocation}/${destinationLocation}"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text(text = "Book Ride", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FareComparison(fareList: List<RideEstimate>, selectedRide: RideEstimate?, onSelect: (RideEstimate) -> Unit) {
    Column {
        fareList.forEach { fare ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onSelect(fare) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (fare == selectedRide) Color(0xFF2E7D32) else Color(0xFF1C1C1C)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${fare.service} (${fare.vehicleType})",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "₹${fare.fare} | ${fare.time}",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
fun RideMapView(pickup: String, drop: String) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Manage MapView lifecycle properly
    DisposableEffect(Unit) {
        mapView.onCreate(Bundle())
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDestroy()
        }
    }

    LaunchedEffect(pickup, drop) {
        mapView.getMapAsync { googleMap ->
            val geocoder = Geocoder(context)

            try {
                val pickupAddress = geocoder.getFromLocationName(pickup, 1)
                val dropAddress = geocoder.getFromLocationName(drop, 1)

                if (!pickupAddress.isNullOrEmpty() && !dropAddress.isNullOrEmpty()) {
                    val pickupLatLng = LatLng(pickupAddress[0].latitude, pickupAddress[0].longitude)
                    val dropLatLng = LatLng(dropAddress[0].latitude, dropAddress[0].longitude)

                    googleMap.clear() // clear old markers/polylines

                    googleMap.addMarker(MarkerOptions().position(pickupLatLng).title("Pickup"))
                    googleMap.addMarker(MarkerOptions().position(dropLatLng).title("Drop"))

                    googleMap.addPolyline(
                        PolylineOptions()
                            .add(pickupLatLng, dropLatLng)
                            .color(android.graphics.Color.BLUE)
                            .width(8f)
                    )

                    val bounds = LatLngBounds.Builder()
                        .include(pickupLatLng)
                        .include(dropLatLng)
                        .build()

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                } else {
                    Toast.makeText(context, "Could not find one or both locations.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error while fetching location. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}


// 🔁 Distance Calculation (Haversine)
fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371 // in kilometers

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}
