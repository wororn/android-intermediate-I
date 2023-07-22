package com.wororn.storyapp.interfaces.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wororn.storyapp.api.ApiService
import com.wororn.storyapp.componen.repository.StoriesRepository
import com.wororn.storyapp.componen.repository.UsersRepository
import com.wororn.storyapp.componen.response.TabStoriesItem
import com.wororn.storyapp.paging.SearchPagingSource
import com.wororn.storyapp.paging.StoriesPagingSource
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository,private val storiesRepository: StoriesRepository) : ViewModel() {

    private val currentQuery= MutableLiveData(DEFAULT_SEARCH)
    private var pagingSource:SearchPagingSource?=null

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

    fun searchStory (token: String,query:String): LiveData<PagingData<TabStoriesItem>> = currentQuery.switchMap {
        pagingSource!!
        storiesRepository.getSearchStory(token,query).cachedIn(viewModelScope)
    }

 //   fun searchStories (token: String): LiveData<PagingData<TabStoriesItem>> = currentQuery.switchMap {
 //       storiesRepository.listStory(token).cachedIn(viewModelScope)
 //   }

    fun searchQuery(query: String){
        currentQuery.value=query
        pagingSource?.invalidate()

    }
    private var tabStories: MutableLiveData<ArrayList<TabStoriesItem>> = MutableLiveData()

    private fun getStoriesList(): MutableLiveData<ArrayList<TabStoriesItem>> {
        return tabStories
    }

companion object{
    private const val DEFAULT_SEARCH=""
}
}

