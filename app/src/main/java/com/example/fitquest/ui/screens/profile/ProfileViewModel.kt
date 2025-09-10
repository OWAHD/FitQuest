//package com.example.fitquest.ui.screens.profile
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.fitquest.data.preferencesDatastore.UserPreferencesDataStore
//import com.example.fitquest.data.repository.UserRepository
//import com.example.fitquest.data.repository.WorkoutRepository
//import com.example.fitquest.data.room.models.WorkoutHistory
//import com.example.fitquest.ui.screens.authentication.UserUiState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//
//class ProfileViewModel(
//    private val userPreferences: UserPreferencesDataStore,
//    private val workoutRepository: WorkoutRepository
//) : ViewModel() {
//
//
//    val loggedInUserDetails: StateFlow<UserUiState> =
//        userPreferences.loggedInUserFlow.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = UserUiState(0,"", 0, 0, 0, 0 ) // Sensible initial value
//        )
//
//    val history: StateFlow<List<WorkoutHistory>> =
//        workoutRepository.getWorkoutHistory().stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )
//
//    private val _profileUiState = MutableStateFlow(ProfileUiState())
//    val profileUiState: StateFlow<ProfileUiState> = _profileUiState
//
//    init {
//        loadProfile()
//    }
//
//    private fun loadProfile() {
//        viewModelScope.launch {
//
//            val totalCalories = history.sumOf { it.totalCalories }
//            val avgDuration = if (history.isNotEmpty()) history.map { it.totalDuration }.average().toInt() else 0
//
//            _profileUiState.value = ProfileUiState(
//                name = user.name,
//                height = user.height,
//                weight = user.weight,
//                waterGoal = user.waterGoal,
//                stepGoal = user.stepGoal,
//                totalWorkouts = history.size,
//                totalCalories = totalCalories,
//                avgWorkoutDuration = avgDuration,
//                bestStreak = 5, // TODO: compute properly
//                recentWorkouts = history.takeLast(3).map {
//                    WorkoutSummary(
//                        date = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(it.date)),
//                        duration = it.totalDuration / 60,
//                        calories = it.totalCalories
//                    )
//                }
//            )
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//data class ProfileUiState(
//    val name: String = "Guest",
//    val height: Int = 0,
//    val weight: Int = 0,
//    val waterGoal: Int = 2000,
//    val stepGoal: Int = 10000,
//    val totalWorkouts: Int = 0,
//    val totalCalories: Int = 0,
//    val avgWorkoutDuration: Int = 0,
//    val bestStreak: Int = 0,
//    val recentWorkouts: List<WorkoutSummary> = emptyList()
//)
//
//data class WorkoutSummary(
//    val date: String,
//    val duration: Int,
//    val calories: Int
//)
