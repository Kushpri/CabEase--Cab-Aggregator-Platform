package com.example.cabaggregatorapp.com.example.cabaggregatorapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import android.content.Context
import com.example.cabaggregatorapp.models.Ride
import com.example.cabaggregatorapp.ui.FeedbackScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.cabaggregatorapp.R


@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToBooking: () -> Unit,
    context: Context,
    ride: Ride? = null
)

 {

    // Step 1: Feedback show karne wala variable
    var showFeedback by remember { mutableStateOf(false) }

    // Step 2: Lifecycle observer - jaise hi app resume ho
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                ride?.let {
                    if (!it.feedbackGiven) {
                        showFeedback = true
                    }
                }

            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Step 3: Agar feedback dikhana hai toh Feedback Screen
    if (showFeedback) {
        ride?.let {
            FeedbackScreen(
                context = context,
                ride = it,
                onSubmit = {
                    showFeedback = false
                }
            )
        }

    }

    // Step 4: Warna Normal HomeScreen ka UI
    else {
        // UI Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF3F51B5), Color(0xFF673AB7))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo), // Make sure app_logo is in res/drawable
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = "Welcome to CabEase!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = { onNavigateToBooking() },
                    modifier = Modifier
                        .width(280.dp)
                        .height(60.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Book a Ride", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate("ride_history") },
                    modifier = Modifier
                        .width(280.dp)
                        .height(60.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("View Ride History", fontSize = 18.sp)
                }
            }
        }
    }
 }