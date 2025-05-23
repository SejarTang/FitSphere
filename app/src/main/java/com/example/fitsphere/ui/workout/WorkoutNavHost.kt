package com.example.myapplication.ui.workout

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitsphere.ui.workout.WorkoutDetailScreen
import com.example.fitsphere.ui.workout.WorkoutHomeScreen
import com.example.fitsphere.ui.workout.WorkoutViewModel
import com.example.myapplication.data.local.database.entity.WorkoutEntity

@Composable
fun WorkoutNavHost(viewModel: WorkoutViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "history") {

        composable("start") {
            StartWorkoutScreen(
                onBack = { navController.popBackStack() },
                onStart = {
                    navController.navigate("session")
                }
            )
        }

        composable("session") {
            WorkoutSessionScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable("history") {
            WorkoutHomeScreen(
                viewModel = viewModel,
                onStartWorkout = { navController.navigate("start") },
                navController = navController
            )
        }

        composable("detail") {
            val entry = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<WorkoutEntity>("workoutEntity")

            if (entry != null) {
                WorkoutDetailScreen(entry)
            } else {
                Log.e("WorkoutNavHost", "Workout not found")
                Scaffold {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Workout not found", color = Color.Red)
                    }
                }
            }
        }
    }
}