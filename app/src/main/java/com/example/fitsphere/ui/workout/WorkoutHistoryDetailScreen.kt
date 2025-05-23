@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitsphere.ui.workout

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview

data class WorkoutRecord(val dateTime: String, val duration: Int)

@Composable
fun WorkoutHistoryScreen() {
    val workoutHistory = remember {
        mutableStateListOf(
            WorkoutRecord("2025-04-12 08:30", 45),
            WorkoutRecord("2025-04-10 17:00", 30),
            WorkoutRecord("2025-04-08 19:15", 60)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Start Workout Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Start workout action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.DirectionsRun, contentDescription = "Start", modifier = Modifier.padding(end = 8.dp))
                    Text("Start Workout", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Workout History Title
            Text(
                text = "Workout History",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Black
            )

            // Workout History List
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(workoutHistory) { record ->
                    WorkoutHistoryItem(record)
                }
            }
        }
    }
}


@Composable
fun WorkoutHistoryItem(record: WorkoutRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE0E0E0))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Time: ${record.dateTime}", fontSize = 14.sp, color = Color.Black)
        Text(text = "Duration: ${record.duration} mins", fontSize = 14.sp, color = Color.Black)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWorkoutHistoryScreen() {
    WorkoutHistoryScreen()
}