package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PaperTradeDao {
    @Query("SELECT * FROM paper_trades ORDER BY timestamp DESC")
    fun getAllTrades(): Flow<List<PaperTrade>>

    @Query("SELECT * FROM paper_trades WHERE status = 'OPEN' ORDER BY timestamp DESC")
    fun getOpenTradesFlow(): Flow<List<PaperTrade>>

    @Query("SELECT * FROM paper_trades WHERE status = 'OPEN' ORDER BY timestamp DESC")
    suspend fun getOpenTrades(): List<PaperTrade>

    @Query("SELECT * FROM paper_trades WHERE status != 'OPEN' ORDER BY timestamp DESC")
    fun getClosedTradesFlow(): Flow<List<PaperTrade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: PaperTrade): Long

    @Update
    suspend fun updateTrade(trade: PaperTrade)

    @Update
    suspend fun updateTrades(trades: List<PaperTrade>)

    @Delete
    suspend fun deleteTrade(trade: PaperTrade)

    @Query("DELETE FROM paper_trades")
    suspend fun clearAllTrades()
}
