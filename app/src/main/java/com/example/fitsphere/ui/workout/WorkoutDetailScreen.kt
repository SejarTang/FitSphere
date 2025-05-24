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
import androidx.navigation.NavController
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorkoutDetailScreen(
    entry: WorkoutEntity,
    navController: NavController
) {
    val context = LocalContext.current
    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date(entry.startTime))


    val durationStars = (entry.duration / 600).toInt()
    val calorieStars = (entry.calories / 100)
    val totalStars = (durationStars + calorieStars).coerceIn(1, 5)


    val beatenPercent = (50 + totalStars * 10).coerceAtMost(99)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Details", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("workout") {
                            popUpTo("workout") { inclusive = true }
                            launchSingleTop = true
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


            Text("Performance Rating", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row {
                repeat(totalStars) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                }
                repeat(5 - totalStars) {
                    Icon(Icons.Default.StarBorder, contentDescription = null, tint = Color(0xFFFFC107))
                }
            }

            Text("You outperformed $beatenPercent% of users!", fontWeight = FontWeight.Medium)
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
