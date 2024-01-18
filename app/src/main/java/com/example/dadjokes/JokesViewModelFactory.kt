package com.example.dadjokes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JokesViewModelFactory(
    private val apiRepository: APIRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiRepository, databaseRepository) as T
        }
        throw IllegalArgumentException("Wrong ViewModel class")
    }
}
