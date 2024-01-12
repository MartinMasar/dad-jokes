package com.example.dadjokes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.dadjokes.model.DadJokes
import com.example.dadjokes.model.JokeItem
import com.example.dadjokes.model.JokesResponse
//import com.example.dadjokes.model.JokeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _jokesResponseValue = MutableLiveData<JokesResponse>()
    val jokesResponseValue: LiveData<JokesResponse> = _jokesResponseValue

    //private val _jokes = MutableLiveData<JokeItem>()
    //val jokes: LiveData<JokeItem> = _jokes

    val jokeList: LiveData<List<DadJokes>> = jokesResponseValue.map { response ->
        response?.results?.map { DadJokes(it.joke) } ?: emptyList()
    }

    fun getRandomJoke() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getRandomJoke()
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

    fun getJokeByWord(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getJokeByWord(input)
            _jokesResponseValue.postValue(result)
        }
    }

    fun getJokeByWordAndPage(input: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Fetch the new data
            val result = repository.getJokeByWordAndPage(input, page)

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
    }

}