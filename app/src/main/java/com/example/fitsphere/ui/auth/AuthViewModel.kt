package com.example.fitsphere.ui.auth

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val weight: String = "70",
    val goal: String = "Build Muscle",
    val gender: String = "Male",
    val age: String = "35",
    val imageUri: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _currentUserEmail = MutableStateFlow(auth.currentUser?.email)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    init {
        // Fetch user profile when ViewModel is initialized
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val googleUser = auth.currentUser
                if (googleUser != null) {
                    // Google Sign-In or authenticated user
                    withContext(Dispatchers.Main) {
                        _userProfile.value = UserProfile(
                            name = googleUser.displayName.orEmpty(),
                            email = googleUser.email.orEmpty(),
                            imageUri = googleUser.photoUrl?.toString()
                        )
                        _currentUserEmail.value = googleUser.email
                    }
                    // Fetch additional data from Firestore for authenticated user
                    val document = firestore.collection("Users").document(googleUser.email.orEmpty()).get().await()
                    withContext(Dispatchers.Main) {
                        if (document.exists()) {
                            _userProfile.value = UserProfile(
                                name = document.getString("name") ?: _userProfile.value.name,
                                email = document.getString("email") ?: _userProfile.value.email,
                                weight = document.getString("weight") ?: "70",
                                goal = document.getString("goal") ?: "Build Muscle",
                                gender = document.getString("gender") ?: "Male",
                                age = document.getString("age") ?: "35",
                                imageUri = document.getString("imageUri")
                            )
                        }
                    }
                } else {
                    // No authenticated user, fetch from 'currentUser' document
                    val document = firestore.collection("Users").document("currentUser").get().await()
                    withContext(Dispatchers.Main) {
                        if (document.exists()) {
                            _userProfile.value = UserProfile(
                                name = document.getString("name").orEmpty(),
                                email = document.getString("email").orEmpty(),
                                weight = document.getString("weight") ?: "70",
                                goal = document.getString("goal") ?: "Build Muscle",
                                gender = document.getString("gender") ?: "Male",
                                age = document.getString("age") ?: "35",
                                imageUri = document.getString("imageUri")
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message ?: "Failed to fetch profile"
                    Toast.makeText(
                        getApplication(),
                        "Failed to fetch profile: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun updateProfile(
        name: String,
        weight: String,
        goal: String,
        gender: String,
        age: String,
        imageUri: Uri?,
        onSuccess: () -> Unit
    ) {
        _loading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val documentId = if (auth.currentUser != null) {
                    // Use email for authenticated users
                    auth.currentUser?.email ?: throw Exception("No user email")
                } else {
                    // Use 'currentUser' for unauthenticated users
                    "currentUser"
                }

                val newUserData = mapOf(
                    "name" to name,
                    "email" to (if (auth.currentUser != null) auth.currentUser?.email.orEmpty() else ""),
                    "weight" to weight,
                    "goal" to goal,
                    "gender" to gender,
                    "age" to age,
                    "imageUri" to imageUri?.toString()
                )

                firestore.collection("Users").document(documentId).set(newUserData).await()
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _userProfile.value = UserProfile(
                        name = name,
                        email = if (auth.currentUser != null) auth.currentUser?.email.orEmpty() else "",
                        weight = weight,
                        goal = goal,
                        gender = gender,
                        age = age,
                        imageUri = imageUri?.toString()
                    )
                    Toast.makeText(
                        getApplication(),
                        "Save Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Save Error"
                    Toast.makeText(
                        getApplication(),
                        "Save Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun signUp(name: String, email: String, password: String, onSuccess: () -> Unit) {
        _loading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.email ?: throw Exception("User creation failed")
                val user = mapOf(
                    "name" to name,
                    "email" to email,
                    "weight" to "70",
                    "goal" to "Build Muscle",
                    "gender" to "Male",
                    "age" to "35"
                )
                firestore.collection("Users").document(userId).set(user).await()
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _currentUserEmail.value = email
                    _userProfile.value = UserProfile(
                        name = name,
                        email = email,
                        weight = "70",
                        goal = "Build Muscle",
                        gender = "Male",
                        age = "35"
                    )
                    Toast.makeText(
                        getApplication(),
                        "Registration Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Registration Failed"
                    Toast.makeText(
                        getApplication(),
                        "Registration Failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        _loading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _currentUserEmail.value = authResult.user?.email
                    fetchUserProfile() // Fetch profile data after login
                    Toast.makeText(
                        getApplication(),
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Login Failed"
                    Toast.makeText(
                        getApplication(),
                        "Login Failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}