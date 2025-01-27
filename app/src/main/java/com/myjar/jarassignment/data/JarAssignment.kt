package com.myjar.jarassignment.data

import android.app.Application
import android.content.Context
import com.myjar.jarassignment.data.local.LocalPreferences

class JarAssignment : Application() {

    override fun onCreate() {
        super.onCreate()

        Dependencies.initialize(this)
    }
}

object Dependencies {
    lateinit var myPreferences: LocalPreferences

    fun initialize(context: Context) {
        myPreferences = LocalPreferences(context)
    }

    fun getLocalPreferences(): LocalPreferences {
        return myPreferences
    }
}