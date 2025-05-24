// WeatherDetailScreen.kt
package com.example.fitsphere.ui.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.fitsphere.util.fetchLocationAndWeather
import com.example.fitsphere.weatherAPI.ForecastItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailScreen(viewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val weatherData by viewModel.weather.collectAsStateWithLifecycle()
    val forecastData by viewModel.forecast.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted) {
            fetchLocationAndWeather(context, viewModel)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    val formattedTime = weatherData?.let {
        val offsetSeconds = it.timezone // in seconds
        val localTime = Instant.now().atOffset(ZoneOffset.UTC).plusSeconds(offsetSeconds.toLong())
        localTime.format(DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy"))
    } ?: "--"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(" ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            if (permissionState.status.isGranted) {
                                fetchLocationAndWeather(context, viewModel)
                            } else {
                                permissionState.launchPermissionRequest()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val city = forecastData?.city?.name ?: "Unknown"
            val country = forecastData?.city?.country ?: ""
            Text("$city, $country", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            val temp = weatherData?.main?.temp?.toInt()?.toString() ?: "--"
            val conditionText = weatherData?.weather?.firstOrNull()?.main ?: "Loading..."

            Text(
                text = "🌤 $conditionText | Feels like $temp°C",
                fontSize = 16.sp,
                color = Color(0xFF2196F3)
            )

            Text("As of $formattedTime", fontSize = 14.sp, color = Color.Gray)

            Divider(thickness = 1.dp, color = Color.LightGray)

            Text("Forecast", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            forecastData?.list?.take(8)?.let { forecastList ->
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(forecastList) { item ->
                        ForecastCard(item = item)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WeatherInfoBox(
                        title = "💨 Wind",
                        value = "${weatherData?.wind?.speed ?: "--"} km/h",
                        modifier = Modifier.weight(1f).height(100.dp)
                    )
                    WeatherInfoBox(
                        title = "💧 Humidity",
                        value = "${weatherData?.main?.humidity ?: "--"}%",
                        modifier = Modifier.weight(1f).height(100.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WeatherInfoBox(
                        title = "🌅 Sunrise",
                        value = "7:20 AM",
                        modifier = Modifier.weight(1f).height(100.dp)
                    )
                    WeatherInfoBox(
                        title = "🌧 Precipitation",
                        value = "7mm in last 24h",
                        modifier = Modifier.weight(1f).height(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Weather data powered by OpenWeatherMap",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ForecastCard(item: ForecastItem) {
    val dateTimeParts = item.dtTxt.split(" ")
    val date = dateTimeParts.getOrNull(0) ?: ""
    val time = dateTimeParts.getOrNull(1) ?: ""

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(date, fontSize = 12.sp, color = Color.DarkGray)
            Text(time, fontSize = 14.sp, color = Color.DarkGray)
            Text(item.weather.firstOrNull()?.main ?: "N/A", fontSize = 14.sp)
            Text("${item.main.temp.toInt()}°C", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
