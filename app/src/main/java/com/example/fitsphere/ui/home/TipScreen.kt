// WeatherDetailScreen.kt
package com.example.fitsphere.ui.home

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitsphere.util.TipUtil
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipScreen(passedTip: String?, navController: NavController) {
    val clipboardManager = LocalClipboardManager.current
    var tip by remember { mutableStateOf(passedTip ?: TipUtil.generateTip()) }

    val greeting = getGreetingMessage()
    val currentTime = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        } else "Time Unavailable"
    }

    val moodOptions = listOf("positive", "fun", "calm", "focus", "dark")
    val moodEmojis = mapOf(
        "positive" to "🌞",
        "fun" to "😄",
        "calm" to "🧘",
        "focus" to "🎯",
        "dark" to "🌙"
    )
    var moodIndex by remember { mutableIntStateOf(0) }
    val mood = moodOptions[moodIndex]
    val emoji = moodEmojis[mood] ?: "💡"

    val bgBrush = getMoodGradient(mood)
    val topBarColor by animateColorAsState(targetValue = getMoodColor(mood), label = "AppBarColor")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        " ",
                        color = if (mood == "dark") Color.White else Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = if (mood == "dark") Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(brush = bgBrush)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Greeting
            Text(
                text = "$emoji $greeting",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tip Block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "💡 Daily Fitness Tip",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(tip, fontSize = 16.sp, lineHeight = 24.sp)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        var newTip: String
                        do {
                            newTip = TipUtil.generateTip()
                        } while (newTip == tip)
                        tip = newTip
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Tip")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Tip")
                    }

                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(tip))
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Tip")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy")
                    }
                }
            }

            // Mood Selector
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                        .clickable {
                            moodIndex = (moodIndex + 1) % moodOptions.size
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(getMoodColor(mood), shape = MaterialTheme.shapes.small)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Mood: ${mood.uppercase()} ${emoji}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "(Tap to switch mood)",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Last updated: $currentTime",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
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
    } else "Hello"
}

fun getMoodGradient(mood: String): Brush {
    return when (mood) {
        "fun" -> Brush.verticalGradient(listOf(Color(0xFFFFCDD2), Color(0xFFFFF8E1)))
        "positive" -> Brush.verticalGradient(listOf(Color(0xFFB3E5FC), Color.White))
        "calm" -> Brush.verticalGradient(listOf(Color(0xFFE0F7FA), Color(0xFFF1F8E9)))
        "focus" -> Brush.verticalGradient(listOf(Color(0xFFE8EAF6), Color(0xFFE3F2FD)))
        "dark" -> Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF37474F)))
        else -> Brush.verticalGradient(listOf(Color.LightGray, Color.White))
    }
}

fun getMoodColor(mood: String): Color {
    return when (mood) {
        "fun" -> Color(0xFFFFCDD2)
        "positive" -> Color(0xFFB3E5FC)
        "calm" -> Color(0xFFE0F7FA)
        "focus" -> Color(0xFFE8EAF6)
        "dark" -> Color(0xFF263238)
        else -> Color.LightGray
    }
}
