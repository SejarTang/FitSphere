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
import com.google.firebase.auth.GoogleAuthProvider


// Data class representing user profile data
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

    // Loading state flow
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Error message state flow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Current user's email state flow
    private val _currentUserEmail = MutableStateFlow(auth.currentUser?.email)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail

    // User profile state flow
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }
    init {
        // Fetch user profile on ViewModel initialization
        fetchUserProfile()
    }

    // Method to check if a user is logged in
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Login using Google ID token
    fun loginWithGoogle(idToken: String, onSuccess: () -> Unit) {
        _loading.value = true
        _errorMessage.value = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = auth.signInWithCredential(credential).await()
                val user = result.user

                if (user != null) {
                    val userMap = mapOf(
                        "name" to (user.displayName ?: ""),
                        "email" to (user.email ?: ""),
                        "weight" to "70",
                        "goal" to "Build Muscle",
                        "gender" to "Male",
                        "age" to "35",
                        "imageUri" to (user.photoUrl?.toString() ?: "")
                    )
                    firestore.collection("Users").document(user.email!!).set(userMap).await()
                }

                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _currentUserEmail.value = user?.email
                    fetchUserProfile()
                    Toast.makeText(getApplication(), "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Google Sign-In Failed"
                    Toast.makeText(getApplication(), "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fetch user profile from Firebase Auth and Firestore
    fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val googleUser = auth.currentUser
                if (googleUser != null) {
                    withContext(Dispatchers.Main) {
                        _userProfile.value = UserProfile(
                            name = googleUser.displayName.orEmpty(),
                            email = googleUser.email.orEmpty(),
                            imageUri = googleUser.photoUrl?.toString()
                        )
                        _currentUserEmail.value = googleUser.email
                    }
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
                    Toast.makeText(getApplication(), "Failed to fetch profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Update user profile and save to Firestore
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
                    auth.currentUser?.email ?: throw Exception("No user email")
                } else {
                    "currentUser"
                }

                val newUserData = mapOf(
                    "name" to name,
                    "email" to (auth.currentUser?.email ?: ""),
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
                        email = auth.currentUser?.email ?: "",
                        weight = weight,
                        goal = goal,
                        gender = gender,
                        age = age,
                        imageUri = imageUri?.toString()
                    )
                    Toast.makeText(getApplication(), "Save Successful", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Save Error"
                    Toast.makeText(getApplication(), "Save Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Register new user with email and password
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
                    Toast.makeText(getApplication(), "Registration Successful", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Registration Failed"
                    Toast.makeText(getApplication(), "Registration Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Login existing user with email and password
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        _loading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _currentUserEmail.value = authResult.user?.email
                    fetchUserProfile()
                    Toast.makeText(getApplication(), "Login Successful", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _errorMessage.value = e.message ?: "Login Failed"
                    Toast.makeText(getApplication(), "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

// ViewModel Factory for AuthViewModel
class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
