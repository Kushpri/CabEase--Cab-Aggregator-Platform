package com.example.cabaggregatorapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.cabaggregatorapp.R
import androidx.compose.foundation.layout.*
import androidx.core.content.ContextCompat.startActivity
import com.example.cabaggregatorapp.MainActivity

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily



@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    // Animation for logo scale
    val scale = remember { Animatable(0f) }

    // Start animation and then navigate
    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
        delay(1200)
        onNavigateToHome()
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3F51B5), Color(0xFF673AB7)) // gradient purple
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),  // Replace with your image file name
                contentDescription = "Splash Screen Logo",
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
            ) // Adjust size as needed


            // Add space between logo and text
            Spacer(modifier = Modifier.height(20.dp))  // Adjust height as needed

            Text(
                text = "Cab Aggregator App",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Your Smart Cab Booking Solution",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            // Delay for 2 seconds before navigating to HomeScreen
            LaunchedEffect(Unit) {
                delay(2000)
                onNavigateToHome()
            }
        }
    }


}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onNavigateToHome = {})
}



class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
