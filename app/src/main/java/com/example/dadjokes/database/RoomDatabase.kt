package com.example.dadjokes.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM joke WHERE joke LIKE '%' || :searchTerm || '%'")
    fun getSavedJokeByWord(searchTerm: String): Flow<List<JokeEntity>>

    @Query("SELECT * FROM joke")
    fun getAllSavedJokes(): Flow<List<JokeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( joke: JokeEntity)
}

@Database(entities = [JokeEntity::class], version = 1)
abstract class JokeDatabase: RoomDatabase() {
    abstract val jokeDao: JokeDao
}

private lateinit var INSTANCE: JokeDatabase

fun getDatabase(context: Context): JokeDatabase {
    synchronized(JokeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                JokeDatabase::class.java,
                "joke_database").build()
        }
    }
    return INSTANCE
}