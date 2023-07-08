package com.wororn.storyapp.interfaces.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wororn.storyapp.componen.repository.UsersRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UsersRepository) : ViewModel() {

    fun setToken(token: String) {
        viewModelScope.launch {
            userRepository.setToken(token)
        }
    }
    fun getToken(): LiveData<String> {
        return userRepository.getToken().asLiveData()
    }


    fun login(email: String, password: String) = userRepository.login(email, password)
}