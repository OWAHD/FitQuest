package com.example.fitquest.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavItem(
    // Route to the specific screen
    val route: String,
    // Icon
    val icon: ImageVector,
    // Text below icon
    val label:String,
)



object Constants {
    val BottomNavItems = listOf(
        // Home Screen
        BottomNavItem(
            route = NavDestinations.Home.route,
            icon = Icons.Default.Home,
            label = "Home"
        ),

        // Workout Screen
        BottomNavItem(
            route = NavDestinations.Workout.route,
            icon = Icons.Default.Menu,
            label = "Workout"
        ),

        // Hydration Screen
        BottomNavItem(
            route = NavDestinations.Hydration.route,
            icon = Icons.Default.Favorite,
            label = "Hydration"
        ),

        // Profile Screen
        BottomNavItem(
            route = NavDestinations.Profile.route,
            icon = Icons.Default.Person,
            label = "Profile"
        )

    )
}


