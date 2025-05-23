package com.example.myapplication.mainScreenPage

/*
This setup includes:
1. Navigation logic to route to WorkoutDetailScreen.
2. MainScreen workout cards with clickable navigation.
3. WorkoutDetailScreen that displays a YouTube thumbnail and info.
*/

// File: AppNav.kt

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
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
            WorkoutDetailScreen(
                navController = navController,
                title = args.getString("title")!!,
                videoId = args.getString("videoId")!!,
                description = args.getString("description")!!,
                intensity = args.getString("intensity")!!,
                duration = args.getString("duration")!!
            )
        }
    }
}