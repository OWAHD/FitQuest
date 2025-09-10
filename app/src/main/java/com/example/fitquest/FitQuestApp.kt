package com.example.fitquest

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fitquest.ui.navigation.FitnessAppNavHost
import com.example.fitquest.ui.navigation.FitnessAppScaffold

@Composable
fun FitQuestApp(navController: NavHostController = rememberNavController()){
    FitnessAppScaffold(
        navController = navController
        )
}