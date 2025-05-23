package com.example.fitsphere

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fitsphere.navigation.NavGraph
import com.example.fitsphere.ui.BottomBar
import com.example.fitsphere.ui.navigation.AppNavigation
import com.example.fitsphere.ui.workout.WorkoutViewModel



@Composable
fun MainScreen() {
    val viewModel: WorkoutViewModel = viewModel()

    AppNavigation(viewModel = viewModel)
}

