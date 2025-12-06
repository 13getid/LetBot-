package com.example.letbot

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}