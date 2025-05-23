package com.example.myapplication.mainScreenPage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class FitnessData(
    val stepsToday: Int = 0,
    val sleepHours: Int = 0,
    val waterLitres: Double = 0.0,
    val workoutMinutes: Int = 0
)

fun generateTip(data: FitnessData?, mood: String = "positive"): String {
    return when {
        data == null -> listOf(
            "💧 Stay hydrated throughout your day.",
            "🧘‍♀️ Take 5 minutes to stretch every hour.",
            "🥗 Eat more greens and whole foods.",
            "🏃 Move at least 30 minutes a day.",
            "😴 Aim for 7–8 hours of sleep each night."
        ).random()

        data.stepsToday < 3000 -> if (mood == "fun") "😅 Your feet miss you! Walk a bit today?" else "🚶 Try walking more to boost your activity."
        data.sleepHours < 6 -> if (mood == "fun") "🦉 Night owl? Even owls need naps!" else "😴 Try to get more sleep for recovery."
        data.waterLitres < 1.5 -> if (mood == "fun") "🚰 Your body called — it wants more water." else "💧 Hydration is key to staying healthy."
        data.workoutMinutes < 20 -> if (mood == "fun") "🏋️ Your muscles are napping — wake them up!" else "🔥 Even a short workout can energize you."
        else -> "🎉 You're doing awesome! Keep going!"
    }
}

@Composable
fun TipScreen(fitnessData: FitnessData? = null) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var tip by remember { mutableStateOf(generateTip(fitnessData)) }
    var mood by remember { mutableStateOf("positive") }

    val greeting = getGreetingMessage()
    val currentTime = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            "Time Unavailable"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.background)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("$greeting 🌞", fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 Fitness Tip", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(tip, fontSize = 16.sp, lineHeight = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    val newTip = generateTip(fitnessData, mood)
                    if (newTip != tip) tip = newTip
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Tip")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Tip")
                }

                Button(onClick = {
                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(tip))
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Copy")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = {
                mood = if (mood == "positive") "fun" else "positive"
                tip = generateTip(fitnessData, mood)
            }) {
                Text("Switch Mood: ${if (mood == "positive") "Fun 😄" else "Positive ✨"}")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Last updated: $currentTime", fontSize = 12.sp)
        }
    }
}

fun getGreetingMessage(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val now = LocalTime.now()
        when {
            now.hour in 5..11 -> "Good Morning"
            now.hour in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    } else {
        "Hello"
    }
}

@Preview(showBackground = true)
@Composable
fun TipScreenPreview() {
    TipScreen()
}

@Preview(showBackground = true)
@Composable
fun TipScreenWithDataPreview() {
    TipScreen(
        fitnessData = FitnessData(
            stepsToday = 1800,
            sleepHours = 5,
            waterLitres = 0.9,
            workoutMinutes = 8
        )
    )
}