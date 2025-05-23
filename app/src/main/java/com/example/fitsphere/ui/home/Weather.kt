package com.example.fitsphere.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.fitsphere.data.remote.OneCallWeatherResponse
import com.example.fitsphere.data.remote.WeatherApiService

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen() {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var weatherData by remember { mutableStateOf<OneCallWeatherResponse?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var permissionRequested by remember { mutableStateOf(false) }
    var locationSkipped by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (locationPermissionState.status.isGranted) {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

                @SuppressLint("MissingPermission")
                val location: Location? = withContext(Dispatchers.IO) {
                    fusedLocationClient.lastLocation.await()
                }

                if (location != null) {
                    val response = withContext(Dispatchers.IO) {
                        WeatherApiService.create().getCurrentWeather(
                            lat = location.latitude,
                            lon = location.longitude,
                            apiKey = "5b9fe71dc1675f8a3c71d874470ed3c2"
                        )
                    }
                    weatherData = response
                } else {
                    error = "Unable to retrieve location."
                }
            } catch (e: Exception) {
                error = e.localizedMessage
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            !locationPermissionState.status.isGranted && !permissionRequested && !locationSkipped -> {
                Text("📍 This app would like to access your location to display weather.")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        permissionRequested = true
                        locationPermissionState.launchPermissionRequest()
                    }) {
                        Text("Allow")
                    }
                    Button(onClick = {
                        locationSkipped = true
                    }) {
                        Text("Skip")
                    }
                }
            }

            locationSkipped -> {
                Text("📍 Location access skipped.")
                Text("No weather data available.")
            }

            error != null -> {
                Text("❌ Error loading weather: $error", color = MaterialTheme.colorScheme.error)
            }

            weatherData != null -> {
                val current = weatherData!!.current
                Text("☁️ Weather at your location", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("🌡️ Temperature: ${current.temp} °C")
                Text("💧 Humidity: ${current.humidity} %")
                Text("💨 Wind Speed: ${current.wind_speed} m/s")
            }

            else -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("☁️ Weather at your location", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("🌡️ Temperature: 18.2 °C")
        Text("💧 Humidity: 82 %")
        Text("💨 Wind Speed: 4.5 m/s")
    }
}