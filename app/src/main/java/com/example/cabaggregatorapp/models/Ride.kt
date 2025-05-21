package com.example.cabaggregatorapp.models



data class Ride(
    val service: String,
    val pickup: String,
    val drop: String,
    val fare: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val id: Int,
    val serviceType: String,
    val feedbackGiven: Boolean = false
)
