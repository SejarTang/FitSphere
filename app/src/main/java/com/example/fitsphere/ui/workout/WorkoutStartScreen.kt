package com.example.fitsphere.ui.workout


import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.fitsphere.ui.workout.WorkoutViewModel
import kotlinx.coroutines.delay


@Composable
fun WorkoutNavHost() {
    val navController = rememberNavController()
    val viewModel: WorkoutViewModel = viewModel()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartWorkoutScreen(
                onBack = {},
                onStart = {
                    navController.navigate("session")
                }
            )
        }
        composable("session") {
            WorkoutSessionScreen(
                navController = navController,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartWorkoutScreen(onBack: () -> Unit = {}, onStart: () -> Unit = {}) {
    val workoutTypes = listOf("Running", "Cycling", "Hiking")
    var selectedType by remember { mutableStateOf("Running") }

    val viewModel: WorkoutViewModel = viewModel()
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }

    var workoutStartTime by remember { mutableStateOf<Long?>(null) }
    var elapsedTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        locationService.reset()
        locationService.startLocationUpdates()
    }

    if (workoutStartTime != null) {
        LaunchedEffect(workoutStartTime) {
            while (true) {
                elapsedTime = (System.currentTimeMillis() - workoutStartTime!!) / 1000
                delay(1000)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Start Workout", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select Workout Type", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))

            workoutTypes.forEach { type ->
                val isSelected = selectedType == type
                val icon = when (type) {
                    "Running" -> Icons.Default.DirectionsRun
                    "Cycling" -> Icons.Default.DirectionsBike
                    "Hiking" -> Icons.Default.Terrain
                    else -> Icons.Default.FitnessCenter
                }
                Button(
                    onClick = { selectedType = type },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.Black else Color.LightGray,
                        contentColor = if (isSelected) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(icon, contentDescription = type, modifier = Modifier.padding(end = 8.dp))
                    Text(type, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start", fontSize = 18.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStartWorkoutScreen() {
    StartWorkoutScreen()
}