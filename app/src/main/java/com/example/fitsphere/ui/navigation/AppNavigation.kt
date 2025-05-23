package com.example.fitsphere.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import com.example.fitsphere.ui.BottomBar
import com.example.fitsphere.ui.auth.LoginScreen
import com.example.fitsphere.ui.diet.DietScreen
import com.example.fitsphere.ui.home.HomeScreen
import com.example.fitsphere.ui.home.WorkoutDetailScreen as VideoWorkoutDetailScreen
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
                val args = backStackEntry.arguments!!
                VideoWorkoutDetailScreen(
                    navController = navController,
                    title = args.getString("title")!!,
                    videoId = args.getString("videoId")!!,
                    description = args.getString("description")!!,
                    intensity = args.getString("intensity")!!,
                    duration = args.getString("duration")!!
                )
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
