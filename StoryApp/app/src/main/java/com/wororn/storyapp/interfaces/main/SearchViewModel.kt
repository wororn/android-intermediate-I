package com.wororn.storyapp.interfaces.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wororn.storyapp.api.ApiService
import com.wororn.storyapp.paging.SearchPagingSource

class SearchViewModel(private val apiService:ApiService,private val token:String,private val query:String) : ViewModel(){
    private val currentQuery= MutableLiveData(DEFAULT_SEARCH)
    private var pagingSource: SearchPagingSource?=null

    get() {
        if (field==null||field?.invalid==true){

            field=SearchPagingSource(apiService,token,query)
        }
     return field
    }
    fun searchQuery(query: String){
        currentQuery.value=query
        pagingSource?.invalidate()

    }

    companion object{
        private const val DEFAULT_SEARCH=""
    }

}