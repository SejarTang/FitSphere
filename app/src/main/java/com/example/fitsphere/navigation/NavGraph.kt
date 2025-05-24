package com.example.fitsphere.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import com.example.fitsphere.ui.auth.LoginScreen
import com.example.fitsphere.ui.auth.SignUpScreen
import com.example.fitsphere.ui.diet.DietScreen
import com.example.fitsphere.ui.home.HomeScreen
import com.example.fitsphere.ui.profile.ProfileScreen
import com.example.fitsphere.ui.workout.WorkoutHomeScreen
import com.example.fitsphere.ui.workout.WorkoutViewModel
import com.example.fitsphere.ui.auth.AuthViewModel
import com.example.fitsphere.ui.workout.WorkoutDetailScreen
import com.example.fitsphere.ui.workout.WorkoutSessionScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    authViewModel: AuthViewModel,
    onStartWorkout: () -> Unit,
    onGoogleLoginClicked: () -> Unit
) {

    val isLoggedIn = remember { mutableStateOf(authViewModel.isUserLoggedIn()) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn.value) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onGoogleLoginClicked = {
                    onGoogleLoginClicked()
                },
                onLoginSuccess = {
                    isLoggedIn.value = true
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToSignUp = {
                    navController.navigate("signup")
                },
                viewModel = authViewModel
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    isLoggedIn.value = true
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            if (isLoggedIn.value) {
                HomeScreen(navController)
            } else {
                navController.navigate("login")
            }
        }

        composable("workout") {
            if (isLoggedIn.value) {
                WorkoutHomeScreen(
                    viewModel = workoutViewModel,
                    onStartWorkout = onStartWorkout,
                    navController = navController
                )
            } else {
                navController.navigate("login")
            }
        }

        composable("diet") {
            if (isLoggedIn.value) {
                DietScreen()
            } else {
                navController.navigate("login")
            }
        }

        composable("profile") {
            if (isLoggedIn.value) {
                ProfileScreen()
            } else {
                navController.navigate("login")
            }
        }

        composable("detail") {
            val entry = navController.previousBackStackEntry
                ?.savedStateHandle?.get<WorkoutEntity>("workoutEntry")

            if (entry != null) {
                WorkoutDetailScreen(
                    entry = entry,
                    navController = navController
                )
            }
        }
    }
}
