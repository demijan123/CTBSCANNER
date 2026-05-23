package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_signals")
data class SavedSignal(
    @PrimaryKey val id: String, // Coin ID (e.g., "solana")
    val symbol: String,
    val name: String,
    val image: String?,
    val currentPrice: Double,
    val marketCap: Double,
    val totalVolume: Double?,
    val signal: String, // LONG, SHORT, NONE
    val confidence: Int,
    val strategy: String,
    val stopLoss: Double,
    val takeProfit: Double,
    val rationale: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false
)
