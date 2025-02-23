package com.android.support.exercise.network

import com.android.support.exercise.data.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}