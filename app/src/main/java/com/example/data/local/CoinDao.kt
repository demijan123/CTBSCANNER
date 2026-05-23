package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM saved_signals ORDER BY timestamp DESC")
    fun getAllSignals(): Flow<List<SavedSignal>>

    @Query("SELECT * FROM saved_signals WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedSignals(): Flow<List<SavedSignal>>

    @Query("SELECT * FROM saved_signals WHERE id = :id LIMIT 1")
    suspend fun getSignalById(id: String): SavedSignal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignal(signal: SavedSignal)

    @Update
    suspend fun updateSignal(signal: SavedSignal)

    @Delete
    suspend fun deleteSignal(signal: SavedSignal)

    @Query("DELETE FROM saved_signals WHERE id = :id")
    suspend fun deleteSignalById(id: String)

    @Query("DELETE FROM saved_signals WHERE isBookmarked = 0")
    suspend fun clearUnsavedSignals()
}
