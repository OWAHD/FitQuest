package com.example.fitquest.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fitquest.FitQuestApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import com.example.fitquest.data.repository.HydrationRepository
import com.example.fitquest.ui.screens.authentication.AuthenticationViewModel
import com.example.fitquest.ui.screens.hydration.HydrationViewModel
import com.example.fitquest.ui.screens.workout.WorkoutTrackingViewModel
import com.example.fitquest.ui.screens.workout.WorkoutViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthenticationViewModel(
                userRepository = fitQuestApplication().container.userRepository,
                userPreferences = fitQuestApplication().container.userPreferences
            )
        }

        initializer{
            WorkoutViewModel(
                workoutRepository = fitQuestApplication().container.workoutRepository,
                exerciseRepository = fitQuestApplication().container.exerciseRepository
            )
        }

        initializer {
            WorkoutTrackingViewModel(
                workoutRepository = fitQuestApplication().container.workoutRepository,
                exerciseRepository = fitQuestApplication().container.exerciseRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            HydrationViewModel(
                hydrationRepository = fitQuestApplication().container.hydrationRepository,
                userPreferencesDataStore = fitQuestApplication().container.userPreferences
            )
        }
    }
}


fun CreationExtras.fitQuestApplication(): FitQuestApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FitQuestApplication)