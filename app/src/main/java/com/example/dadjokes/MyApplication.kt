package com.example.dadjokes

import android.app.Application
import com.example.dadjokes.api.JokesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication : Application() {
    val apiService: JokesApiService by lazy {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://icanhazdadjoke.com/")
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON serialization/deserialization
            .build()

        retrofit.create(JokesApiService::class.java)
    }

    val repository: Repository by lazy {
        Repository(apiService)
    }

    override fun onCreate() {
        super.onCreate()
    }
}