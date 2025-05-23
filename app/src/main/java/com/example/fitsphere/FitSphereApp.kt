package com.example.fitsphere

import android.app.Application
import com.example.fitsphere.di.DatabaseProvider

class FitSphereApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.initDatabase(this)
    }
}
