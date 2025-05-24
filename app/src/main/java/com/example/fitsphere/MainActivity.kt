package com.example.fitsphere

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitsphere.ui.auth.AuthViewModel
import com.example.fitsphere.ui.auth.LoginScreen
import com.example.fitsphere.ui.auth.SignUpScreen
import com.example.fitsphere.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.example.fitsphere.R

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    private val authViewModel: AuthViewModel by lazy {
        AuthViewModel(application)
    }

    // Register Google Sign-In activity launcher
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google SignIn options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            // State to switch between Login and SignUp screens
            var showLogin by remember { mutableStateOf(true) }

            if (showLogin) {
                // Show LoginScreen
                LoginScreen(
                    onGoogleLoginClicked = { signInWithGoogle() },
                    onLoginSuccess = {
                        // Navigate to HomeActivity after successful login
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish() // Close this activity
                    },
                    onGoToSignUp = {
                        // Switch to SignUpScreen
                        showLogin = false
                    },
                    viewModel = authViewModel
                )
            } else {
                // Show SignUpScreen
                SignUpScreen(
                    onSignUpSuccess = {
                        // After successful sign-up, switch back to LoginScreen
                        showLogin = true
                    },
                    onGoToLogin = {
                        // User clicked "Already have an account? Log in"
                        showLogin = true
                    },
                    viewModel = authViewModel
                )
            }
        }
    }

    // Launch Google SignIn intent
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    // Handle Google SignIn result
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            val idToken = account.idToken
            if (idToken != null) {
                // Call ViewModel to perform Google login with token
                authViewModel.loginWithGoogle(idToken) {
                    // On successful login callback (optional)
                    // For example: close login screen or navigate elsewhere
                }
            } else {
                // Handle missing idToken error
            }
        } catch (e: ApiException) {
            // Handle sign-in failure (log or notify user)
            e.printStackTrace()
        }
    }
}
