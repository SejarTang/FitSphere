package com.example.fitsphere.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf("home", "workout", "diet", "profile")

    NavigationBar(containerColor = Color.Black) {
        items.forEach { route ->
            val icon = when (route) {
                "home" -> Icons.Default.Home
                "workout" -> Icons.Default.DirectionsWalk
                "diet" -> Icons.Default.Coffee
                "profile" -> Icons.Default.Person
                else -> Icons.Default.Help
            }

            val selected = when (route) {
                "workout" -> currentRoute in listOf("workout", "start", "session", "detail")
                else -> currentRoute == route
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = route, tint = Color.White) },
                label = { Text(route.replaceFirstChar { it.uppercase() }, color = Color.White) }
            )
        }
    }
}
