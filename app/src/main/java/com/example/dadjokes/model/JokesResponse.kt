package com.example.dadjokes.model

data class DadJokes(
    val joke: String
)

data class JokesResponse(
    val currentPage: Int,
    val nextPage: Int,
    val results: Array<JokeItem>
)

data class JokeItem(
    val id: String,
    val joke: String
)
