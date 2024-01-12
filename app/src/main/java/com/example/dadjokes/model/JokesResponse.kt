package com.example.dadjokes.model

/*
data class JokesResponse(
    val current_page: Int,
    val limit: Int,
    val next_page: Int,
    val previous_page: Int,
    val results: Array<JokeItem>,
    val search_term: String,
    val status: Int,
    val total_jokes: Int,
    val total_pages: Int
)*/

data class JokesResponse(
    val current_page: Int,
    val next_page: Int,
    val results: Array<JokeItem>
)

data class JokeItem(
    val id: String,
    val joke: String
)
