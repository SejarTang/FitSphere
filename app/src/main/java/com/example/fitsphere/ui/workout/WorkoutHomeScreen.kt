package com.example.fitsphere.ui.workout


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import com.example.fitsphere.ui.workout.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHomeScreen(
    viewModel: WorkoutViewModel,
    onStartWorkout: () -> Unit,
    navController: NavController
) {
    val workoutHistory by viewModel.workoutList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Fitness", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }

        // ✅ bottomBar 已删除
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onStartWorkout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.DirectionsRun, contentDescription = "Start", modifier = Modifier.padding(end = 8.dp))
                    Text("Start Workout", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Workout History",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Black
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(workoutHistory) { entry ->
                    WorkoutRecordItem(entry, navController)
                }
            }
        }
    }
}


@Composable
fun WorkoutRecordItem(entry: WorkoutEntity, navController: NavController) {
    val dateTime = remember(entry.startTime) {
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(entry.startTime))
    }

    val durationMin = (entry.duration / 60).toInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE0E0E0))
            .clickable {

                navController.currentBackStackEntry?.savedStateHandle?.set("workoutEntry", entry)
                navController.navigate("detail")
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Time: $dateTime", fontSize = 14.sp, color = Color.Black)
        Text(text = "Duration: ${durationMin} mins", fontSize = 14.sp, color = Color.Black)
    }
}