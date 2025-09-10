package com.example.fitquest

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.fitquest.data.AppContainer
import com.example.fitquest.data.AppDataContainer
import com.example.fitquest.data.preferencesDatastore.UserPreferencesDataStore



class FitQuestApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}