package com.example.dadjokes.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dadjokes.model.DadJokes
import com.example.dadjokes.model.JokeItem

@Entity(tableName = "joke")
data class JokeEntity constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //@PrimaryKey
    //val id: String,
    val joke: String
) {
    /**
     * Map Subject info object to domain entity
     */
    fun asDomainModel(): DadJokes {
        return DadJokes(
            this.joke
        )
    }
}