package com.example.fitsphere.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitsphere.ui.home.HomeScreen
import com.example.fitsphere.ui.workout.WorkoutHomeScreen
import com.example.fitsphere.ui.diet.DietScreen
import com.example.fitsphere.ui.profile.ProfileScreen
import com.example.fitsphere.ui.workout.WorkoutViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    onStartWorkout: () -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("workout") {
            WorkoutHomeScreen(
                viewModel = workoutViewModel,
                onStartWorkout = onStartWorkout,
                navController = navController
            )
        }
        composable("diet") {
            DietScreen()
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}
