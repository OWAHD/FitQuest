package com.example.fitquest.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import com.example.fitquest.ui.screens.workout.WorkoutScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.fitquest.ui.AppViewModelProvider
import com.example.fitquest.ui.screens.authentication.AuthenticationViewModel
import com.example.fitquest.ui.screens.workout.HomeScreen
import com.example.fitquest.ui.screens.authentication.LoginScreen
import com.example.fitquest.ui.screens.authentication.OnboardingScreen
import com.example.fitquest.ui.screens.authentication.SignUpScreen
import com.example.fitquest.ui.screens.authentication.WelcomeScreen
import com.example.fitquest.ui.screens.hydration.HydrationTrackerScreen
import com.example.fitquest.ui.screens.workout.WorkoutTrackingDestination
import com.example.fitquest.ui.screens.workout.WorkoutTrackingScreen


interface NavigationDestination {
    val route: String
}

sealed class NavDestinations(val route: String) {
    object Welcome : NavDestinations("Welcome")
    object Onboarding : NavDestinations("Onboarding")
    object Login : NavDestinations("Login")
    object SignUp : NavDestinations("SignUp")
    object Home : NavDestinations("Home")
    object Workout : NavDestinations("Workout")
    object Hydration : NavDestinations("Hydration")
    object Profile : NavDestinations("Profile")
}


@Composable
fun FitnessAppNavHost(
    navController: NavHostController,
    modifier: Modifier,
    authViewModel: AuthenticationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if(isAuthenticated){ NavDestinations.Home.route} else { NavDestinations.Welcome.route},
        modifier = modifier
    ) {
        composable( NavDestinations.Welcome.route) {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(NavDestinations.Login.route)
                },
                onSignUpClick = {
                    navController.navigate(NavDestinations.Onboarding.route)
                }
            )
        }

        composable(NavDestinations.Onboarding.route) {
            OnboardingScreen(navController, modifier = modifier)
        }

        composable(NavDestinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavDestinations.Home.route) {
                        popUpTo(NavDestinations.Welcome.route) { inclusive = true } // Optional: prevent back stack
                    }
                },
                onSignUpClick = {
                    navController.navigate(NavDestinations.SignUp.route)
                },
                onBackClicked = {
                    navController.navigate(NavDestinations.Welcome.route)
                }
            )
        }

        composable(NavDestinations.SignUp.route) {
            SignUpScreen(
                onLoginClick = {
                    navController.navigate(NavDestinations.Login.route){
                        popUpTo(NavDestinations.SignUp.route) { inclusive = true }
                    }
                },
                onBackClicked = {
                    navController.navigate(NavDestinations.Welcome.route)
                }
            )
        }


        composable(NavDestinations.Home.route) {
            HomeScreen(
                onLogoutClick = {
                    navController.navigate(NavDestinations.Welcome.route)
                                },
                onViewWorkoutClick = {
                    navController.navigate("${WorkoutTrackingDestination.route}/${it}")
                },
                onWaterTrackerClick = {
                    navController.navigate(NavDestinations.Hydration.route)
                },
                onViewAllWorkoutClick = {
                    navController.navigate(NavDestinations.Workout.route)
                },
            )
        }

        composable(NavDestinations.Workout.route ) {
            WorkoutScreen(
                onBackClicked = {
                    navController.navigate(NavDestinations.Home.route)
                },
                onWorkoutItemClicked = {
                    navController.navigate("${WorkoutTrackingDestination.route}/${it}")
                }
            )
        }

        composable(
            route = WorkoutTrackingDestination.routeWithArgs,
            arguments = listOf(navArgument(WorkoutTrackingDestination.workoutIdArg){ type = NavType.LongType })
        ){ backStackEntry ->

            val arguments = requireNotNull(backStackEntry.arguments)

            WorkoutTrackingScreen(
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestinations.Hydration.route) {
            HydrationTrackerScreen()
        }

        composable(NavDestinations.Profile.route) {
            /* TODO: Create ProfileScreen */
        }

    }

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated && navController.currentDestination?.route != NavDestinations.Login.route) {
            // If user is no longer authenticated and not on login screen, navigate to login
            navController.navigate(NavDestinations.Welcome.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
        // set background color
        containerColor = MaterialTheme.colorScheme.primary
    ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route

        // Bottom nav items we declared
        Constants.BottomNavItems.forEach { navItem ->

            // Place the bottom nav items
            NavigationBarItem(

                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,

                // navigate on click
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                },

                // Icon of navItem
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },

                // label
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = true,

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, // Icon color when selected
                    unselectedIconColor = Color.White, // Icon color when not selected
                    selectedTextColor = Color.White, // Label color when selected
                    unselectedTextColor = Color.White, // Label color when not selected
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer // Highlight color for selected item
                )
            )
        }
    }
}