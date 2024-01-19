package com.example.dadjokes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joke")
data class JokeEntity constructor(
    @PrimaryKey
    val joke: String
)