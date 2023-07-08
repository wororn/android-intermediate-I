package com.wororn.storyapp.interfaces.register

import androidx.lifecycle.ViewModel
import com.wororn.storyapp.componen.repository.UsersRepository

class RegisterViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = usersRepository.register(name, email, password)
}