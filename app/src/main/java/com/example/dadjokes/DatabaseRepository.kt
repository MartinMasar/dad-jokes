package com.example.dadjokes

import android.util.Log
import com.example.dadjokes.database.JokeDao
import com.example.dadjokes.database.JokeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class DatabaseRepository (private val jokeDao: JokeDao){

    suspend fun addToFavorites(jokeEntity: JokeEntity) {
        jokeDao.insert(jokeEntity)
    }

    suspend fun removeFromFavorites(joke: JokeEntity) {
        jokeDao.deleteJoke(joke)
    }

    suspend fun getAllSavedJokes(): Flow<List<JokeEntity>> {
        return jokeDao.getAllSavedJokes()
    }

    suspend fun getSavedJokeByWord(searchTerm: String): Flow<List<JokeEntity>> {
        return jokeDao.getSavedJokeByWord(searchTerm)
    }

    suspend fun deleteAllJokes() {
        return jokeDao.deleteAllJokes()
    }

}