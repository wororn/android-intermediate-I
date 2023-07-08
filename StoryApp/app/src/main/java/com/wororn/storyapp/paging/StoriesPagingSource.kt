package com.wororn.storyapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wororn.storyapp.api.ApiService
import com.wororn.storyapp.componen.response.TabStoriesItem

class StoriesPagingSource(private val apiService: ApiService, private val token:String) : PagingSource<Int, TabStoriesItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TabStoriesItem> {
        return try {

            val page = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.listStory("Bearer $token",page, params.loadSize)

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, TabStoriesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}