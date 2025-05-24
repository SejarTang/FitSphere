@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitsphere.ui.workout

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.gestures.gestures
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorkoutDetailScreen(
    entry: WorkoutEntity,
    navController: NavController
) {
    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date(entry.startTime))
    val routePoints = entry.route.map { Point.fromLngLat(it.longitude, it.latitude) }

    val durationMinutes = entry.duration / 60.0
    val rating = when {
        durationMinutes < 2 -> 2
        durationMinutes < 5 -> 3
        durationMinutes < 10 -> 4
        else -> 5
    }

    val performanceMessage = when (rating) {
        5 -> "Excellent performance! Keep up the great work 💪"
        4 -> "Great job! You're on the right track!"
        3 -> "Good effort. Try to push a bit more next time."
        else -> "Too short 😥 Try to exercise longer for better results."
    }

    val defeatedPercent = when (rating) {
        5 -> "98%"
        4 -> "85%"
        3 -> "60%"
        else -> "20%"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Details", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack("workout", inclusive = false)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Workout on $formattedDate", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Icon(
                imageVector = Icons.Default.DirectionsRun,
                contentDescription = "Workout icon",
                modifier = Modifier.size(48.dp),
                tint = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val durationMin = (entry.duration / 60).toInt()
                val durationStr = String.format(
                    "%02d:%02d:%02d",
                    durationMin / 60,
                    durationMin % 60,
                    (entry.duration % 60)
                )
                WorkoutInfoCard("Duration", durationStr)
                WorkoutInfoCard("Distance (km)", "%.2f".format(entry.distance))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WorkoutInfoCard("Elevation Gain", "-- m")
                WorkoutInfoCard("Calories", "${entry.calories} kcal")
            }

            Divider()

            if (routePoints.isNotEmpty()) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    factory = { ctx ->
                        MapView(ctx).apply {
                            getMapboxMap().loadStyle(
                                style(Style.MAPBOX_STREETS) {
                                    +geoJsonSource("route-source") {
                                        feature(Feature.fromGeometry(LineString.fromLngLats(routePoints)))
                                    }
                                    +lineLayer("route-layer", "route-source") {
                                        lineColor(literal("#3F51B5"))
                                        lineWidth(literal(5.0))
                                    }
                                }
                            )
                            gestures.rotateEnabled = false
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No route data", color = Color.Gray)
                }
            }

            Text("Performance Rating", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row {
                repeat(rating) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                }
                repeat(5 - rating) {
                    Icon(Icons.Default.StarBorder, contentDescription = null, tint = Color(0xFFFFC107))
                }
            }

            Text("You outperformed $defeatedPercent of users!", fontWeight = FontWeight.Medium)
            Text(performanceMessage, fontSize = 14.sp)
        }
    }
}

@Composable
fun WorkoutInfoCard(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE0E0E0))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(value, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 12.sp)
    }
}
