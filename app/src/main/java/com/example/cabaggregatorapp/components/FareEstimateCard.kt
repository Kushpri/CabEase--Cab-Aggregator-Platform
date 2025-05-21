package com.example.cabaggregatorapp.com.example.cabaggregatorapp.components



import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FareEstimateCard(service: String, fare: Double, time: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Service: $service", fontWeight = FontWeight.Bold)
            Text(text = "Fare: ₹$fare")
            Text(text = "ETA: $time")
        }
    }
}