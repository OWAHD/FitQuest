package com.example.fitquest.ui.screens.hydration

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitquest.data.preferencesDatastore.UserPreferencesDataStore
import com.example.fitquest.data.repository.HydrationRepository
import com.example.fitquest.ui.screens.authentication.UserUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HydrationViewModel(
    private val hydrationRepository: HydrationRepository,
    userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    val loggedInUserDetails: StateFlow<UserUiState> =
        userPreferencesDataStore.loggedInUserFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserUiState(0,"", 0, 0, 0, 0 ) // Sensible initial value
        )


    private val userId: Int = loggedInUserDetails.value.userId
    val dailyGoal: Int = loggedInUserDetails.value.waterGoal // e.g. 2000 ml

    private val _todayTotal = MutableStateFlow(0)
    val todayTotal: StateFlow<Int> = _todayTotal

    init {
        refreshTodayTotal()
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            hydrationRepository.addWater(userId, amountMl)
            refreshTodayTotal()
        }
    }

    private fun refreshTodayTotal() {
        viewModelScope.launch {
            _todayTotal.value = hydrationRepository.getTodayTotal(userId)
        }
    }


    fun progress(): Float {
        return (todayTotal.value.toFloat() / dailyGoal).coerceIn(0f, 1f)
    }
}
