package com.example.fitquest.ui.screens.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.R
import com.example.fitquest.data.room.models.User
import com.example.fitquest.ui.AppViewModelProvider
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    onBackClicked: () -> Unit,
    viewModel: AuthenticationViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {

    val signUpSuccess by viewModel.signUpSuccess.collectAsState()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var stepGoal by remember { mutableFloatStateOf(2000f) }
    var waterGoal by remember { mutableFloatStateOf(5f) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()


    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) {
            onLoginClick()
        }
    }

    Column( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.back_button),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable(onClick = onBackClicked)
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create Account",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                singleLine = true,
            )
//        Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth() ,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                singleLine = true,
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                singleLine = true,
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                singleLine = true,
            )

            Text("Set Your Fitness Goals", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                singleLine = true,
            )
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done),
                singleLine = true,
            )
            Text("Daily Step Goal: ${stepGoal.toInt()} steps")
            Slider(
                value = stepGoal,
                onValueChange = { stepGoal = it },
                valueRange = 2000f..10000f,
                steps = 8
            )
            Text("Daily Water Goal: ${String.format("%.2f", waterGoal)} L")
            Slider(
                value = waterGoal,
                onValueChange = { waterGoal = it },
                valueRange = 5f..10f,
                steps = 10
            )
//        Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        if ((password.isNotEmpty() && confirmPassword.isNotEmpty()) && (password == confirmPassword)) {

                            val currentWeight = weight.toIntOrNull() ?: 0
                            val currentHeight = height.toIntOrNull() ?: 0

                            val user = User(
                                userId = 0,
                                name = name,
                                username = username,
                                password = password,
                                weight = currentWeight,
                                height = currentHeight,
                                waterGoal = waterGoal.toInt(),
                                stepGoal = stepGoal.toInt()
                            )
                            try {
                                if (name.isNotEmpty() || username.isNotEmpty() || weight.isNotEmpty() || height.isNotEmpty()) {
                                    viewModel.signUp(user)
                                    if (signUpSuccess){
                                        // Show success message
                                        Toast.makeText(context, "Sign up successful! Proceed to Login.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Input fields should not be empty", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                // Handle potential errors during sign up (e.g., network issues, username taken)
                                Toast.makeText(context, "Sign up failed: $e", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            // Passwords do not match, show an error message
                            Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            TextButton(onClick = onLoginClick) {
                Text("Already have an account? Log in")
            }
        }
    }
}
