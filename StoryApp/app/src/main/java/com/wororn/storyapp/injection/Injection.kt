package com.wororn.storyapp.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.wororn.storyapp.api.ApiConfig
import com.wororn.storyapp.componen.repository.StoriesRepository
import com.wororn.storyapp.componen.repository.UsersRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object StoriesInjection {
    fun provideRepository(): StoriesRepository {
        val apiService = ApiConfig.getApiService()
        return StoriesRepository.getInstance(apiService)
    }
}

object UsersInjection {
    fun providePreferences(context: Context): UsersRepository {
        val apiService = ApiConfig.getApiService()
        return UsersRepository.getInstance(context.dataStore, apiService)
    }
}
