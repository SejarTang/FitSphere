@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GridItem(val title: String, val imageRes: Int, val link: String)

@Composable
fun DietScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diet & Nutrition", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.DirectionsWalk, contentDescription = "Workout", tint = Color.White) },
                    label = { Text("Workout", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Coffee, contentDescription = "Diet", tint = Color.White) },
                    label = { Text("Diet", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White) },
                    label = { Text("Profile", color = Color.White) },
                    alwaysShowLabel = true
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionWithBackground(
                title = "Today's Diet Tip",
                content = "Eat a balanced breakfast rich in protein and fiber to start your day right."
            )

            DietTrackerSection()

            Text("Healthy Recipes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            RecipeGrid(context)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SectionWithBackground(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(content, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun RecipeGrid(context: android.content.Context) {
    val items = listOf(
        GridItem("High Protein", R.drawable.high_protein, ""),
        GridItem("Low Carb", R.drawable.low_carb, ""),
        GridItem("Vegetarian", R.drawable.vegetarian, ""),
        GridItem("Smoothies", R.drawable.smoothie, "")
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (row in items.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0))
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                                context.startActivity(intent)
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DietTrackerSection() {
    var selectedFood by remember { mutableStateOf("Select food") }
    val foodOptions = listOf("Chicken Breast", "Brown Rice", "Salad", "Oats", "Smoothie")

    var expanded by remember { mutableStateOf(false) }
    var calories by remember { mutableStateOf(300f) }

    var breakfastChecked by remember { mutableStateOf(false) }
    var lunchChecked by remember { mutableStateOf(false) }
    var dinnerChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Food Intake Log", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedFood,
                onValueChange = {},
                readOnly = true,
                label = { Text("Choose a food") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                foodOptions.forEach { food ->
                    DropdownMenuItem(
                        text = { Text(food) },
                        onClick = {
                            selectedFood = food
                            expanded = false
                        }
                    )
                }
            }
        }

        Text("Calories: ${calories.toInt()} kcal")
        Slider(
            value = calories,
            onValueChange = { calories = it },
            valueRange = 0f..1000f,
            steps = 9
        )

        Divider()

        Text("Today's Meal Checklist", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = breakfastChecked, onCheckedChange = { breakfastChecked = it })
            Text("Breakfast")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = lunchChecked, onCheckedChange = { lunchChecked = it })
            Text("Lunch")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = dinnerChecked, onCheckedChange = { dinnerChecked = it })
            Text("Dinner")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDietScreen() {
    DietScreen()
}
