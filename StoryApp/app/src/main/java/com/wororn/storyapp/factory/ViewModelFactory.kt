package com.wororn.storyapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wororn.storyapp.componen.repository.StoriesRepository
import com.wororn.storyapp.componen.repository.UsersRepository
import com.wororn.storyapp.injection.StoriesInjection
import com.wororn.storyapp.injection.UsersInjection
import com.wororn.storyapp.interfaces.login.LoginViewModel
import com.wororn.storyapp.interfaces.main.MainViewModel
import com.wororn.storyapp.interfaces.register.RegisterViewModel
import com.wororn.storyapp.interfaces.setmodes.ModesViewModel
import com.wororn.storyapp.interfaces.stories.StoriesViewModel

class StoriesViewModelFactory private constructor(private val usersRepository: UsersRepository, private val storiesRepository: StoriesRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(usersRepository,storiesRepository ) as T
                }
                modelClass.isAssignableFrom(StoriesViewModel::class.java) -> {
                    StoriesViewModel(usersRepository, storiesRepository) as T
                }
                else -> {
                    throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
                }
            }
        }
        companion object {

             @Volatile
            private var instanceStories: StoriesViewModelFactory? = null
            fun getInstance(context: Context): StoriesViewModelFactory =
                instanceStories ?: synchronized(this) {
                instanceStories ?: StoriesViewModelFactory(UsersInjection.providePreferences(context), StoriesInjection.provideRepository())
            }.also { instanceStories = it }
        }

    }

class UsersViewModelFactory (private val usersRepository: UsersRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(usersRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(usersRepository) as T
            }
            modelClass.isAssignableFrom(ModesViewModel::class.java) -> {
                ModesViewModel(usersRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
    companion object {
        @Volatile
        private var instanceUsers: UsersViewModelFactory? = null
        fun getInstance(context: Context): UsersViewModelFactory =
            instanceUsers ?: synchronized(this) {
                instanceUsers ?: UsersViewModelFactory(UsersInjection.providePreferences(context))
            }.also { instanceUsers = it }
    }
}

