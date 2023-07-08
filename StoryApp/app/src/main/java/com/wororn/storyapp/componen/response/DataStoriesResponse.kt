package com.wororn.storyapp.componen.response

import com.google.gson.annotations.SerializedName

data class DataStoriesResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)
