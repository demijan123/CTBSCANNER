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
    val okxOrderId: String? = null,
    val isMexcTrade: Boolean = false,
    val mexcOrderId: String? = null,
    val isMexcDemoTrade: Boolean = false,
    val whyTradeReason: String = "",
    val timeframe: String = "15m",
    val leverage: Double = 1.0,
    val riskRewardRatio: Double = 1.5,
    val rsi: Double = 50.0,
    val volatility: Double = 0.05,
    val volume: Double = 12500000.0,
    val trend: String = "NEUTRAL",
    val exchange: String = "PAPER",
    
    // Fee-accounting fields supporting both net and gross PnL
    val entryValue: Double = 0.0,
    val exitValue: Double = 0.0,
    val entryFee: Double = 0.0,
    val exitFee: Double = 0.0,
    val totalFees: Double = 0.0,
    val grossPnl: Double = 0.0,
    val netPnl: Double = 0.0
)
