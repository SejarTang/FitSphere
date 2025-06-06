package com.example.fitsphere.ui.workout

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.SystemClock
import android.util.Log
import com.example.fitsphere.data.local.database.entity.LatLngEntity
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
    private var startTimeMillis: Long = 0

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
            startTimeMillis = SystemClock.elapsedRealtime()
            isStarted = true
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isStarted = false
    }

    fun getRoute(): List<Location> {
        return locationList
    }

    fun getLatLngRoute(): List<LatLngEntity> {
        return locationList.map {
            LatLngEntity(it.latitude, it.longitude)
        }
    }

    fun calculateTotalDistance(): Float {
        var total = 0f
        for (i in 1 until _locationList.size) {
            total += _locationList[i - 1].distanceTo(_locationList[i])
        }
        return total
    }

    fun calculateDurationSeconds(): Long {
        return if (startTimeMillis != 0L) {
            (SystemClock.elapsedRealtime() - startTimeMillis) / 1000 // return in second
        } else {
            0
        }
    }

    fun reset() {
        _locationList.clear()
        startTimeMillis = 0
        isStarted = false
    }
}
