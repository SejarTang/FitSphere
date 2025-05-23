@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import androidx.compose.ui.graphics.Color

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(email: String) {
    val context = LocalContext.current
    val googleUser = Firebase.auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("70") }
    var goal by remember { mutableStateOf("Build Muscle") }
    val genderOptions = listOf("Male", "Female", "Other")
    val ageOptions = (12..100).map { it.toString() }

    var genderExpanded by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("Male") }

    var ageExpanded by remember { mutableStateOf(false) }
    var age by remember { mutableStateOf("35") }

    // Image Picker
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var currentEmail by remember { mutableStateOf(email) }
    var password: String = "default"
    LaunchedEffect(currentEmail) {
        // according to current user type, fetch data from different database
        if (googleUser != null) {
            name = googleUser.displayName.orEmpty()
            currentEmail = googleUser.email.orEmpty()
            imageUri = googleUser.photoUrl
        } else {
            val targetUser = firestore.collection("Users").document(currentEmail).get().await()
            name = targetUser.getString("name").orEmpty()
            weight = targetUser.getString("weight") ?: "70"
            goal = targetUser.getString("goal") ?: "Build Muscle"
            gender = targetUser.getString("gender") ?: "Male"
            imageUri = Uri.parse(targetUser.getString("imageUri").orEmpty())
            password = targetUser.getString("password").orEmpty()
        }
    }
    var imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.DirectionsWalk, contentDescription = "Workout", tint = Color.White) },
                    label = { Text("Workout", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Coffee, contentDescription = "Diet", tint = Color.White) },
                    label = { Text("Diet", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* current */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White) },
                    label = { Text("Profile", color = Color.White) },
                    alwaysShowLabel = true
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            // Avatar Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { imagePicker.launch("image/*") }
            ) {
                Image(
                    painter = if (imageUri != null)
                        rememberAsyncImagePainter(imageUri)
                    else
                        painterResource(id = R.drawable.placeholder),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Gender dropdown
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    label = { Text("Gender") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                gender = it
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            // Age dropdown
            ExposedDropdownMenuBox(
                expanded = ageExpanded,
                onExpandedChange = { ageExpanded = !ageExpanded }
            ) {
                OutlinedTextField(
                    value = age,
                    onValueChange = {},
                    label = { Text("Age") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = ageExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = ageExpanded,
                    onDismissRequest = { ageExpanded = false }
                ) {
                    ageOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                age = it
                                ageExpanded = false
                            }
                        )
                    }
                }
            }

            // Weight
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Goal
            OutlinedTextField(
                value = goal,
                onValueChange = { goal = it },
                label = { Text("Fitness Goal") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        // update data to firestore
                        val newUserData = mapOf(
                            "email" to currentEmail,
                            "name" to name,
                            "weight" to weight,
                            "gender" to gender,
                            "imageUri" to imageUri,
                            "age" to age,
                            "password" to password
                        )
                        try {
                            firestore.collection("Users").document(currentEmail).set(newUserData).await()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Save Successful", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Save Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProfileScreen() {
    val email = "test@gmail.com"
    ProfileScreen(email)
}
