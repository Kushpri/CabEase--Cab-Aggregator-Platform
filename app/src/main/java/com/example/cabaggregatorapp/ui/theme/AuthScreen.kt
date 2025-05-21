package com.example.cabaggregatorapp.ui.theme


import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.cabaggregatorapp.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector

import com.google.firebase.FirebaseApp
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape



import com.google.android.gms.common.api.ApiException
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.VisualTransformation
import com.example.cabaggregatorapp.utils.UserPreferences
import com.google.firebase.auth.userProfileChangeRequest


@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isRegistering by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // App Title / Logo
        Text(
            text = "CabEase",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = if (isRegistering) "Create an Account" else "Welcome Back",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (isRegistering) {
            CustomInputField(value = firstName, label = "First Name", onValueChange = { firstName = it })
            Spacer(modifier = Modifier.height(12.dp))
            CustomInputField(value = lastName, label = "Last Name", onValueChange = { lastName = it })
            Spacer(modifier = Modifier.height(12.dp))
        }

        CustomInputField(value = email, label = "Email", onValueChange = { email = it }, leadingIcon = Icons.Default.Email)
        Spacer(modifier = Modifier.height(12.dp))

        CustomInputField(
            value = password,
            label = "Password",
            onValueChange = { password = it },
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            passwordVisible = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isRegistering) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val fullName = "$firstName $lastName"
                                val profileUpdates = userProfileChangeRequest { displayName = fullName }
                                user?.updateProfile(profileUpdates)
                                Toast.makeText(context, "Registration Successful!", Toast.LENGTH_LONG).show()
                                onAuthSuccess()
                            } else {
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show()
                                onAuthSuccess()
                            } else {
                                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = if (isRegistering) "Register" else "Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = { isRegistering = !isRegistering }) {
            Text(
                text = if (isRegistering) "Already have an account? Login" else "New user? Register here",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isRegistering) {
            GoogleSignInButton(context = context, onAuthSuccess = onAuthSuccess)
        }
    }
}

@Composable
fun CustomInputField (
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onToggleVisibility: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onToggleVisibility?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray
        )
    )
}


@Composable
fun GoogleSignInButton(context: Context, onAuthSuccess: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("943018489255-ro47uc5lssbakudpmjn8ar41sjfdfe3s.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onAuthSuccess()
                } else {
                    Log.e("GoogleSignIn", "Authentication Failed: ${task.exception?.localizedMessage}")
                    Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google Sign-In Failed", e)
            Toast.makeText(context, "Google Sign-In Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }


    Button(
        onClick = { launcher.launch(googleSignInClient.signInIntent) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Login with Google")
    }
}
