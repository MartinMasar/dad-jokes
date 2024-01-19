package com.example.dadjokes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.dadjokes.database.JokeEntity
import com.example.dadjokes.model.DadJokes
import com.example.dadjokes.model.JokeItem
import com.example.dadjokes.model.JokesResponse
//import com.example.dadjokes.model.JokeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiRepository: APIRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {
    private val _jokesResponseValue = MutableLiveData<JokesResponse>()
    val jokesResponseValue: LiveData<JokesResponse> = _jokesResponseValue

    //private val _jokes = MutableLiveData<JokeItem>()
    //val jokes: LiveData<JokeItem> = _jokes

    val jokeList: LiveData<List<DadJokes>> = jokesResponseValue.map { response ->
        response?.results?.map { DadJokes(it.joke) } ?: emptyList()
    }

    private val _favoriteJokeList = MutableLiveData<List<DadJokes>>()
    val favoriteJokeList: LiveData<List<DadJokes>> = _favoriteJokeList

    private val _favoriteJokeCount = MutableLiveData<Int>()
    val favoriteJokeCount: LiveData<Int> = _favoriteJokeCount


    fun getRandomJoke() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiRepository.getRandomJoke()
            //_jokes.postValue(result)

            /*if(result == null){
                _jokesResponseValue.postValue(result)
            }
            else{
                val jokesResponse = JokesResponse(
                    current_page = 1,
                    next_page = 1,
                    //results = result?.let { arrayOf(it) } ?: null
                    //results = arrayOf(result?:JokeItem(id = "default_id", joke = "default_joke"))
                    results = arrayOf(result)
                )
                _jokesResponseValue.postValue(jokesResponse)
            }*/
            /*try {
                val result = repository.getRandomJoke()

                val jokesResponse = JokesResponse(
                    current_page = 1,
                    next_page = 1,
                    results = arrayOf(result ?: JokeItem(id = "default_id", joke = "default_joke "))
                )
                _jokesResponseValue.postValue(jokesResponse)
            } catch (e: Exception) {
                Log.e("JokeError", e.toString()) // Log any errors
            }*/

            if (result != null) {
                val jokesResponse = JokesResponse(
                    current_page = 1,
                    next_page = 1,
                    results = arrayOf(result)
                )
                _jokesResponseValue.postValue(jokesResponse)
            }
        }
    }

    /*fun getJokeByWord(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiRepository.getJokeByWord(input)
            _jokesResponseValue.postValue(result)
        }
    }*/

    fun getJokeByWord(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Fetch the new data
            var result = apiRepository.getJokeByWordAndPage(input, 1)
            // Check if there is existing data
            Log.d("result", result.toString())
            if (result == null) {
                // If no existing data, set the result directly
                _jokesResponseValue.postValue(result)
            } else {
                var updatedResults =result.results
                Log.d("updatedResults", updatedResults.toString())
                while(result?.current_page?:6<=5 && !(result?.current_page?:0 == result?.next_page?:0)) {
                    // If there is existing data, update the results array and replace current_page and next_page
                    //val newResults = apiRepository.getJokeByWordAndPage(input, result?.next_page ?: 1)
                    result = apiRepository.getJokeByWordAndPage(input, result?.next_page ?: 1)
                    Log.d("newResults", result.toString())
                    //= result?.results ?: emptyArray()
                    //updatedResults += newResults?.results ?: emptyArray()
                    updatedResults += result?.results ?: emptyArray()
                    Log.d("updatedResultswhile", updatedResults.toString())

                }
                val updatedJokesResponse = JokesResponse(
                    current_page = result?.current_page ?: 0,
                    next_page = result?.next_page ?: 0,
                    results = updatedResults
                )
                Log.d("updatedJokesResponse", updatedJokesResponse.toString())
                Log.d("count", updatedJokesResponse.results.size.toString())

                // Post the updated value
                _jokesResponseValue.postValue(updatedJokesResponse)
            }
        }
    }

    /*fun getJokeByWordAndPage(input: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Fetch the new data
            val result = apiRepository.getJokeByWordAndPage(input, page)

            // Get the existing value
            val currentValue = _jokesResponseValue.value

            // Check if there is existing data
            if (currentValue == null) {
                // If no existing data, set the result directly
                _jokesResponseValue.postValue(result)
            } else {
                // If there is existing data, update the results array and replace current_page and next_page
                val newResults = result?.results ?: emptyArray()
                val updatedResults = currentValue.results + newResults
                val updatedJokesResponse = JokesResponse(
                    current_page = result?.current_page ?: 0,
                    next_page = result?.next_page ?: 0,
                    results = updatedResults
                )

                // Post the updated value
                _jokesResponseValue.postValue(updatedJokesResponse)
            }
        }
    }*/

    fun addToFavorites(joke: DadJokes) {
        viewModelScope.launch(Dispatchers.IO) {
            val jokeEntity = JokeEntity(joke = joke.joke)
            databaseRepository.addToFavorites(jokeEntity)
        }
    }

    fun removeFromFavorites(joke: DadJokes) {
        viewModelScope.launch(Dispatchers.IO) {
            val jokeEntity = JokeEntity(joke = joke.joke)
            databaseRepository.removeFromFavorites(jokeEntity)
            //Log.d("viewmodel", "id: ${jokeEntity.id}, joke: ${jokeEntity.joke}")
        }
    }

    private fun updateFavoriteJokesList(jokes: List<JokeEntity>) {
        // Process the result and convert it to JokeItem
        val convertedJokes = jokes.map { DadJokes(joke = it.joke) }
        _favoriteJokeList.postValue(convertedJokes)
    }

    private fun updateFavoriteJokeCount(jokes: List<JokeEntity>) {
        _favoriteJokeCount.postValue(jokes.size)
    }

    fun getAllSavedJokes() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = databaseRepository.getAllSavedJokes()
            result.collect { jokes ->
                updateFavoriteJokesList(jokes)
                updateFavoriteJokeCount(jokes)
            }
        }
    }

    fun getSavedJokeByWord(searchTerm: String) {
        if(searchTerm==""){
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

    fun isJokeSaved(joke: DadJokes): Boolean {
        return favoriteJokeList.value?.any { it.joke == joke.joke } == true
    }
}