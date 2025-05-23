@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun LoginSignUpScreen(
    onGoogleSignInClick: () -> Unit = {} // Pass your Google sign-in logic
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // use id token to do google login
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("620727711916-sng865hss8vm3vctdlouh89b5oo0n6qi.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    //  integrate firebase with google
    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Firebase.auth.signInWithCredential(credential)
        } catch (e: Exception) {
            Log.w("GoogleSignIn", "Google sign-in failed", e)
        }
    }
    println(Firebase.auth.currentUser?.email)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            if (isLogin) "Welcome to FitSphere!" else "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // after user click, trigger here to get user related data from firestore
                coroutineScope.launch(Dispatchers.IO) {
                    val userSnapshot = firestore.collection("Users").document(email).get().await()
                    withContext(Dispatchers.Main) { // Switch to main thread for UI operations
                        if (isLogin) {
                            if (userSnapshot.exists() && userSnapshot.getString("password") == password) {
                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Email or Password is wrong", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            if (userSnapshot.exists()) {
                                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                firestore.collection("Users").document(email).set(
                                    hashMapOf(
                                        "email" to email,
                                        "password" to password
                                    )
                                ).addOnSuccessListener {
                                    coroutineScope.launch(Dispatchers.Main) { // Switch to main thread for success Toast
                                        Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener {
                                    coroutineScope.launch(Dispatchers.Main) { // Switch to main thread for failure Toast
                                        Toast.makeText(context, "Failed to create account", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "Log In" else "Sign Up")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ Google Sign-In Button using Image
        OutlinedButton(
            onClick = {
//                onGoogleSignInClick()
                val signInIntent = googleSignInClient.signInIntent
                googleLauncher.launch(signInIntent)

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Sign-In",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            if (isLogin) "Don't have an account? Sign up"
            else "Already have an account? Log in",
            modifier = Modifier.clickable { isLogin = !isLogin },
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginSignUpScreen() {
    LoginSignUpScreen()
}
