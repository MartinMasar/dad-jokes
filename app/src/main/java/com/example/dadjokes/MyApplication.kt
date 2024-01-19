package com.example.dadjokes

import android.app.Application
import com.example.dadjokes.api.JokesApiService
import com.example.dadjokes.database.getDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    val apiService: JokesApiService by lazy {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://icanhazdadjoke.com/")
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON serialization/deserialization
            .build()

        retrofit.create(JokesApiService::class.java)
    }

    val apiRepository: APIRepository by lazy {
        APIRepository(apiService)
    }

    val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepository(getDatabase(this).jokeDao)
    }
}