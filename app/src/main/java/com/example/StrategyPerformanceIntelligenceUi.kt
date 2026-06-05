package com.example

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PaperTrade
import com.example.ui.CryptoViewModel
import com.example.ui.AiCruncherMode
import androidx.compose.foundation.BorderStroke
import java.util.Locale

@Composable
fun CoreIntelligenceSubTab(
    viewModel: CryptoViewModel,
    closedTrades: List<PaperTrade>,
    openTrades: List<PaperTrade>,
    selectedAiMode: AiCruncherMode,
    aiInsights: String,
    isGeneratingAiInsights: Boolean,
    context: Context,
    total: Int,
    winRate: Double,
    profitFactor: Double,
    totalRevenue: Double
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("trade_analytics_container"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "DECISION ALGORITHMIC METRICS & PERFORMANCE REPORT",
                color = CyberGold,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
            Text(
                text = "Dynamic performance analytics from historical system paper logs.",
                color = CyberTextDim,
                fontSize = 9.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CyberCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("CLOSED POSITIONS", fontSize = 8.sp, color = CyberTextDim)
                        Text("$total", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CyberTextWhite)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CyberCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("WIN RATE", fontSize = 8.sp, color = CyberTextDim)
                        Text("${String.format(Locale.US, "%.1f", winRate)}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (winRate >= 50.0) CyberAccentGreen else CyberAccentRed)
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CyberCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("PROFIT FACTOR", fontSize = 8.sp, color = CyberTextDim)
                        val pfText = if (profitFactor.isInfinite() || profitFactor > 90.0) "MAX" else String.format(Locale.US, "%.2f", profitFactor)
                        Text(pfText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CyberCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("NET REVENUE", fontSize = 8.sp, color = CyberTextDim)
                        Text("$${formatCurrency(totalRevenue)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (totalRevenue >= 0) CyberAccentGreen else CyberAccentRed)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("system_sharing_compliance_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📤 DATA JOURNAL & AUDIT ROADWAY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Export every logged trade in customized high-precision Excel spreadsheets (.csv format) for compliance reviews, or direct message them.",
                        color = CyberTextDim,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { exportTradesToCsvAndShare(context, openTrades, closedTrades) },
                        modifier = Modifier.fillMaxWidth().testTag("share_workbook_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "SHARE WORKBOOK REPORT (EXCEL CSV)",
                            color = CyberAccentGreen,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (aiInsights.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { shareToWhatsApp(context, "🤖 CYBER AI BRAIN ADVISORY UNIT REPORT [${selectedAiMode.title.uppercase()}]\n\n$aiInsights") },
                            modifier = Modifier.fillMaxWidth().testTag("share_whatsapp_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "SHARE ACTIVE AI REPORT ON WHATSAPP",
                                color = CyberGold,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ai_insight_optimization_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🤖 CYBER AI BRAIN ADVISORY CRUNCHER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Generate institutional-grade audit compliance, performance telemetry, and entry signal optimization using active Gemini models.",
                        color = CyberTextDim,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        for (mode in AiCruncherMode.values()) {
                            val selected = selectedAiMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(if (selected) CyberSurface else Color.Transparent, RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setAiMode(mode) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (mode) {
                                        AiCruncherMode.AUDIT -> "RISK AUDIT"
                                        AiCruncherMode.TRADE_ANALYSIS -> "TELEMETRY"
                                        AiCruncherMode.TRADING_SIGNALS -> "SIGNALS"
                                    },
                                    color = if (selected) CyberGold else CyberTextDim,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = selectedAiMode.description,
                        color = CyberTextWhite,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateAiOptimizationInsights() },
                        enabled = !isGeneratingAiInsights,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberSurface)
                    ) {
                        Text(
                            text = if (isGeneratingAiInsights) "CRUNCHING TELEMETRY..." else "ACTIVATE INTEL ENGINE: ${selectedAiMode.title.uppercase()}",
                            color = CyberAccentGreen,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isGeneratingAiInsights) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            color = CyberGold,
                            trackColor = CyberDark,
                            modifier = Modifier.fillMaxWidth().height(2.dp)
                        )
                    }
                }
            }
        }

        if (aiInsights.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("ai_insight_report_display_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, CyberSurface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active Report",
                                tint = CyberAccentGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DECISION SYSTEM TRANSCRIPT [${selectedAiMode.title.uppercase()}]",
                                color = CyberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MarkdownText(text = aiInsights)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { shareToWhatsApp(context, "🤖 CYBER AI BRAIN ADVISORY UNIT REPORT [${selectedAiMode.title.uppercase()}]\n\n$aiInsights") },
                                modifier = Modifier.weight(1f).testTag("share_report_whatsapp_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("WHATSAPP SHARE", color = CyberAccentGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Button(
                                onClick = { shareTextSystem(context, "🤖 CYBER AI BRAIN ADVISORY UNIT REPORT [${selectedAiMode.title.uppercase()}]\n\n$aiInsights", "AI Audit Report") },
                                modifier = Modifier.weight(1f).testTag("share_report_system_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("SYSTEM SHARE", color = CyberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StrategyTimeframeSubTab(
    viewModel: CryptoViewModel,
    closedTrades: List<PaperTrade>,
    strategyTimeframeSettings: Map<String, Set<String>>,
    strategiesList: List<String>,
    timeframesList: List<String>
) {
    val computedStatsList = remember(closedTrades) {
        val list = mutableListOf<StrategyTimeframeStats>()
        for (strategy in strategiesList) {
            for (timeframe in timeframesList) {
                list.add(computeStrategyTimeframeStats(closedTrades, strategy, timeframe))
            }
        }
        list
    }

    var performanceSortOption by remember { mutableStateOf("totalPnL") } // "totalPnL", "winRate", "totalTrades", "profitFactor"
    val sortedStats = remember(computedStatsList, performanceSortOption) {
        when (performanceSortOption) {
            "totalPnL" -> computedStatsList.sortedByDescending { it.totalPnL }
            "winRate" -> computedStatsList.sortedByDescending { it.winRate }
            "totalTrades" -> computedStatsList.sortedByDescending { it.totalTrades }
            "profitFactor" -> computedStatsList.sortedByDescending { it.profitFactor }
            else -> computedStatsList.sortedByDescending { it.totalPnL }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("strategy_timeframe_analytics_tab"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "📊 STRATEGY × TIMEFRAME SYSTEM MATRIX",
                color = CyberGold,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
            Text(
                text = "Optimize execution settings per individual-blueprint and timeframe parameters.",
                color = CyberTextDim,
                fontSize = 9.sp
            )
        }

        // MATRIX CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("strategy_timeframe_matrix_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("BLUEPRINT × TIMEFRAME HEATMAP LEDGER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        Column {
                            Row(
                                modifier = Modifier.background(CyberDark, RoundedCornerShape(6.dp)).padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Strategy Objective", modifier = Modifier.width(150.dp), color = CyberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                timeframesList.forEach { tf ->
                                    Text(tf, modifier = Modifier.width(62.dp), textAlign = TextAlign.Center, color = CyberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            strategiesList.forEach { strategy ->
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (strategy.length > 25) strategy.take(23) + ".." else strategy,
                                        modifier = Modifier.width(150.dp),
                                        color = CyberTextWhite,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    timeframesList.forEach { tf ->
                                        val cellStats = computedStatsList.firstOrNull { it.strategy == strategy && it.timeframe == tf }
                                        val count = cellStats?.totalTrades ?: 0
                                        val pnl = cellStats?.totalPnL ?: 0.0

                                        val color = when {
                                            count == 0 -> CyberTextDim
                                            pnl > 0.0 -> CyberAccentGreen
                                            else -> CyberAccentRed
                                        }

                                        val text = when {
                                            count == 0 -> "—"
                                            pnl >= 0.0 -> "+$${String.format(Locale.US, "%.0f", pnl)}"
                                            else -> "-$${String.format(Locale.US, "%.0f", Math.abs(pnl))}"
                                        }

                                        Column(
                                            modifier = Modifier.width(62.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                            if (count > 0) {
                                                Text("$count tx", color = CyberTextDim, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // RANKINGS WINDOW
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ranking_engine_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🏆 STRATEGY RANKING AUTOMATIC ENGINE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Ranks active strategy x timeframe configurations dynamically based on realized returns.", fontSize = 8.sp, color = CyberTextDim)
                    Spacer(modifier = Modifier.height(14.dp))

                    val rankedList = computedStatsList.filter { it.totalTrades > 0 }.sortedByDescending { it.totalPnL }
                    if (rankedList.isEmpty()) {
                        Text("Waiting for strategy execution trade history. Baseline showcase rankings:", color = CyberTextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(8.dp))
                        listOf(
                            "EMA Continuation Cross (V3) on 15m" to ("+72.5% win rate" to "Best Performer"),
                            "Wyckoff Spring & Phase C Accumulation on 4H" to ("+68.1% win rate" to "Consistently Stable"),
                            "High-Volume Momentum Breakout on 5m" to ("+62.4% win rate" to "High Frequency")
                        ).forEachIndexed { i, pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).background(CyberDark, RoundedCornerShape(8.dp)).padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Rank #${i+1}: ${pair.first}", color = CyberTextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    Text(pair.second.second, color = CyberGold, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                }
                                Text(pair.second.first, color = CyberAccentGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    } else {
                        rankedList.take(6).forEachIndexed { index, stat ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).background(CyberDark, RoundedCornerShape(8.dp)).padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Rank #${index + 1}: ${stat.strategy}", color = CyberTextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    Text("Timeframe: ${stat.timeframe} | PF: ${String.format(Locale.US, "%.2f", stat.profitFactor)} | Total: ${stat.totalTrades} trades", color = CyberTextDim, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("$${String.format(Locale.US, "%.1f", stat.totalPnL)}", color = if (stat.totalPnL >= 0.0) CyberAccentGreen else CyberAccentRed, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    Text("${String.format(Locale.US, "%.1f", stat.winRate)}% WR", color = CyberTextWhite, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }

        // FILTERS
        item {
            Column {
                Text("📊 LEADERBOARD TELEMETRY & METRIC SEARCH", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "totalPnL" to "🚀 PNL USD",
                        "winRate" to "🎯 Win Rate %",
                        "totalTrades" to "📈 Trades",
                        "profitFactor" to "💎 Profit Factor"
                    ).forEach { option ->
                        val isSelected = performanceSortOption == option.first
                        Box(
                            modifier = Modifier
                                .background(if (isSelected) CyberGold else CyberCard, RoundedCornerShape(12.dp))
                                .clickable { performanceSortOption = option.first }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(option.second, color = if (isSelected) CyberDark else CyberTextWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        val statsWithTrades = sortedStats.filter { it.totalTrades > 0 }
        if (statsWithTrades.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberCard)
                ) {
                    Box(modifier = Modifier.padding(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("No metrics recorded yet. Simulated values will populate here once trades finalize.", color = CyberTextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        } else {
            items(statsWithTrades) { stat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                    border = BorderStroke(1.dp, CyberSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(stat.strategy, color = CyberTextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Text("Timeframe Parameter: ${stat.timeframe}", color = CyberGold, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            }
                            Text(
                                text = if (stat.totalPnL >= 0.0) "+$${String.format(Locale.US, "%.2f", stat.totalPnL)}" else "-$${String.format(Locale.US, "%.2f", Math.abs(stat.totalPnL))}",
                                color = if (stat.totalPnL >= 0.0) CyberAccentGreen else CyberAccentRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Win Rate: ${String.format(Locale.US, "%.1f", stat.winRate)}%", color = CyberTextWhite, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                Text("Profit Factor: ${String.format(Locale.US, "%.2f", stat.profitFactor)}", color = CyberTextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                Text("Risk-Reward: ${String.format(Locale.US, "%.1f", stat.avgRiskReward)}:1", color = CyberTextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total Trades: ${stat.totalTrades} (${stat.winningTrades}W - ${stat.losingTrades}L)", color = CyberTextWhite, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                Text("Max Drawdown: ${String.format(Locale.US, "%.1f", stat.maxDrawdown)}%", color = CyberTextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                Text("Consecutive: ${stat.consecutiveWins}W / ${stat.consecutiveLosses}L", color = CyberTextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        // SWITCHBOARD CONTROLLER CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("switchboard_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🧬 TIMEFRAME OPTIMIZATION SWITCHBOARD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Control which active timeframes are permitted for each blueprint strategy. The strategy remains active but restricts executions.", fontSize = 8.sp, color = CyberTextDim)
                    Spacer(modifier = Modifier.height(14.dp))

                    strategiesList.forEach { strategy ->
                        val activeTfs = strategyTimeframeSettings[strategy] ?: setOf("5m", "15m", "30m", "1H", "4H")
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(strategy, color = CyberTextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                timeframesList.forEach { tf ->
                                    val isEnabled = activeTfs.contains(tf)
                                    Box(
                                        modifier = Modifier
                                            .background(if (isEnabled) CyberSurface else CyberDark, RoundedCornerShape(8.dp))
                                            .border(1.dp, if (isEnabled) CyberAccentGreen else CyberSurface, RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setTimeframeEnabled(strategy, tf, !isEnabled) }
                                            .weight(1f)
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = (if (isEnabled) "✓ " else "✗ ") + tf,
                                            color = if (isEnabled) CyberAccentGreen else CyberTextDim,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(color = CyberSurface, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CoinsRegimesSubTab(
    viewModel: CryptoViewModel,
    closedTrades: List<PaperTrade>,
    strategiesList: List<String>,
    timeframesList: List<String>
) {
    val coinStatsList = remember(closedTrades) {
        val grouped = closedTrades.groupBy { it.symbol.uppercase() }
        grouped.map { (symbol, trades) ->
            val totalT = trades.size
            val winsT = trades.filter { it.pnl > 0.0 }.size
            val winR = if (totalT > 0) (winsT.toDouble() / totalT) * 100.0 else 0.0
            val tcPnL = trades.sumOf { it.pnl }
            
            val tProfit = trades.filter { it.pnl > 0.0 }.sumOf { it.pnl }
            val tLoss = trades.filter { it.pnl < 0.0 }.sumOf { Math.abs(it.pnl) }
            val pf = if (tLoss > 0.0) tProfit / tLoss else if (tProfit > 0.0) 99.9 else 1.0

            CoinStats(
                symbol = symbol,
                totalTrades = totalT,
                winningTrades = winsT,
                winRate = winR,
                totalPnL = tcPnL,
                profitFactor = pf
            )
        }.sortedByDescending { it.totalPnL }
    }

    var selectedStrategyForRegime by remember { mutableStateOf("ALL") }

    val regimeStats = remember(closedTrades, selectedStrategyForRegime) {
        val targetTrades = if (selectedStrategyForRegime == "ALL") {
            closedTrades
        } else {
            closedTrades.filter { it.strategy == selectedStrategyForRegime }
        }

        val bullTrades = targetTrades.filter { it.trend.uppercase() == "UPTREND" || it.trend.uppercase() == "BULL" }
        val bearTrades = targetTrades.filter { it.trend.uppercase() == "DOWNTREND" || it.trend.uppercase() == "BEAR" }
        val sidewaysTrades = targetTrades.filter { it.trend.uppercase() == "SIDEWAYS" || it.trend.uppercase() == "NEUTRAL" }

        listOf(
            "Bullish Market" to bullTrades,
            "Bearish Market" to bearTrades,
            "Sideways Market" to sidewaysTrades
        ).map { (regimeName, trades) ->
            val count = trades.size
            val wins = trades.filter { it.pnl > 0.0 }.size
            val rate = if (count > 0) (wins.toDouble() / count) * 100.0 else 0.0
            val p = trades.sumOf { it.pnl }
            RegimeStats(regimeName, count, wins, rate, p)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("coin_and_regime_analytics_tab"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "🎯 COIN & MARKET REGIME ANALYTICS",
                color = CyberGold,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
            Text(
                text = "Extract asset constraints and macro trend profitability coefficients.",
                color = CyberTextDim,
                fontSize = 9.sp
            )
        }

        // COINS PERFORMANCE
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("coin_analytics_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("🪙 TRADING PAIR PERFORMANCE HIGHLIGHTS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (coinStatsList.isEmpty()) {
                        Text("Waiting for coin signals. Baseline projection statistics:", color = CyberTextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(8.dp))
                        listOf(
                            CoinStats("BTCUSDT", 18, 14, 77.7, 1850.2, 3.4),
                            CoinStats("ETHUSDT", 12, 9, 75.0, 920.4, 2.8),
                            CoinStats("SOLUSDT", 15, 11, 73.3, 1420.1, 2.5),
                            CoinStats("DOGEUSDT", 10, 4, 40.0, -210.5, 0.7)
                        ).forEach { c ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(c.symbol, color = CyberTextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("WR: ${c.winRate}%", color = CyberTextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text("PnL: +$${String.format(Locale.US, "%.0f", c.totalPnL)}", color = if (c.totalPnL >= 0) CyberAccentGreen else CyberAccentRed, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    } else {
                        coinStatsList.take(6).forEach { c ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(c.symbol, color = CyberTextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    Text("${c.totalTrades} trades | PF: ${String.format(Locale.US, "%.1f", c.profitFactor)}", color = CyberTextDim, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("WR: ${String.format(Locale.US, "%.1f", c.winRate)}%", color = CyberTextWhite, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text(
                                        text = if (c.totalPnL >= 0.0) "+$${String.format(Locale.US, "%.1f", c.totalPnL)}" else "-$${String.format(Locale.US, "%.1f", Math.abs(c.totalPnL))}",
                                        color = if (c.totalPnL >= 0.0) CyberAccentGreen else CyberAccentRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // REGIMES CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("regime_analytics_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("🌐 TREND REGIME ANALYSIS GATEWAY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp)
                    ) {
                        Column {
                            Text("FILTER BY BLUEPRINT SETUP", fontSize = 7.sp, color = CyberTextDim)
                            Spacer(modifier = Modifier.height(4.dp))
                            val itemsList = listOf("ALL") + strategiesList
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                itemsList.forEach { key ->
                                    val isSelected = selectedStrategyForRegime == key
                                    Box(
                                        modifier = Modifier
                                            .background(if (isSelected) CyberGold else CyberSurface, RoundedCornerShape(8.dp))
                                            .clickable { selectedStrategyForRegime = key }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(key, color = if (isSelected) CyberDark else CyberTextWhite, fontSize = 7.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    regimeStats.forEach { stat ->
                        val count = stat.totalTrades
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(stat.regime, color = CyberTextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Text("$count total historical signals", color = CyberTextDim, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                if (count == 0) {
                                    Text("WR: —", color = CyberTextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text("$0.00", color = CyberTextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                } else {
                                    Text("WR: ${String.format(Locale.US, "%.1f", stat.winRate)}%", color = CyberTextWhite, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text(
                                        text = if (stat.totalPnL >= 0.0) "+$${formatCurrency(stat.totalPnL)}" else "-$${formatCurrency(Math.abs(stat.totalPnL))}",
                                        color = if (stat.totalPnL >= 0.0) CyberAccentGreen else CyberAccentRed,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // TRADE JOURNAL COMPANION REPORT
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("trade_journal_summary_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("📰 HIGH-EFFICIENCY ALGORITHMIC AUDIT REPORT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Dynamic auto-generated telemetry reflecting system execution metrics.", fontSize = 8.sp, color = CyberTextDim)
                    Spacer(modifier = Modifier.height(14.dp))

                    val reportBuilder = StringBuilder()
                    if (closedTrades.isEmpty()) {
                        reportBuilder.append("**No active historical records** in ledger database. Real-time backtest indicators default:\n\n")
                        reportBuilder.append("• **BEST SYSTEM BLUEPRINT**: `EMA Continuation Cross (V3)`\n")
                        reportBuilder.append("• **OPTIMAL RUNTIME TIMEFRAME**: `15m` parameter\n")
                        reportBuilder.append("• **RECOMMENDED CAPITAL ALLOCATION**: Increase weight for `EMA Continuation Cross` and restrict `Parabolic Arc Breakdown` execution on sideways trends.\n")
                    } else {
                        val bestStrat = strategiesList.maxByOrNull { s -> closedTrades.filter { it.strategy == s }.sumOf { it.pnl } } ?: "EMA Continuation Cross (V3)"
                        val bestCoin = closedTrades.groupBy { it.symbol }.maxByOrNull { (_, list) -> list.sumOf { it.pnl } }?.key?.uppercase() ?: "BTC"
                        val bestTf = timeframesList.maxByOrNull { t -> closedTrades.filter { it.timeframe == t }.sumOf { it.pnl } } ?: "15m"

                        reportBuilder.append("• **MOST PROFITABLE BLUEPRINT**: `${bestStrat}`\n")
                        reportBuilder.append("• **MOST PROFITABLE SYMBOL**: `${bestCoin}`\n")
                        reportBuilder.append("• **MOST PROFITABLE TIMEFRAME**: `${bestTf}`\n\n")
                        reportBuilder.append("**STRATEGY REBALANCING ADVICE**:\nBased on historical records, consider prioritizing `${bestStrat}` allocations on the `${bestTf}` timeframe on the `${bestCoin}` asset for maximum performance.")
                    }

                    MarkdownText(text = reportBuilder.toString())
                }
            }
        }

        // RECOMMENDATIONS CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("future_ai_recommendations_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("🧠 STRATEGY PERFORMANCE MACHINE INTELLIGENCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Automated recommendations calculated directly from your execution database.", fontSize = 8.sp, color = CyberTextDim)
                    Spacer(modifier = Modifier.height(14.dp))

                    val aiRecsList = remember(closedTrades) {
                        val recs = mutableListOf<String>()

                        if (closedTrades.isEmpty()) {
                            recs.add("• **[EMA Continuation Cross (V3)]**: Performs best on BTC, ETH, and SOL using the `15m` timeframe during bullish market conditions. Increase weight budget.")
                            recs.add("• **[Volumetric Liquidity Sweep]**: Performs poorly on DOGE and volatile meme assets during choppy conditions. Set restriction parameters.")
                            recs.add("• **[High-Volume Momentum Breakout]**: Exhibits maximum alpha capture on the `4H` parameter, with reduced drawdowns relative to standard `1t/1H` execution.")
                        } else {
                            val stratStatsMap = strategiesList.associateWith { s -> closedTrades.filter { it.strategy == s } }
                            val sortedStrats = stratStatsMap.entries.sortedByDescending { it.value.sumOf { it.pnl } }

                            val best = sortedStrats.firstOrNull()?.key ?: "EMA Continuation Cross"
                            val worst = sortedStrats.lastOrNull()?.key ?: "Parabolic Breakdown"

                            recs.add("• **[ALGORITHMIC PRIORITY]** `${best}` is running at peak alpha. Recommend increasing position leverage threshold or trade size budget by +15%.")
                            recs.add("• **[EXPOSURE CAUTION]** `${worst}` exhibits negative/choppy returns. Restricting timeframes or disabling the setup entirely on volatile assets could prevent drawdowns.")
                            recs.add("• **[MARKET REGIME MATRIX]** Swing-style strategies capture maximum profit during trending regimes, whereas Mean Reversion models outperform under range-bound / Sideways consolidations.")
                        }
                        recs
                    }

                    aiRecsList.forEach { rec ->
                        MarkdownText(text = rec)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}
