package com.example

import com.example.data.local.PaperTrade

data class StrategyTimeframeStats(
    val strategy: String,
    val timeframe: String,
    val totalTrades: Int,
    val winningTrades: Int,
    val losingTrades: Int,
    val winRate: Double,
    val totalPnL: Double,
    val avgProfit: Double,
    val avgLoss: Double,
    val profitFactor: Double,
    val avgRiskReward: Double,
    val avgDurationMinutes: Double,
    val maxDrawdown: Double,
    val sharpeRatio: Double,
    val consecutiveWins: Int,
    val consecutiveLosses: Int
)

data class CoinStats(
    val symbol: String,
    val totalTrades: Int,
    val winningTrades: Int,
    val winRate: Double,
    val totalPnL: Double,
    val profitFactor: Double
)

data class RegimeStats(
    val regime: String,
    val totalTrades: Int,
    val wins: Int,
    val winRate: Double,
    val totalPnL: Double
)

fun computeStrategyTimeframeStats(
    allTrades: List<PaperTrade>,
    strategy: String,
    timeframe: String
): StrategyTimeframeStats {
    val filtered = allTrades.filter { 
        it.strategy.equals(strategy, ignoreCase = true) && 
        it.timeframe.equals(timeframe, ignoreCase = true) 
    }
    
    val total = filtered.size
    val closed = filtered.filter { it.status.uppercase().startsWith("CLOSED") }
    val wins = closed.filter { it.pnl > 0.0 }
    val losses = closed.filter { it.pnl <= 0.0 }
    
    val winRate = if (closed.isNotEmpty()) (wins.size.toDouble() / closed.size.toDouble()) * 100.0 else 0.0
    val totalPnL = closed.sumOf { it.pnl }
    
    val sumWins = wins.sumOf { it.pnl }
    val sumLosses = losses.sumOf { Math.abs(it.pnl) }
    
    val avgProfit = if (wins.isNotEmpty()) sumWins / wins.size else 0.0
    val avgLoss = if (losses.isNotEmpty()) sumLosses / losses.size else 0.0
    val profitFactor = if (sumLosses > 0.0) sumWins / sumLosses else if (sumWins > 0.0) 99.9 else 1.0
    
    val avgRR = if (filtered.isNotEmpty()) filtered.map { it.riskRewardRatio }.average() else 1.5
    
    val closedDurations = closed.filter { it.exitTimestamp != null && it.exitTimestamp!! > it.timestamp }
    val avgDurMin = if (closedDurations.isNotEmpty()) {
        closedDurations.map { (it.exitTimestamp!! - it.timestamp) / 60000.0 }.average()
    } else {
        0.0
    }
    
    val sortedClosed = closed.filter { it.exitTimestamp != null }.sortedBy { it.exitTimestamp }
    var peak = 0.0
    var runningBalance = 0.0
    var maxDd = 0.0
    for (t in sortedClosed) {
        runningBalance += t.pnl
        if (runningBalance > peak) {
            peak = runningBalance
        }
        val ddPct = if (peak != 0.0) ((peak - runningBalance) / Math.abs(peak)) * 100.0 else 0.0
        if (ddPct > maxDd) {
            maxDd = ddPct
        }
    }
    
    val pnls = closed.map { it.pnl }
    val sharpe = if (pnls.size >= 3) {
        val mean = pnls.average()
        val variance = pnls.map { Math.pow(it - mean, 2.0) }.sum() / pnls.size
        val stdDev = Math.sqrt(variance)
        if (stdDev > 0.0) (mean / stdDev) * Math.sqrt(252.0) else 0.0
    } else {
         0.0
    }
    
    var maxConsecWins = 0
    var currentConsecWins = 0
    var maxConsecLosses = 0
    var currentConsecLosses = 0
    
    for (t in sortedClosed) {
        if (t.pnl > 0.0) {
            currentConsecWins++
            currentConsecLosses = 0
            if (currentConsecWins > maxConsecWins) maxConsecWins = currentConsecWins
        } else {
            currentConsecLosses++
            currentConsecWins = 0
            if (currentConsecLosses > maxConsecLosses) maxConsecLosses = currentConsecLosses
        }
    }
    
    return StrategyTimeframeStats(
        strategy = strategy,
        timeframe = timeframe,
        totalTrades = total,
        winningTrades = wins.size,
        losingTrades = losses.size,
        winRate = winRate,
        totalPnL = totalPnL,
        avgProfit = avgProfit,
        avgLoss = avgLoss,
        profitFactor = profitFactor,
        avgRiskReward = if (avgRR.isNaN() || avgRR.isInfinite()) 1.5 else avgRR,
        avgDurationMinutes = avgDurMin,
        maxDrawdown = maxDd,
        sharpeRatio = if (sharpe.isNaN() || sharpe.isInfinite()) 0.0 else sharpe,
        consecutiveWins = maxConsecWins,
        consecutiveLosses = maxConsecLosses
    )
}
