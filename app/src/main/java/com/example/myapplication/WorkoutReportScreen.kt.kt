package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val locationService = remember { LocationService(context) }

    // 权限请求
    LaunchedEffect(Unit) {
        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        } else {
            locationService.startLocationUpdates()
        }
    }

    var elapsedSeconds by remember { mutableStateOf(0L) }

    LaunchedEffect(true) {
        locationService.reset()
        while (true) {
            delay(1000)
            elapsedSeconds++
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            locationService.stopLocationUpdates()
        }
    }

    val rotatingAngle by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing))
    )

    val totalDistanceMeters = locationService.calculateTotalDistance()
    val distanceKm = totalDistanceMeters / 1000f
    val elevationGain = (elapsedSeconds / 30) * 2 // 每 30 秒增加 2 米
    val calories = (distanceKm * 60).toInt() // 每公里消耗 60 千卡

    val formattedTime = String.format(
        "%02d:%02d:%02d",
        elapsedSeconds / 3600,
        (elapsedSeconds % 3600) / 60,
        elapsedSeconds % 60
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Session", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(selected = true, onClick = {}, icon = {
                    Icon(Icons.Default.DirectionsWalk, contentDescription = "Workout", tint = Color.White)
                }, label = { Text("Workout", color = Color.White) }, alwaysShowLabel = true)
                NavigationBarItem(selected = false, onClick = {}, icon = {
                    Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                }, label = { Text("Home", color = Color.White) }, alwaysShowLabel = true)
                NavigationBarItem(selected = false, onClick = {}, icon = {
                    Icon(Icons.Default.Coffee, contentDescription = "Diet", tint = Color.White)
                }, label = { Text("Diet", color = Color.White) }, alwaysShowLabel = true)
                NavigationBarItem(selected = false, onClick = {}, icon = {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                }, label = { Text("Profile", color = Color.White) }, alwaysShowLabel = true)
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.DirectionsRun,
                contentDescription = "Active",
                modifier = Modifier.size(48.dp).rotate(rotatingAngle),
                tint = Color.Black
            )

            StatItem(title = "Duration", value = formattedTime)
            StatItem(title = "Distance (km)", value = String.format("%.2f", distanceKm))
            StatItem(title = "Elevation Gain (m)", value = "$elevationGain m")
            StatItem(title = "Estimated Calories Burned", value = "$calories kcal")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // End workout 逻辑预留：可以保存数据到数据库
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("End Workout", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE0E0E0))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
