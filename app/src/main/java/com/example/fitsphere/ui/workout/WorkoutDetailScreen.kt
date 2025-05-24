@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitsphere.ui.workout

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
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorkoutDetailScreen(
    entry: WorkoutEntity,
    navController: NavController
) {
    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date(entry.startTime))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Details", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("workout") {
                            popUpTo("start") { inclusive = true }
                        }
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

            Text("Workout Route", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            // Mapbox 地图（带模拟折线路线）
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AndroidView(
                    factory = { context ->
                        MapView(context).apply {
                            getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
                                val mapboxMap = getMapboxMap()
                                val center = Point.fromLngLat(144.9631, -37.8136)
                                mapboxMap.setCamera(
                                    CameraOptions.Builder()
                                        .center(center)
                                        .zoom(14.0)
                                        .build()
                                )

                                // 创建 Polyline 路线
                                val annotationApi = this.annotations
                                val polylineManager = annotationApi.createPolylineAnnotationManager()

                                val routePoints = listOf(
                                    Point.fromLngLat(144.9631, -37.8136),
                                    Point.fromLngLat(144.9645, -37.8145),
                                    Point.fromLngLat(144.9660, -37.8132),
                                    Point.fromLngLat(144.9675, -37.8120)
                                )

                                val polyline = PolylineAnnotationOptions()
                                    .withPoints(routePoints)
                                    .withLineColor("#ee4e8b")
                                    .withLineWidth(5.0)
                                polylineManager.create(polyline)
                            }
                        }
                    }
                )
            }

            Divider()

            Text("Performance Rating", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row {
                repeat(4) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                }
                Icon(Icons.Default.StarBorder, contentDescription = null, tint = Color(0xFFFFC107))
            }

            Text("You outperformed 82% of users!", fontWeight = FontWeight.Medium)
            Text("Keep pushing your limits — you're doing great! 💪", fontSize = 14.sp)
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
