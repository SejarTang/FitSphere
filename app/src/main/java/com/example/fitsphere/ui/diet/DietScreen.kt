@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitsphere.ui.diet

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitsphere.R
import com.example.fitsphere.data.repository.DietRepository
import com.example.fitsphere.di.DatabaseProvider
import java.text.SimpleDateFormat
import java.util.*

data class GridItem(val title: String, val imageRes: Int, val link: String)

@Composable
fun DietScreen() {
    val context = LocalContext.current
    val dietDao = remember { DatabaseProvider.getDietDao() }
    val repository = remember { DietRepository(dietDao) }
    val viewModel = remember { DietViewModel(repository) }

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diet & Nutrition", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
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

            DietTrackerSection(viewModel)

            Text("Healthy Recipes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            RecipeGrid(context)

            if (viewModel.dietHistory.isNotEmpty()) {
                Text("Saved Records", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                SavedRecordsList(viewModel)
            }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
fun DietTrackerSection(viewModel: DietViewModel) {
    val selectedFood = viewModel.selectedFood
    val calories = viewModel.calories
    val breakfast = viewModel.breakfast
    val lunch = viewModel.lunch
    val dinner = viewModel.dinner
    val context = LocalContext.current

    val foodOptions = listOf("Chicken Breast", "Brown Rice", "Salad", "Oats", "Smoothie")
    var expanded by remember { mutableStateOf(false) }

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
                value = selectedFood.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Choose a food") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                foodOptions.forEach { food ->
                    DropdownMenuItem(
                        text = { Text(food) },
                        onClick = {
                            selectedFood.value = food
                            expanded = false
                        }
                    )
                }
            }
        }

        Text("Calories: ${calories.value.toInt()} kcal")
        Slider(value = calories.value, onValueChange = { calories.value = it }, valueRange = 0f..1000f, steps = 9)

        Divider()

        Text("Today's Meal Checklist", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = breakfast.value, onCheckedChange = { breakfast.value = it })
            Text("Breakfast")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = lunch.value, onCheckedChange = { lunch.value = it })
            Text("Lunch")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = dinner.value, onCheckedChange = { dinner.value = it })
            Text("Dinner")
        }

        Button(onClick = {
            viewModel.saveDiet()
            Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.align(Alignment.End)) {
            Text("Save")
        }
    }
}

@Composable
fun SavedRecordsList(viewModel: DietViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        viewModel.dietHistory.forEach { record ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("${record.foodName} - ${record.calories} kcal", fontWeight = FontWeight.SemiBold, color = Color.Black)

                        val formattedTime = remember(record.timestamp) {
                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(record.timestamp))
                        }

                        Text(formattedTime, fontSize = 12.sp, color = Color.Gray)

                        val tags = listOfNotNull(
                            if (record.isBreakfast) "Breakfast" else null,
                            if (record.isLunch) "Lunch" else null,
                            if (record.isDinner) "Dinner" else null
                        )
                        if (tags.isNotEmpty()) {
                            Text("Meal: ${tags.joinToString()}", fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }

                    IconButton(onClick = { viewModel.deleteDiet(record) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
