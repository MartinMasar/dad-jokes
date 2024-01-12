package com.example.dadjokes.api

import com.example.dadjokes.model.JokeItem
import com.example.dadjokes.model.JokesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface JokesApiService {

    @GET("?")
    suspend fun getRandomJoke(@Header("Accept") acceptHeader: String = "application/json"): Response<JokeItem>

    @GET("search")
    suspend fun getJokeByWord(
        @Query("term") input: String,
        @Header("Accept") acceptHeader: String = "application/json"
    ): Response<JokesResponse>

    @GET("search")
    suspend fun getJokeByWordAndPage(
        @Query("term") input: String,
        @Query("page") page: Int,
        @Header("Accept") acceptHeader: String = "application/json"
    ): Response<JokesResponse>
}