// WeatherUtils.kt
package com.example.fitsphere.util

import android.Manifest
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.fitsphere.ui.home.HomeViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

suspend fun fetchLocationAndWeather(context: Context, viewModel: HomeViewModel) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    val hasPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PermissionChecker.PERMISSION_GRANTED

    if (hasPermission) {
        try {
            val location: Location? = fusedClient.lastLocation.await()
            if (location != null) {
                viewModel.fetchWeather(location.latitude, location.longitude)
                return
            } else {
                Toast.makeText(context, "Location not found, using Melbourne", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Location error, using Melbourne", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Permission denied, using Melbourne", Toast.LENGTH_SHORT).show()
    }

    // fallback to Melbourne
    viewModel.fetchWeather(-37.8136, 144.9631)
}
