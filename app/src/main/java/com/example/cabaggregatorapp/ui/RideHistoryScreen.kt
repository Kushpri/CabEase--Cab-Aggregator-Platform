package com.example.cabaggregatorapp.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cabaggregatorapp.models.Ride
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.cabaggregatorapp.utils.RideHistoryManager


@Composable
fun RideHistoryScreen(context: Context) {
    var rideHistory by remember { mutableStateOf(listOf<Ride>()) }

    // Load ride history on first composition
    LaunchedEffect(Unit) {
        val rideHistoryManager = RideHistoryManager(context)
        rideHistory = rideHistoryManager.getRides()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "Ride History",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (rideHistory.isEmpty()) {
            Text("No rides found.")
        } else {
            LazyColumn {
                items(rideHistory) { ride ->
                    RideCard(ride)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun RideCard(ride: Ride) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Service: ${ride.service}", style = MaterialTheme.typography.titleMedium)
            Text("Fare: ₹${ride.fare}")
            Text("From: ${ride.pickup}")
            Text("To: ${ride.drop}")
            val formattedTime = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date(ride.timestamp))
            Text("Time: $formattedTime")

        }
    }
}
