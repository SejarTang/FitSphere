package com.example.fitsphere.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import com.example.fitsphere.ui.BottomBar
import com.example.fitsphere.ui.auth.LoginScreen
import com.example.fitsphere.ui.diet.DietScreen
import com.example.fitsphere.ui.home.*
import com.example.fitsphere.ui.profile.ProfileScreen
import com.example.fitsphere.ui.workout.*

@Composable
fun AppNavigation(viewModel: WorkoutViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {

            // Home Tab
            composable("home") {
                HomeScreen(navController)
            }

            // Workout Tab
            composable("workout") {
                WorkoutHomeScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onStartWorkout = { navController.navigate("start") }
                )
            }

            composable("start") {
                StartWorkoutScreen(
                    onBack = { navController.popBackStack() },
                    onStart = { navController.navigate("session") }
                )
            }

            composable("session") {
                WorkoutSessionScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable("detail") {
                val entry = navController.previousBackStackEntry
                    ?.savedStateHandle?.get<WorkoutEntity>("workoutEntry")

                if (entry != null) {
                    WorkoutDetailScreen(
                        entry = entry,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // Workout video detail (from Home)
            composable(
                route = "workout/{title}/{videoId}/{description}/{intensity}/{duration}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("videoId") { type = NavType.StringType },
                    navArgument("description") { type = NavType.StringType },
                    navArgument("intensity") { type = NavType.StringType },
                    navArgument("duration") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
                val description = backStackEntry.arguments?.getString("description") ?: ""
                val intensity = backStackEntry.arguments?.getString("intensity") ?: ""
                val duration = backStackEntry.arguments?.getString("duration") ?: ""

                VideoWorkoutDetailScreen(
                    navController = navController,
                    title = title,
                    videoId = videoId,
                    description = description,
                    intensity = intensity,
                    duration = duration
                )
            }

            // Tip screen
            composable(
                route = "tip/{tip}",
                arguments = listOf(navArgument("tip") { type = NavType.StringType })
            ) { backStackEntry ->
                val tip = backStackEntry.arguments?.getString("tip")
                TipScreen(passedTip = tip, navController = navController)
            }

            // ✅ Weather Detail screen
            composable("weatherDetail") {
                val homeViewModel: HomeViewModel = viewModel()
                WeatherDetailScreen(viewModel = homeViewModel, navController = navController)
            }

            // Diet Tab
            composable("diet") {
                DietScreen()
            }

            // Profile Tab
            composable("profile") {
                ProfileScreen()
            }
        }
    }
}
