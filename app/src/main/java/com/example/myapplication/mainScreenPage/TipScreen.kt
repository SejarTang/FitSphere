package com.example.myapplication.mainScreenPage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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

@Composable
fun TipScreen() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var tip by remember { mutableStateOf(generateRandomTip()) }

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
            Text("💡 Fitness Tip", fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(tip, fontSize = 16.sp, lineHeight = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    tip = generateRandomTip()
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
        }
    }
}

fun generateRandomTip(): String {
    return listOf(
        "💧 Stay hydrated throughout your day.",
        "🧘‍♀️ Take 5 minutes to stretch every hour.",
        "🥗 Eat more greens and whole foods.",
        "🏃 Move at least 30 minutes a day.",
        "😴 Aim for 7–8 hours of sleep each night.",
        "🔥 Even a short workout can energize you.",
        "🚰 Your body called — it wants more water.",
        "🏋️ Wake up your muscles with some light exercise."
    ).random()
}

@Preview(showBackground = true)
@Composable
fun TipScreenPreview() {
    TipScreen()
}
