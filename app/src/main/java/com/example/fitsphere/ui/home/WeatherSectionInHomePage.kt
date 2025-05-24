package com.example.fitsphere.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherSectionInHomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val weatherData by viewModel.weather.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted) {
            fetchLocationAndWeather(context, viewModel)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("weatherDetail") }
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val condition = weatherData?.weather?.firstOrNull()?.main ?: "Loading"
            val temp = weatherData?.main?.temp?.toInt()?.toString() ?: "--"


            Text("Today's Weather", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "🌤 $condition | Feels like $temp°C",
                fontSize = 16.sp,
                color = Color(0xFF2196F3)
            )
            Text(
                text = "Tap for more details →",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun fetchLocationAndWeather(context: Context, viewModel: HomeViewModel) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    val location: Location? = try {
        client.lastLocation.await()
    } catch (e: Exception) {
        null
    }
    location?.let {
        viewModel.fetchWeather(it.latitude, it.longitude)
    }
}
