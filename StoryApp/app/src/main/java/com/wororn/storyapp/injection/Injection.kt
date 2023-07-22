package com.wororn.storyapp.injection

import android.content.Context
import android.media.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.wororn.storyapp.api.ApiConfig
import com.wororn.storyapp.api.ApiService
import com.wororn.storyapp.componen.database.StoryDatabase
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

object ApiServiceInjection {
    fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }
}

fun provideRepository(context: Context): StoriesRepository {
    val database = StoryDatabase.getDatabase(context)
    val apiService = ApiConfig.getApiService()
    return StoriesRepository(apiService)
}