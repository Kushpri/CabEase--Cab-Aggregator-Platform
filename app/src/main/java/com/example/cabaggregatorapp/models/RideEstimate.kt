package com.example.cabaggregatorapp.com.example.cabaggregatorapp.models


data class RideEstimate(
    val service: String,
    val vehicleType: String, // e.g. Bike, Car, Auto
    val fare: Double,
    val time: String
)
