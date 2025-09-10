package com.example.fitquest.ui.screens.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitquest.data.preferencesDatastore.UserPreferencesDataStore
import com.example.fitquest.data.repository.UserRepository
import com.example.fitquest.data.room.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferencesDataStore
) : ViewModel(){

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess

    private val _authState = MutableStateFlow(false)
    val authState: StateFlow<Boolean> = _authState

    init {
        Log.d("ViewModelCheck", "AuthenticationViewModel instance: ${this.hashCode()}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelLifecycle", "AuthenticationViewModel: OnCLEARED - Instance ${this.hashCode()}")
    }

    fun signUp(user: User){
        try {
            if (user.name.isNotBlank() ||user.username.isNotEmpty() || user.password.isNotEmpty()) {
                viewModelScope.launch{
                    val userId = userRepository.register(user)
                    Log.d("USERID", "$userId")
                    Log.d("SIGNUP", "You have Registered your account.")
                    _signUpSuccess.value = true
                }
            }
        } catch (e: Exception) {
            Log.d("$e", "${e.message}")
        }
    }


    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.login(username, password)
            if (user != null) {
                val newUiState = user.toUiState()
                Log.d("ViewModelLogin(UserEntity from repo)", "UserEntity from repo: Name='${user.name}', Weight='${user.weight}'")
                Log.d("ViewModelLogin(UI State)", "UI State: Name='${newUiState.name}', Weight='${newUiState.weight}'")
                userPreferences.saveLoginDetails(
                    userName = newUiState.name,
                    userWeight = newUiState.weight,
                    userHeight = newUiState.height,
                    userWaterGoal = newUiState.waterGoal,
                    userStepsGoal = newUiState.stepsGoal,
                    userId = newUiState.userId
                )
                _authState.value = true
            } else {
                _authState.value = false
                Log.d("USER DETAILS:", "User details is empty")
            }
        }
    }

    val loggedInUserDetails: StateFlow<UserUiState> =
        userPreferences.loggedInUserFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserUiState(0,"", 0, 0, 0, 0 ) // Sensible initial value
        )

    val isAuthenticated: StateFlow<Boolean> =
        userPreferences.isLoggedIn.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly, // Or Lazily/While Subscribed
            initialValue = false // Assuming user is not logged in initially
        )

    fun logout() {
        viewModelScope.launch {
            // Clear from DataStore
            userPreferences.clearLoginDetails()
            // Any other logout tasks (e.g., clear in-memory user data, call backend logout)
        }
    }


    fun User.toUiState(): UserUiState{
        return UserUiState(
            name = username,
            userId = userId.toInt(),
            weight = weight,
            height = height,
            waterGoal = waterGoal,
            stepsGoal = stepGoal
        )
    }

}


data class UserUiState(
    val userId: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val waterGoal: Int,
    val stepsGoal: Int,
    val isLoggedIn: Boolean = false
)