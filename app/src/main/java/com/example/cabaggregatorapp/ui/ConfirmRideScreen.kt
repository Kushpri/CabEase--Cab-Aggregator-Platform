package com.example.cabaggregatorapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.cabaggregatorapp.R
import com.example.cabaggregatorapp.models.Ride
import com.example.cabaggregatorapp.utils.RideHistoryManager
import com.example.cabaggregatorapp.utils.calculateDistanceInKm
import com.example.cabaggregatorapp.utils.openRideApp
import com.google.gson.Gson

@Composable
fun ConfirmRideScreen(
    navController: NavController,
    service: String,
    fare: Double,
    pickup: String,
    destination: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var rideConfirmed by remember { mutableStateOf(false) }
    var distanceInKm by remember { mutableStateOf<Double?>(null) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        distanceInKm = calculateDistanceInKm(context, pickup, destination)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CabEase",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Confirm your ride with $service",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                RideInfoRow(icon = Icons.Default.LocationOn, label = "Pickup", value = pickup)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                RideInfoRow(icon = Icons.Default.Flag, label = "Destination", value = destination)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                RideInfoRow(icon = Icons.Default.AttachMoney, label = "Fare", value = "₹$fare")
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                distanceInKm?.let {
                    RideInfoRow(icon = Icons.Default.Flag, label = "Distance", value = "${"%.2f".format(it)} km")
                } ?: CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Confirm Booking")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                sendEmergencySMS(context, pickup, destination, service)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Panic Button (SOS)")
        }

        // ✅ Animated Ride confirmation message
        AnimatedVisibility(visible = rideConfirmed) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "✅ Ride booked successfully with $service!",
                    color = Color.Green,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // 🚘 Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Ride") },
                text = { Text("Do you want to confirm this ride with $service?") },
                confirmButton = {
                    TextButton(onClick = {
                        rideConfirmed = true
                        showDialog = false

                        val ride = Ride(
                            id = 0,
                            serviceType = service,
                            service = service,
                            pickup = pickup,
                            drop = destination,
                            fare = fare
                        )

                        val rideHistoryManager = RideHistoryManager(context)
                        rideHistoryManager.saveRide(ride)

                        val sharedPrefs = context.getSharedPreferences("CabEasePrefs", Context.MODE_PRIVATE)
                        val rideJson = Gson().toJson(ride)
                        sharedPrefs.edit().putString("last_ride", rideJson).apply()

                        createNotificationChannel(context)

                        val notification = NotificationCompat.Builder(context, "ride_channel")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("Ride Confirmed")
                            .setContentText("Your ride from $pickup to $destination is confirmed with $service.")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .build()

                        NotificationManagerCompat.from(context).notify(1001, notification)

                        openRideApp(context, service, pickup, destination)
                        navController.navigate("feedback")
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

// 🔔 Create Notification Channel
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "ride_channel",
            "Ride Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications about your rides"
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

// 🚨 SOS Function
fun sendEmergencySMS(context: Context, pickup: String, destination: String, service: String) {
    val emergencyNumber = "1234567890"
    val message = "🚨 Emergency! I am using $service for a ride from $pickup to $destination. Please check on me."

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("sms:$emergencyNumber")
        putExtra("sms_body", message)
    }

    try {
        ContextCompat.startActivity(context, intent, null)
        Toast.makeText(context, "SOS message ready to be sent!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to launch SMS app.", Toast.LENGTH_SHORT).show()
    }
}

// 🚗 Ride Info Row
@Composable
fun RideInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF4CAF50) // Green color for all icons
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// 📍 Distance Calculation
fun calculateDistanceInKm(context: Context, startAddress: String, endAddress: String): Double? {
    return try {
        val geocoder = Geocoder(context)
        val startLocation = geocoder.getFromLocationName(startAddress, 1)?.firstOrNull()
        val endLocation = geocoder.getFromLocationName(endAddress, 1)?.firstOrNull()

        if (startLocation != null && endLocation != null) {
            val result = FloatArray(1)
            Location.distanceBetween(
                startLocation.latitude,
                startLocation.longitude,
                endLocation.latitude,
                endLocation.longitude,
                result
            )
            result[0] / 1000.0
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
