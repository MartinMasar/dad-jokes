package com.example.dadjokes

import com.example.dadjokes.api.JokesApiService
import com.example.dadjokes.model.JokesResponse
import com.example.dadjokes.model.JokeItem

class Repository (private val apiService: JokesApiService){

    suspend fun getRandomJoke () : JokeItem? {
        val response = apiService.getRandomJoke()

        if(response.isSuccessful) {
            return response.body()
        } else {
            return null
        }
    }

    suspend fun getJokeByWord(input: String): JokesResponse? {
        val response = apiService.getJokeByWord(input)

        return if (response.isSuccessful) {
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
