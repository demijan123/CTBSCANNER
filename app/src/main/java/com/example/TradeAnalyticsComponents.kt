package com.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PaperTrade
import com.example.ui.AiCruncherMode
import com.example.ui.CryptoViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun shareToWhatsApp(context: Context, text: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.setPackage("com.whatsapp")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val businessIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                    setPackage("com.whatsapp.w4b")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(businessIntent)
            } catch (ex: Exception) {
                val baseIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                val chooser = Intent.createChooser(baseIntent, "Share Report via").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)
            }
        }
    } catch (t: Throwable) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("AI Advisory Report", text)
            clipboard.setPrimaryClip(clip)
            android.widget.Toast.makeText(context, "Sharing platform not active. Report copied to clipboard!", android.widget.Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            android.widget.Toast.makeText(context, "Error sharing: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}

internal fun shareTextSystem(context: Context, text: String, title: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(intent, "Share $title via").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    } catch (t: Throwable) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("AI Advisory Report", text)
            clipboard.setPrimaryClip(clip)
            android.widget.Toast.makeText(context, "Sharing platform not active. Report copied to clipboard!", android.widget.Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            android.widget.Toast.makeText(context, "Error sharing: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}

internal fun escapeCsvField(field: String): String {
    val clean = field.replace("\"", "\"\"")
    return if (clean.contains(",") || clean.contains("\n") || clean.contains("\"")) {
        "\"$clean\""
    } else {
        clean
    }
}

internal fun exportTradesToCsvAndShare(context: Context, openTrades: List<PaperTrade>, closedTrades: List<PaperTrade>) {
    val sb = StringBuilder()
    try {
        val allTrades = (openTrades + closedTrades).sortedByDescending { it.timestamp }
        val header = "Trade ID,Status,Symbol,Type,Strategy,Timeframe,Quantity,Invested Amount (USD),Entry Price (USD),Current Price (USD),Exit Price (USD),Realized P&L (USD),Execution Date,Exit Date,Justification\n"
        
        val dateFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        sb.append(header)
        
        for (tr in allTrades) {
            val row = listOf(
                tr.id.toString(),
                tr.status,
                tr.symbol.uppercase(),
                tr.signalType,
                tr.strategy,
                tr.timeframe,
                String.format(Locale.US, "%.6f", tr.quantity),
                String.format(Locale.US, "%.2f", tr.investedAmount),
                String.format(Locale.US, "%.4f", tr.entryPrice),
                String.format(Locale.US, "%.4f", tr.currentPrice),
                tr.exitPrice?.let { String.format(Locale.US, "%.4f", it) } ?: "",
                String.format(Locale.US, "%.2f", tr.pnl),
                dateFmt.format(Date(tr.timestamp)),
                tr.exitTimestamp?.let { dateFmt.format(Date(it)) } ?: "",
                tr.whyTradeReason
            )
            sb.append(row.joinToString(",") { escapeCsvField(it) }).append("\n")
        }
        
        val file = File(context.cacheDir, "crypto_trades_journal.csv")
        file.writeText(sb.toString())
        
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Crypto System Trade Journal Export")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "Share Journal Excel CSV via").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    } catch (e: Exception) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Crypto Trade Journal", sb.toString())
            clipboard.setPrimaryClip(clip)
            android.widget.Toast.makeText(context, "Sharing platform not active. CSV workbook copied to clipboard!", android.widget.Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            android.widget.Toast.makeText(context, "Export Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun MarkdownText(text: String, color: Color = CyberTextWhite) {
    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        while (currentIndex < text.length) {
            val boldStart = text.indexOf("**", currentIndex)
            if (boldStart == -1) {
                append(text.substring(currentIndex))
                break
            }
            append(text.substring(currentIndex, boldStart))
            val boldEnd = text.indexOf("**", boldStart + 2)
            if (boldEnd == -1) {
                append(text.substring(boldStart))
                break
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = CyberGold)) {
                append(text.substring(boldStart + 2, boldEnd))
            }
            currentIndex = boldEnd + 2
        }
    }
    Text(
        text = annotatedString,
        color = color,
        fontSize = 12.sp,
        fontFamily = FontFamily.Monospace,
        lineHeight = 16.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun MexcTradesTab(viewModel: CryptoViewModel, isDemo: Boolean) {
    LaunchedEffect(isDemo) {
        viewModel.validateAndRefreshMexcBalance(isDemo)
    }
    
    val openTradesAll by viewModel.openTrades.collectAsState()
    val closedTradesAll by viewModel.closedTrades.collectAsState()
    val mexcDemoBalance by viewModel.mexcDemoBalance.collectAsState()
    val mexcBalance by viewModel.mexcBalance.collectAsState()
    val mexcPnLBalancerEnabled by viewModel.mexcPnLBalancerEnabled.collectAsState()
    val mexcOpenTrades = openTradesAll.filter { tr -> tr.isMexcTrade && tr.isMexcDemoTrade == isDemo }
    val mexcClosedTrades = closedTradesAll.filter { tr -> tr.isMexcTrade && tr.isMexcDemoTrade == isDemo }

    var subTabSelection by remember { mutableStateOf(0) }
    val labelPrefix = if (isDemo) "DEMO SPOT" else "LIVE SPOT"

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("mexc_trades_${if (isDemo) "demo" else "live"}_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // --- Header summary statistics card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("mexc_pnl_summary_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("$labelPrefix ACCOUNT PERFORMANCE SUMMARY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val mexcRealized = mexcClosedTrades.sumOf { it.pnl }
                    val mexcUnrealized = mexcOpenTrades.sumOf { it.pnl }
                    val availableBalance = if (isDemo) mexcDemoBalance else mexcBalance
                    val netPortfolioValue = availableBalance + mexcUnrealized

                    Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("REALIZED HISTORIC P&L", fontSize = 8.sp, color = CyberTextDim)
                            Text("$${formatCurrency(mexcRealized)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (mexcRealized >= 0) CyberAccentGreen else CyberAccentRed)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("UNREALIZED FLOATING P&L", fontSize = 8.sp, color = CyberTextDim)
                            Text("$${formatCurrency(mexcUnrealized)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (mexcUnrealized >= 0) CyberAccentGreen else CyberAccentRed)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AVAILABLE BALANCE", fontSize = 8.sp, color = CyberTextDim)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier.size(14.dp).clickable {
                                        viewModel.validateAndRefreshMexcBalance(isDemo)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh Balance",
                                        tint = CyberAccentGreen,
                                        modifier = Modifier.size(10.dp).align(Alignment.Center)
                                    )
                                }
                            }
                            Text("$${formatCurrency(availableBalance)} USDT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberTextWhite)
                        }
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("NET PORTFOLIO VALUE", fontSize = 8.sp, color = CyberTextDim)
                            Text("$${formatCurrency(netPortfolioValue)} USDT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                        }
                    }
                }
            }
        }

        // --- SECURITY PRE-EMPTIVE PROFIT HARVESTER (P&L BALANCER) ---
        item {
            val mexcRealized = mexcClosedTrades.sumOf { it.pnl }
            val mexcUnrealized = mexcOpenTrades.sumOf { it.pnl }

            Card(
                modifier = Modifier.fillMaxWidth().testTag("mexc_pnl_balancer_card_${if (isDemo) "demo" else "live"}"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SECURITY PRE-EMPTIVE PROFIT HARVESTER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                            Text("Close profitable positions early to offset current negative realized P&L.", fontSize = 11.sp, color = CyberTextDim)
                        }
                        Switch(
                            checked = mexcPnLBalancerEnabled, onCheckedChange = { viewModel.setMexcPnLBalancerEnabled(it) },
                            colors = SwitchDefaults.colors(checkedTrackColor = CyberAccentGreen)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("REALIZED HISTORIC P&L", fontSize = 8.sp, color = CyberTextDim)
                            Text("$${formatCurrency(mexcRealized)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (mexcRealized >= 0) CyberAccentGreen else CyberAccentRed)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("UNREALIZED FLOATING P&L", fontSize = 8.sp, color = CyberTextDim)
                            Text("$${formatCurrency(mexcUnrealized)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (mexcUnrealized >= 0) CyberAccentGreen else CyberAccentRed)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.manualHarvestProfitTrades(isMexc = true, isDemo = isDemo) }, modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberSurface)
                    ) {
                        Text("FORCE HARVEST WINNING POSITIONS (WITH CONSENT)", color = CyberAccentGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- SUB TABS SWITCHER ---
        item {
            Row(modifier = Modifier.fillMaxWidth().background(CyberCard, RoundedCornerShape(12.dp)).padding(4.dp)) {
                Tab(
                    selected = subTabSelection == 0, onClick = { subTabSelection = 0 }, modifier = Modifier.weight(1f),
                    text = { Text("OPEN POSITIONS (${mexcOpenTrades.size})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (subTabSelection == 0) CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace) }
                )
                Tab(
                    selected = subTabSelection == 1, onClick = { subTabSelection = 1 }, modifier = Modifier.weight(1f),
                    text = { Text("CLOSED SIGNAL JOURNAL (${mexcClosedTrades.size})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (subTabSelection == 1) CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace) }
                )
            }
        }

        if (subTabSelection == 0) {
            if (mexcOpenTrades.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("No active open $labelPrefix positions currently trading.", color = CyberTextDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            } else {
                itemsIndexed(mexcOpenTrades, key = { index, tr -> "mexc_open_${tr.id}_$index" }) { index, tr ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CyberCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val safeSignalType = tr.signalType ?: "LONG"
                                    Text(safeSignalType, color = if (safeSignalType == "LONG") CyberAccentGreen else CyberAccentRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(tr.symbol.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyberTextWhite)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isDemo) "[DEMO]" else "[LIVE]", fontSize = 8.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                val pct = if (tr.entryPrice > 0.0) {
                                    ((tr.currentPrice - tr.entryPrice) / tr.entryPrice) * 100.0 * (if (tr.signalType == "LONG") 1 else -1)
                                } else {
                                    0.0
                                }
                                val pctText = if (pct >= 0.0) {
                                    "+" + String.format(java.util.Locale.US, "%.2f", pct) + "%"
                                } else {
                                    String.format(java.util.Locale.US, "%.2f", pct) + "%"
                                }
                                Text(
                                    text = pctText,
                                    color = if (tr.pnl >= 0) CyberAccentGreen else CyberAccentRed, fontSize = 12.sp, fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("ENTRY PRICE (EP)", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.entryPrice)}", fontSize = 11.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("STOP LOSS (SL)", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.stopLoss)}", fontSize = 11.sp, color = CyberAccentRed, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("TAKE PROFIT (TP)", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.takeProfit)}", fontSize = 11.sp, color = CyberAccentGreen, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("MARK PRICE", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.currentPrice)}", fontSize = 11.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("EXECUTION TIME", fontSize = 8.sp, color = CyberTextDim)
                                    val formattedTime = java.text.SimpleDateFormat("MMM dd, HH:mm:ss", java.util.Locale.US).format(java.util.Date(tr.timestamp))
                                    Text(formattedTime, fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("FLOATING PNL ($)", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.pnl)}", fontSize = 11.sp, color = if (tr.pnl >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "SETUP JUSTIFICATION: ${if (tr.whyTradeReason.isBlank()) "No manual justification specified." else tr.whyTradeReason}",
                                fontSize = 10.sp,
                                color = CyberTextDim,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.closePaperTradeManually(tr) },
                                modifier = Modifier.fillMaxWidth().height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("LIQUIDATE AND CLOSE POSITION", color = CyberAccentRed, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            if (mexcClosedTrades.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("No closed trade journal records in database.", color = CyberTextDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            } else {
                itemsIndexed(mexcClosedTrades, key = { index, tr -> "mexc_closed_${tr.id}_$index" }) { index, tr ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CyberCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val safeSignalType = tr.signalType ?: "LONG"
                                    Text(safeSignalType, color = if (safeSignalType == "LONG") CyberAccentGreen else CyberAccentRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(tr.symbol.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyberTextWhite)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isDemo) "[DEMO]" else "[LIVE]", fontSize = 8.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                val statusLabel = when (tr.status) {
                                    "CLOSED_TP" -> "TAKE PROFIT (TP)"
                                    "CLOSED_SL" -> "STOP LOSS (SL)"
                                    "CLOSED_MANUAL" -> "MANUALLY LIQUIDATED"
                                    else -> tr.status
                                }
                                Text(
                                    text = statusLabel,
                                    color = when (tr.status) {
                                        "CLOSED_TP" -> CyberAccentGreen
                                        "CLOSED_SL" -> CyberAccentRed
                                        else -> CyberGold
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("ENTRY VALUE", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.entryValue)}", fontSize = 10.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("EXIT VALUE", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.exitValue)}", fontSize = 10.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("GROSS PNL", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.grossPnl)}", fontSize = 10.sp, color = if (tr.grossPnl >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("ENTRY FEE", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.entryFee)}", fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("EXIT FEE", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.exitFee)}", fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("TOTAL FEES", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.totalFees)}", fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("ENTRY PRICE", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.entryPrice)}", fontSize = 10.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("EXIT PRICE", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.exitPrice ?: tr.currentPrice)}", fontSize = 10.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("NET PNL", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.netPnl)}", fontSize = 10.sp, color = if (tr.netPnl >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("CLOSED TIME", fontSize = 7.5.sp, color = CyberTextDim)
                                    val formattedTime = java.text.SimpleDateFormat("MMM dd, HH:mm:ss", java.util.Locale.US).format(java.util.Date(tr.exitTimestamp ?: tr.timestamp))
                                    Text(formattedTime, fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("ACTIVE PNL DISPLAY", fontSize = 7.5.sp, color = CyberTextDim)
                                    Text("$${formatCurrency(tr.pnl)}", fontSize = 11.sp, color = if (tr.pnl >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "JUSTIFICATION: ${if (tr.whyTradeReason.isBlank()) "No manual justification specified." else tr.whyTradeReason}",
                                fontSize = 10.sp,
                                color = CyberTextDim,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TradeAnalyticsTab(viewModel: CryptoViewModel) {
    val closedTrades by viewModel.closedTrades.collectAsState()
    val openTrades by viewModel.openTrades.collectAsState()
    val selectedAiMode by viewModel.selectedAiMode.collectAsState()
    val aiInsights by viewModel.aiInsights.collectAsState()
    val isGeneratingAiInsights by viewModel.isGeneratingAiInsights.collectAsState()
    val strategyTimeframeSettings by viewModel.strategyTimeframeSettings.collectAsState()
    val context = LocalContext.current

    val total = closedTrades.size
    val wins = closedTrades.filter { it.pnl > 0.0 }.size
    val winRate = if (total > 0) (wins.toDouble() / total) * 100.0 else 0.0
    val grossProfit = closedTrades.filter { it.pnl > 0.0 }.sumOf { it.pnl }
    val grossLoss = closedTrades.filter { it.pnl < 0.0 }.sumOf { Math.abs(it.pnl) }
    val profitFactor = if (grossLoss > 0) grossProfit / grossLoss else if (grossProfit > 0) Double.POSITIVE_INFINITY else 1.0
    val totalRevenue = closedTrades.sumOf { it.pnl }

    val strategiesList = listOf(
        "EMA Continuation Cross (V3)",
        "High-Volume Momentum Breakout",
        "Order Flow Imbalance (FVG Recovery)",
        "Mean Reversion & Oversold Bounce",
        "VWAP Deviation Band Mean Reversion",
        "Wyckoff Spring & Phase C Accumulation",
        "Institutional Order Block Grab",
        "MACD Divergence & Momentum Exhaustion",
        "Parabolic Arc Breakdown Squeeze",
        "Volumetric Liquidity Sweep",
        "Funding Rate Arbitrage Squeeze",
        "Weekly Pivot Resistance Rejection",
        "Order Flow Overexpansion (Bearish FVG)"
    )

    val timeframesList = listOf("5m", "15m", "30m", "1H", "4H")

    var activeAnalyticsSection by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = activeAnalyticsSection,
            containerColor = CyberDark,
            contentColor = CyberGold,
            edgePadding = 8.dp,
            indicator = { tabPositions ->
                if (activeAnalyticsSection in tabPositions.indices) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeAnalyticsSection]),
                        color = CyberGold
                    )
                }
            },
            divider = {}
        ) {
            val sections = listOf("🌐 CORE INTELLIGENCE", "📊 STRATEGY × TIMEFRAME", "🎯 COINS & REGIMES")
            sections.forEachIndexed { idx, label ->
                Tab(
                    selected = activeAnalyticsSection == idx,
                    onClick = { activeAnalyticsSection = idx },
                    text = { Text(label, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = if (activeAnalyticsSection == idx) CyberGold else CyberTextDim) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeAnalyticsSection) {
            0 -> {
                CoreIntelligenceSubTab(
                    viewModel = viewModel,
                    closedTrades = closedTrades,
                    openTrades = openTrades,
                    selectedAiMode = selectedAiMode,
                    aiInsights = aiInsights,
                    isGeneratingAiInsights = isGeneratingAiInsights,
                    context = context,
                    total = total,
                    winRate = winRate,
                    profitFactor = profitFactor,
                    totalRevenue = totalRevenue
                )
            }
            1 -> {
                StrategyTimeframeSubTab(
                    viewModel = viewModel,
                    closedTrades = closedTrades,
                    strategyTimeframeSettings = strategyTimeframeSettings,
                    strategiesList = strategiesList,
                    timeframesList = timeframesList
                )
            }
            2 -> {
                CoinsRegimesSubTab(
                    viewModel = viewModel,
                    closedTrades = closedTrades,
                    strategiesList = strategiesList,
                    timeframesList = timeframesList
                )
            }
        }
    }
}

@Composable
fun DummyDeadTab(viewModel: CryptoViewModel) {
    val closedTrades by viewModel.closedTrades.collectAsState()
    val openTrades by viewModel.openTrades.collectAsState()
    val selectedAiMode by viewModel.selectedAiMode.collectAsState()
    val aiInsights by viewModel.aiInsights.collectAsState()
    val isGeneratingAiInsights by viewModel.isGeneratingAiInsights.collectAsState()
    val context = LocalContext.current

    val total = closedTrades.size
    val wins = closedTrades.filter { it.pnl > 0.0 }.size
    val winRate = if (total > 0) (wins.toDouble() / total) * 100.0 else 0.0
    val grossProfit = closedTrades.filter { it.pnl > 0.0 }.sumOf { it.pnl }
    val grossLoss = closedTrades.filter { it.pnl < 0.0 }.sumOf { Math.abs(it.pnl) }
    val profitFactor = if (grossLoss > 0) grossProfit / grossLoss else if (grossProfit > 0) Double.POSITIVE_INFINITY else 1.0
    val totalRevenue = closedTrades.sumOf { it.pnl }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                fontSize = 12.sp
            )
            Text(
                text = "Dynamic performance analytics from historical system paper logs.",
                color = CyberTextDim,
                fontSize = 10.sp
            )
        }

        // --- Stats Grid ---
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
                        Text("${String.format(java.util.Locale.US, "%.1f", winRate)}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (winRate >= 50.0) CyberAccentGreen else CyberAccentRed)
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
                        val pfText = if (profitFactor == Double.POSITIVE_INFINITY) "MAX" else String.format(java.util.Locale.US, "%.2f", profitFactor)
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

        // --- DATA SHARING & COMPLIANCE GATEWAY ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("system_sharing_compliance_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
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

        // --- AI Advisory Segment ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ai_insight_optimization_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
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

                    // Mode Selection segmented row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        AiCruncherMode.values().forEach { mode ->
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
                                        AiCruncherMode.TRADE_ANALYSIS -> "PERF TELEMETRY"
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
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
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
