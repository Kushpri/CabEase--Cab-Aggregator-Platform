package com.example.cabaggregatorapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import android.os.Build
import androidx.core.app.ActivityCompat
import android.app.NotificationChannel
import android.app.NotificationManager


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseApp
import com.example.cabaggregatorapp.ui.SplashScreen
import com.example.cabaggregatorapp.ui.RideHistoryScreen
import com.example.cabaggregatorapp.com.example.cabaggregatorapp.ui.HomeScreen
import com.example.cabaggregatorapp.ui.theme.AuthScreen
import com.example.cabaggregatorapp.ui.BookingScreen
import com.example.cabaggregatorapp.ui.ConfirmRideScreen
import com.example.cabaggregatorapp.ui.FeedbackScreen
import com.example.cabaggregatorapp.ui.theme.CabAggregatorAppTheme
import com.google.gson.Gson
import com.example.cabaggregatorapp.models.Ride


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }


            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen(onNavigateToHome = {
                        navController.navigate("auth") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    })
                }

                composable(route = "auth") {
                    AuthScreen(onAuthSuccess = {
                        Log.d("AuthScreen", "User Authenticated, Navigating to Home")
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    })
                }

                composable(route = "home") {
                    val context = LocalContext.current

                    // ✅ Moved redirection logic here
                    LaunchedEffect(Unit) {
                        val sharedPrefs = context.getSharedPreferences("CabEasePrefs", Context.MODE_PRIVATE)
                        val lastRideJson = sharedPrefs.getString("last_ride", null)

                        if (lastRideJson != null) {
                            val gson = Gson()
                            val ride = gson.fromJson(lastRideJson, Ride::class.java)

                            // Clear stored ride to prevent repeated redirection
                            sharedPrefs.edit().remove("last_ride").apply()

                            navController.navigate("feedback/${ride.service}/${ride.fare}/${ride.pickup}/${ride.drop}")
                        }
                    }



                    HomeScreen(
                        navController = navController,
                        onNavigateToBooking = { navController.navigate("booking") },
                        context = context,
                    )
                }

                composable("booking") {
                    BookingScreen(navController)
                }

                composable("ride_history") {
                    RideHistoryScreen(context = LocalContext.current)
                }

                composable(
                    route = "confirmRide/{service}/{fare}/{pickup}/{drop}"
                ) { backStackEntry ->

                    val service = backStackEntry.arguments?.getString("service") ?: ""
                    val fare = backStackEntry.arguments?.getString("fare")?.toDoubleOrNull() ?: 0.0
                    val pickup = backStackEntry.arguments?.getString("pickup") ?: ""
                    val destination = backStackEntry.arguments?.getString("drop") ?: ""

                    ConfirmRideScreen(
                        navController = navController,
                        service = service,
                        fare = fare,
                        pickup = pickup,
                        destination = destination
                    )
                }

                composable(
                    route = "feedback/{service}/{fare}/{pickup}/{drop}"
                ) { backStackEntry ->
                    val service = backStackEntry.arguments?.getString("service") ?: ""
                    val fare = backStackEntry.arguments?.getString("fare") ?: "0.0"
                    val pickup = backStackEntry.arguments?.getString("pickup") ?: ""
                    val drop = backStackEntry.arguments?.getString("drop") ?: ""

                    val context = LocalContext.current
                    val ride = Ride(
                        service = service,
                        pickup = pickup,
                        drop = drop,
                        fare = fare.toDoubleOrNull() ?: 0.0,
                        id = 0,
                        serviceType = "Standard"
                    )

                    FeedbackScreen(
                        context = context,
                        ride = ride,
                        onSubmit = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }

    class SplashActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.splashscreen)

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2000)
        }
    }

    class MyApp : Application() {
        override fun onCreate() {
            super.onCreate()
            FirebaseApp.initializeApp(this)
            createNotificationChannel()
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "ride_notifications",
                    "Ride Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Channel for ride confirmations"
                }

                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CabAggregatorAppTheme {
            Greeting("Android")
        }
    }
}
