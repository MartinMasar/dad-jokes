package com.example.dadjokes

import com.example.dadjokes.api.JokesApiService
import com.example.dadjokes.model.JokesResponse
import com.example.dadjokes.model.JokeItem

class APIRepository (private val apiService: JokesApiService){

    suspend fun getRandomJoke () : JokeItem? {
        val response = apiService.getRandomJoke()

        return if(response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getJokeByWordAndPage(input: String, page: Int): JokesResponse? {
        val response = apiService.getJokeByWordAndPage(input, page)

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
