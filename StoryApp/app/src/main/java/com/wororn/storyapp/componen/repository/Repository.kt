package com.wororn.storyapp.componen.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.wororn.storyapp.tools.Status
import com.wororn.storyapp.api.ApiService
import com.wororn.storyapp.componen.response.*
import com.wororn.storyapp.paging.StoriesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoriesRepository (private val apiService: ApiService,) {
    fun listStory(token: String): LiveData<PagingData<TabStoriesItem>> {

        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),

            pagingSourceFactory = {
                StoriesPagingSource(apiService, token)

            }
        ).liveData
    }

    fun tableStories(token: String): LiveData<PagingData<TabStoriesItem>> = liveData {
        apiService.tableStories("Bearer $token")
    }

    fun tabStories(token: String) : LiveData<Status<TableStoriesResponse>> = liveData {

        val data = apiService.tableStories("Bearer $token")
        emit(Status.Done(data))

    }

        fun addFieldStories(token: String, photo: MultipartBody.Part, desc: RequestBody) : LiveData<Status<DataStoriesResponse>> = liveData {
            emit(Status.Process)
            try {
                val data = apiService.addFieldStories("Bearer $token", photo, desc)
                emit(Status.Done(data))
            } catch (except: Exception) {
                emit(Status.Fail(except.message.toString()))
            }
        }


    companion object {
        @Volatile
        private var instanceStories: StoriesRepository? = null
        fun getInstance(apiService: ApiService): StoriesRepository =
            instanceStories ?: synchronized(this) {
                instanceStories ?: StoriesRepository(apiService)
            }.also { instanceStories = it }
    }
}


class UsersRepository private constructor(private val dataStore: DataStore<Preferences>, private val apiService: ApiService) {
    fun login(email: String, password: String) : LiveData<Status<LoginResponse>> = liveData {
        emit(Status.Process)
        try {
                val data = apiService.login(email, password)
                emit(Status.Done(data))
           } catch (except: Exception) {
               emit(Status.Fail(except.message.toString()))
           }
    }

    fun register(name: String, email: String, password: String) : LiveData<Status<RegisterResponse>> = liveData {
        emit(Status.Process)
        try {
                val data =  apiService.register(name, email, password)
                emit(Status.Done(data))
            } catch (except: Exception) {
                emit(Status.Fail(except.message.toString()))
            }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[EXTRA_KEY] = false
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[EXTRA_KEY ] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[EXTRA_KEY ] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var instanceUsers: UsersRepository? = null

        private val TOKEN = stringPreferencesKey("token")
        private val EXTRA_KEY = booleanPreferencesKey("extra")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): UsersRepository {
            return instanceUsers ?: synchronized(this) {
                val instance = UsersRepository(dataStore, apiService)
                instanceUsers = instance
                instance
            }
        }
    }
}
