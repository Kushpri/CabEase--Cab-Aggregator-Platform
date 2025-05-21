
package com.example.cabaggregatorapp.utils


import android.content.Context
import com.example.cabaggregatorapp.models.Ride
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RideHistoryManager(context: Context) {
    private val prefs = context.getSharedPreferences("ride_history", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveRide(ride: Ride) {
        val rides = getRides().toMutableList()
        rides.add(ride)
        prefs.edit().putString("rides", gson.toJson(rides)).apply()
    }

    fun getRides(): List<Ride> {
        val json = prefs.getString("rides", "[]")
        val type = object : TypeToken<List<Ride>>() {}.type
        return gson.fromJson(json, type)
    }
}
