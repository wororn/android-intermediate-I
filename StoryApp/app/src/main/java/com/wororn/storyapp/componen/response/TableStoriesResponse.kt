package com.wororn.storyapp.componen.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TableStoriesResponse(

    @field:SerializedName("listStory")
    val  listStory: List<TabStoriesItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Parcelize
@Entity(tableName = "stories")
data class TabStoriesItem(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null
) : Parcelable


data class SearchStoriesResponse(

    @field:SerializedName("listStory")
    val  listStory: List<TabStoriesItem>,
    @field:SerializedName("name")
    val name: String? = null

)