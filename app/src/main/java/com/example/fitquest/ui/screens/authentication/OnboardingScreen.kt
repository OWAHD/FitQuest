package com.example.fitquest.ui.screens.authentication

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitquest.R
import com.example.fitquest.ui.navigation.NavDestinations

data class OnboardingPage(val imageRes: Int, val title: String, val description: String)

val onboardingPages = listOf(
    OnboardingPage(R.drawable.onboarding_background, "Track Workouts", "Log your sets, reps, and track progress."),
    OnboardingPage(R.drawable.onboarding_background, "Log Water", "Stay hydrated with easy logging and reminders."),
    OnboardingPage(R.drawable.onboarding_background, "Set Goals", "Customize your goals and stay motivated.")
)


@Composable
fun OnboardingScreen(navController: NavController,modifier: Modifier) {
    var pageIndex by remember { mutableStateOf(0) }
    val lastPage = pageIndex == onboardingPages.lastIndex
    val page = onboardingPages[pageIndex]

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
        Column {
            Text(text = page.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = page.description, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pageIndex > 0) {
                    TextButton(onClick = { pageIndex-- }) {
                        Text("Back")
                    }
                }
                Button(
                    onClick = {
                        if (lastPage) {
                            navController.navigate(NavDestinations.SignUp.route) {
                                popUpTo(NavDestinations.Onboarding.route) { inclusive = true }
                            }
                        } else {
                            pageIndex++
                        }
                    }
                ) {
                    Text(if (lastPage) "Finish" else "Next")
                }
            }
        }
    }
}
