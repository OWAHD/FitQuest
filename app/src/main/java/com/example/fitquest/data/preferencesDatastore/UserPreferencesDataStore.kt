package com.example.fitquest.data.preferencesDatastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.fitquest.ui.screens.authentication.UserUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore(private val dataStore: DataStore<Preferences>) {
    private companion object Keys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("username")
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
        val USER_WEIGHT = intPreferencesKey("userWeight")
        val USER_HEIGHT = intPreferencesKey("userHeight")
        val USER_WATER_GOAL = intPreferencesKey("userWaterGoal")
        val USER_STEPS_GOAL = intPreferencesKey("userStepsGoal")
    }

    val loggedInUserFlow: Flow<UserUiState> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isLoggedIn = preferences[IS_LOGGED_IN] ?: false
            val userId = preferences[USER_ID]
            val userName = preferences[USER_NAME]
            val userWeight = preferences[USER_WEIGHT]
            val userHeight = preferences[USER_HEIGHT]
            val userWaterGoal = preferences[USER_WATER_GOAL]
            val userStepsGoal = preferences[USER_STEPS_GOAL]
            UserUiState(userId?: 0, userName.toString(), userWeight?: 0, userHeight?: 0, userWaterGoal?: 0, userStepsGoal?: 0 ,isLoggedIn)
        }


    suspend fun saveLoginDetails(userId : Int, userName: String, userWeight: Int, userHeight: Int, userWaterGoal: Int, userStepsGoal: Int) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesDataStore.IS_LOGGED_IN] = true
            preferences[UserPreferencesDataStore.USER_ID] = userId
            preferences[UserPreferencesDataStore.USER_NAME] = userName
            preferences[UserPreferencesDataStore.USER_WEIGHT] = userWeight
            preferences[UserPreferencesDataStore.USER_HEIGHT] = userHeight
            preferences[UserPreferencesDataStore.USER_WATER_GOAL] = userWaterGoal
            preferences[UserPreferencesDataStore.USER_STEPS_GOAL] = userStepsGoal
            // Save other details
        }
    }

    suspend fun clearLoginDetails() {
        dataStore.edit { preferences ->
            preferences[UserPreferencesDataStore.IS_LOGGED_IN] = false
            preferences.remove(UserPreferencesDataStore.USER_ID)
            preferences.remove(UserPreferencesDataStore.USER_WEIGHT) // Or set to a default null/0 value
            preferences.remove(UserPreferencesDataStore.USER_NAME)
            preferences.remove(UserPreferencesDataStore.USER_HEIGHT)
            preferences.remove(UserPreferencesDataStore.USER_WATER_GOAL)
            preferences.remove(UserPreferencesDataStore.USER_STEPS_GOAL)
            // Clear other details
            // Alternatively, you can use preferences.clear() to remove all keys
            // but be cautious if this DataStore instance is used for other preferences too.
        }
    }

    // You can also add individual getter functions if needed,
    // though observing loggedInUserFlow is often more robust.
    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[UserPreferencesDataStore.IS_LOGGED_IN] ?: false
        }


}