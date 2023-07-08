package com.wororn.storyapp.interfaces.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wororn.storyapp.componen.repository.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesViewModel(private val userRepository: UsersRepository, private val storiesRepository: StoriesRepository) : ViewModel()  {
    fun getToken() : LiveData<String> {
        return userRepository.getToken().asLiveData()
    }

    fun addFieldStories(token: String, photo: MultipartBody.Part, desc: RequestBody) = storiesRepository.addFieldStories(token, photo, desc)

}