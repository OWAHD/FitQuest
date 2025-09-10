package com.example.fitquest.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun FitnessAppScaffold(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = when (currentRoute) {
        NavDestinations.Home.route,
        NavDestinations.Workout.route,
        NavDestinations.Hydration.route,
        NavDestinations.Profile.route -> true
        else -> false // Hides for Splash, Welcome, Login, Signup
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        FitnessAppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding) // make sure content isn't hidden by bottom bar
        )
    }
}
