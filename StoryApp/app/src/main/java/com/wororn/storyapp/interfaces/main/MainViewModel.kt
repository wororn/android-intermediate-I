package com.wororn.storyapp.interfaces.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wororn.storyapp.componen.repository.*
import com.wororn.storyapp.componen.response.TabStoriesItem
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val usersRepository: UsersRepository,private val storiesRepository: StoriesRepository) : ViewModel() {

    fun getToken() : LiveData<String> {
        return usersRepository.getToken().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            usersRepository.logout()
        }
    }

    fun listStory(token: String): LiveData<PagingData<TabStoriesItem>> =
        storiesRepository.listStory(token).cachedIn(viewModelScope)

 //   fun tableStories(token: String) = storiesRepository.tableStories(token)

 //   fun tabStories(token: String) = storiesRepository.tableStories(token)

   // private val _storyList = MutableLiveData<List<TabStoriesItem>>()
   // val storyList: LiveData<List<TabStoriesItem>> = _storyList

    private var tabStories: MutableLiveData<ArrayList<TabStoriesItem>> = MutableLiveData()

    private fun getStoriesList(): MutableLiveData<ArrayList<TabStoriesItem>> {
        return tabStories
    }

    fun searchStories(searchString: String): ArrayList<TabStoriesItem> {

        val resultsList: ArrayList<TabStoriesItem> = ArrayList()

        if (getStoriesList().value == null) return resultsList

        if (getStoriesList().value!!.size > 0) {

            for ( story in getStoriesList().value!!.iterator()) {
                if (story.name!!.lowercase(Locale.ROOT).startsWith(searchString)) {
                    resultsList.add(story)
                }
            }
        }
        resultsList.sortBy { it.name }
        return resultsList
    }

}

