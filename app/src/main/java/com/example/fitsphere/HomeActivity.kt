package com.example.fitsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.fitsphere.ui.workout.WorkoutViewModel
import com.example.fitsphere.ui.navigation.AppNavigation

class HomeActivity : ComponentActivity() {
    private val workoutViewModel: WorkoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(viewModel = workoutViewModel)
        }
    }
}

