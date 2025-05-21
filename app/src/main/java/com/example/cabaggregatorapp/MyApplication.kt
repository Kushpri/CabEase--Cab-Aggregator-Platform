package com.example.cabaggregatorapp

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.FirebaseApp


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Ensure Firebase initializes
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}
