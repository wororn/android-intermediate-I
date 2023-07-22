package com.wororn.storyapp.api

import com.wororn.storyapp.componen.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @GET("stories")
    suspend fun listStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): TableStoriesResponse


    @GET("stories")
    suspend fun searchStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("name") name: String
    ): SearchStoriesResponse

    @GET("stories")
    suspend fun tableStories(@Header("Authorization") token: String): TableStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addFieldStories(
        @Header("Authorization") Authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): DataStoriesResponse

}