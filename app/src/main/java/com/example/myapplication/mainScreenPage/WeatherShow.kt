package com.example.myapplication.mainScreenPage

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.api.HourlyWeather
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherShow(viewModel: ViewWeatherModel = viewModel()) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val weatherData by viewModel.weatherData.collectAsState()
    val hourlyData by viewModel.hourlyData.collectAsState()

    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted) {
            fetchLocationAndWeather(context, viewModel)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color.White)
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Location", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        val condition = weatherData?.current?.weather?.firstOrNull()?.main ?: "Loading..."
        val feelsLike = weatherData?.current?.temp?.toInt()?.toString() ?: "--"

        Text(
            text = "🌤 $condition | Feels like $feelsLike°C",
            fontSize = 16.sp,
            color = Color(0xFF2196F3)
        )

        Divider(thickness = 1.dp, color = Color.LightGray)

        Text("Hourly Forecast", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(hourlyData) { weather -> WeatherHourCard(weather) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherInfoBox(
                    title = "💨 Wind",
                    value = "${weatherData?.current?.wind_speed ?: "--"} km/h",
                    modifier = Modifier.weight(1f).height(100.dp)
                )
                WeatherInfoBox(
                    title = "💧 Humidity",
                    value = "${weatherData?.current?.humidity ?: "--"}%",
                    modifier = Modifier.weight(1f).height(100.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherInfoBox(
                    title = "🌅 Sunrise",
                    value = "7:20 AM", // mock value
                    modifier = Modifier.weight(1f).height(100.dp)
                )
                WeatherInfoBox(
                    title = "🌧 Precipitation",
                    value = "7mm in last 24h", // mock value
                    modifier = Modifier.weight(1f).height(100.dp)
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun fetchLocationAndWeather(context: android.content.Context, viewModel: ViewWeatherModel) {
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

@Composable
fun WeatherHourCard(data: HourlyWeather) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(data.time, fontSize = 14.sp, color = Color.DarkGray)
            Text(data.icon, fontSize = 26.sp)
            Text(data.temperature, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
        }
    }
}

@Composable
fun WeatherInfoBox(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF90CAF9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
        }
    }
}
