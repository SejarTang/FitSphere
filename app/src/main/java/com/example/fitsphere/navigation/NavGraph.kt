//package com.example.myapplication.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.myapplication.DietScreen
//import com.example.myapplication.ui.home.HomeScreen
//import com.example.myapplication.ui.ProfileScreen
//import com.example.myapplication.ui.WorkoutScreen
//
//sealed class Screen(val route: String) {
//    object Home : Screen("home")
//    object Workout : Screen("workout")
//    object Diet : Screen("diet")
//    object Profile : Screen("profile")
//}
//
//@Composable
//fun AppNavGraph(navController: NavHostController) {
//    NavHost(navController = navController, startDestination = Screen.Diet.route) {
//        composable(Screen.Home.route) { HomeScreen() }
//        composable(Screen.Workout.route) { WorkoutScreen() }
//        composable(Screen.Diet.route) { DietScreen() }
//        composable(Screen.Profile.route) { ProfileScreen() }
//    }
//}
