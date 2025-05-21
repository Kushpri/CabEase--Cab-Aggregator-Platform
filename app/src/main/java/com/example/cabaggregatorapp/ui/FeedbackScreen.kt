package com.example.cabaggregatorapp.ui

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import com.example.cabaggregatorapp.models.Ride
import androidx.compose.ui.graphics.Color


@Composable
fun FeedbackScreen(context: Context, ride: Ride, onSubmit: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var feedbackText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rate your ride with ${ride.service}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show Pickup
        Text(
            text = "From: ${ride.pickup}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Show Drop
        Text(
            text = "To: ${ride.drop}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Rating Stars
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 1..5) {
                IconButton(onClick = { rating = i }) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star $i",
                        tint = if (i <= rating) Color.Yellow else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Write your feedback") },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            println("Rating: $rating, Feedback: ${feedbackText.text}, Ride Info: ${ride.service}, ${ride.pickup} to ${ride.drop}")
            onSubmit()
        }) {
            Text("Submit Feedback")
        }
    }
}
