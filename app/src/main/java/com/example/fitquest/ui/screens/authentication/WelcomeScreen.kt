package com.example.fitquest.ui.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun WelcomeScreen(
    onLoginClick: ()-> Unit,
    onSignUpClick: () -> Unit
){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome to FitQuest",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(15.dp)
        )
        Text(text = "Conquer your Health",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = Bold,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 40.dp)
        )
        Button(
            onClick = onLoginClick,
            modifier = Modifier.width(200.dp)
        ) {
            Text("Log In")
        }
        Button(
            onClick = onSignUpClick,
            modifier = Modifier.width(200.dp)
        ) {
            Text("New User? Sign Up")
        }
    }
}