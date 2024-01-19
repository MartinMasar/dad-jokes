package com.example.dadjokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dadjokes.database.JokeEntity
import com.example.dadjokes.model.DadJokes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiRepository: APIRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    // LiveData to hold the list of jokes fetched from the API
    private val _jokeList = MutableLiveData<List<DadJokes>>()
    val jokeList: LiveData<List<DadJokes>> = _jokeList

    // LiveData to hold the list of favorite jokes from the local database
    private val _favoriteJokeList = MutableLiveData<List<DadJokes>>()
    val favoriteJokeList: LiveData<List<DadJokes>> = _favoriteJokeList

    // LiveData to hold the count of favorite jokes
    private val _favoriteJokeCount = MutableLiveData<Int>()
    val favoriteJokeCount: LiveData<Int> = _favoriteJokeCount


    // Fetch a random joke from the API
    fun getRandomJoke() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiRepository.getRandomJoke()

            if (result != null) {
                _jokeList.postValue(listOf(DadJokes(result.joke)))
            }
        }
    }

    // Fetch jokes containing a specific word from the API
    fun getJokeByWord(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var result = apiRepository.getJokeByWordAndPage(input, 1)

            if (result == null) {
                // If no result, post an empty list
                _jokeList.postValue(result?.results?.map { DadJokes(it.joke) } ?: emptyList())
            } else {
                var updatedResults =result.results
                // Fetch additional pages of results until the last page is reached (does not fetch more then 5 pages because of words like "and" and "the")
                while(result?.currentPage?:6<=5 && !(result?.currentPage?:0 == result?.nextPage?:0)) {
                    result = apiRepository.getJokeByWordAndPage(input, result?.nextPage ?: 1)
                    updatedResults += result?.results ?: emptyArray()
                }
                // Post the updated results to the LiveData
                _jokeList.postValue(updatedResults.map {DadJokes(it.joke)})
            }
        }
    }

    // Add a joke to the local database
    fun addToFavorites(joke: DadJokes) {
        viewModelScope.launch(Dispatchers.IO) {
            val jokeEntity = JokeEntity(joke = joke.joke)
            databaseRepository.addToFavorites(jokeEntity)
        }
    }

    // Remove a joke from the local database
    fun removeFromFavorites(joke: DadJokes) {
        viewModelScope.launch(Dispatchers.IO) {
            val jokeEntity = JokeEntity(joke = joke.joke)
            databaseRepository.removeFromFavorites(jokeEntity)
        }
    }

    // Update the LiveData with the list of favorite jokes
    private fun updateFavoriteJokesList(jokes: List<JokeEntity>) {
        val convertedJokes = jokes.map { DadJokes(joke = it.joke) }
        _favoriteJokeList.postValue(convertedJokes)
    }

    // Update the LiveData with the count of favorite jokes
    private fun updateFavoriteJokeCount(jokes: List<JokeEntity>) {
        _favoriteJokeCount.postValue(jokes.size)
    }

    // Fetch all saved jokes from the local database
    fun getAllSavedJokes() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = databaseRepository.getAllSavedJokes()
            result.collect { jokes ->
                updateFavoriteJokesList(jokes)
                updateFavoriteJokeCount(jokes)
            }
        }
    }

    // Fetch saved jokes containing a specific word from the local database
    fun getSavedJokeByWord(searchTerm: String) {
        if(searchTerm==""){
            // If the search term is empty, fetch all saved jokes
            getAllSavedJokes()
        }
        else{
            viewModelScope.launch(Dispatchers.IO) {
                val result = databaseRepository.getSavedJokeByWord(searchTerm)
                result.collect { jokes ->
                    updateFavoriteJokesList(jokes)
                }
            }
        }
    }

    // Check if a joke is saved
    fun isJokeSaved(joke: DadJokes): Boolean {
        return favoriteJokeList.value?.any { it.joke == joke.joke } == true
    }

    // Delete all jokes from the local database
    fun deleteAllJokes() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.deleteAllJokes()
        }
    }
}