@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitsphere.ui.profile

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fitsphere.R
import com.example.fitsphere.ui.auth.AuthViewModel
import com.example.fitsphere.ui.auth.AuthViewModelFactory
import com.example.fitsphere.ui.auth.UserProfile

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val loading by authViewModel.loading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    var name by remember { mutableStateOf(userProfile.name) }
    var weight by remember { mutableStateOf(userProfile.weight) }
    var goal by remember { mutableStateOf(userProfile.goal) }
    var gender by remember { mutableStateOf(userProfile.gender) }
    var age by remember { mutableStateOf(userProfile.age) }
    var imageUri by remember { mutableStateOf<Uri?>(if (userProfile.imageUri != null) Uri.parse(userProfile.imageUri) else null) }

    val genderOptions = listOf("Male", "Female", "Other")
    val ageOptions = (12..100).map { it.toString() }

    var genderExpanded by remember { mutableStateOf(false) }
    var ageExpanded by remember { mutableStateOf(false) }

    // Image Picker
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    // Update local state when userProfile changes
    LaunchedEffect(userProfile) {
        name = userProfile.name
        weight = userProfile.weight
        goal = userProfile.goal
        gender = userProfile.gender
        age = userProfile.age
        imageUri = if (userProfile.imageUri != null) Uri.parse(userProfile.imageUri) else null
    }

    Scaffold { padding ->
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

            Button(
                onClick = {
                    authViewModel.updateProfile(
                        name = name,
                        weight = weight,
                        goal = goal,
                        gender = gender,
                        age = age,
                        imageUri = imageUri,
                        onSuccess = { /* Handle success if needed */ }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text("Save")
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}