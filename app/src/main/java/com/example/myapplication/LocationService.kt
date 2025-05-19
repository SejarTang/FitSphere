package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*

class LocationService(context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 5000
    ).build()

    private val locationCallback: LocationCallback

    private val _locationList = mutableListOf<Location>()
    val locationList: List<Location> get() = _locationList

    private var isStarted = false

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.firstOrNull()?.let { location ->
                    _locationList.add(location)
                    Log.d("LocationService", "New location: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (!isStarted) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            isStarted = true
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isStarted = false
    }

    fun reset() {
        _locationList.clear()
    }

    fun calculateTotalDistance(): Float {
        var total = 0f
        for (i in 1 until _locationList.size) {
            total += _locationList[i - 1].distanceTo(_locationList[i])
        }
        return total // 单位：米
    }
}
