package com.example.dadjokes

import com.example.dadjokes.database.JokeDao
import com.example.dadjokes.database.JokeEntity
import kotlinx.coroutines.flow.Flow

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