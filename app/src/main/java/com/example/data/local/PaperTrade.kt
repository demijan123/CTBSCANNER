package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paper_trades")
data class PaperTrade(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinId: String,
    val symbol: String,
    val name: String,
    val image: String?,
    val signalType: String, // LONG or SHORT
    val entryPrice: Double,
    val currentPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val quantity: Double, // Amount of crypto assets
    val timestamp: Long = System.currentTimeMillis(),
    val status: String, // "OPEN" or "CLOSED_TP" or "CLOSED_SL" or "CLOSED_MANUAL"
    val exitPrice: Double? = null,
    val exitTimestamp: Long? = null,
    val pnl: Double = 0.0, // Profit / loss in USD
    val investedAmount: Double = 0.0, // Initial amount in USD
    val strategy: String = "Manual Position",
    val isOkxTrade: Boolean = false,
    val okxOrderId: String? = null
)
