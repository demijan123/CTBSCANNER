package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.util.Locale
import androidx.compose.material.icons.filled.Close
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.activity.compose.BackHandler
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.data.local.AppDatabase
import com.example.data.local.SavedSignal
import com.example.data.local.PaperTrade
import com.example.data.model.Coin
import com.example.data.repository.CryptoRepository
import com.example.ui.CryptoViewModel
import com.example.ui.CryptoViewModelFactory
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

// --- Creative Palette Styling (Dynamic Theme Selection System) ---
enum class ThemeMode {
    LIGHT, DARK, BLUE, PURPLE
}

data class CyberPalette(
    val cyberDark: Color,
    val cyberSurface: Color,
    val cyberCard: Color,
    val cyberAccentGreen: Color,
    val cyberAccentRed: Color,
    val cyberGold: Color,
    val cyberSlate: Color,
    val cyberTextWhite: Color,
    val cyberTextDim: Color
)

val LightPalette = CyberPalette(
    cyberDark = Color(0xFFF7F9FF),
    cyberSurface = Color(0xFFE1E2EC),
    cyberCard = Color(0xFFFFFFFF),
    cyberAccentGreen = Color(0xFF005AC1),
    cyberAccentRed = Color(0xFFBA1A1A),
    cyberGold = Color(0xFF2E6094),
    cyberSlate = Color(0xFFDDE1FF),
    cyberTextWhite = Color(0xFF1A1C1E),
    cyberTextDim = Color(0xFF5E6272)
)

val DarkPalette = CyberPalette(
    cyberDark = Color(0xFF0C0E12),
    cyberSurface = Color(0xFF1A1D24),
    cyberCard = Color(0xFF222630),
    cyberAccentGreen = Color(0xFF4ADE80),
    cyberAccentRed = Color(0xFFEF4444),
    cyberGold = Color(0xFFF59E0B),
    cyberSlate = Color(0xFF2D3748),
    cyberTextWhite = Color(0xFFF3F4F6),
    cyberTextDim = Color(0xFF9CA3AF)
)

val BluePalette = CyberPalette(
    cyberDark = Color(0xFF0B192C),
    cyberSurface = Color(0xFF1E3E62),
    cyberCard = Color(0xFF1A3D6C),
    cyberAccentGreen = Color(0xFF00D2C4),
    cyberAccentRed = Color(0xFFFF4D4D),
    cyberGold = Color(0xFFFFD700),
    cyberSlate = Color(0xFF15305B),
    cyberTextWhite = Color(0xFFE2F1FF),
    cyberTextDim = Color(0xFF8AB4F8)
)

val PurplePalette = CyberPalette(
    cyberDark = Color(0xFF120320),
    cyberSurface = Color(0xFF2E0249),
    cyberCard = Color(0xFF3B0066),
    cyberAccentGreen = Color(0xFFCCFF00),
    cyberAccentRed = Color(0xFFFF007F),
    cyberGold = Color(0xFFE0AA3E),
    cyberSlate = Color(0xFF4B086B),
    cyberTextWhite = Color(0xFFF5E6FF),
    cyberTextDim = Color(0xFFC084FC)
)

var currentThemeMode by mutableStateOf(ThemeMode.DARK)

val activePalette: CyberPalette
    get() = when (currentThemeMode) {
        ThemeMode.LIGHT -> LightPalette
        ThemeMode.DARK -> DarkPalette
        ThemeMode.BLUE -> BluePalette
        ThemeMode.PURPLE -> PurplePalette
    }

val CyberDark: Color get() = activePalette.cyberDark
val CyberSurface: Color get() = activePalette.cyberSurface
val CyberCard: Color get() = activePalette.cyberCard
val CyberAccentGreen: Color get() = activePalette.cyberAccentGreen
val CyberAccentRed: Color get() = activePalette.cyberAccentRed
val CyberGold: Color get() = activePalette.cyberGold
val CyberSlate: Color get() = activePalette.cyberSlate
val CyberTextWhite: Color get() = activePalette.cyberTextWhite
val CyberTextDim: Color get() = activePalette.cyberTextDim

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup high-availability uncaught exception analytics and logging to local SharedPreferences
        val crashPrefs = getSharedPreferences("crash_reports", android.content.Context.MODE_PRIVATE)
        val initialCrash = crashPrefs.getString("last_crash", null)
        
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val writer = java.io.StringWriter()
            val printWriter = java.io.PrintWriter(writer)
            throwable.printStackTrace(printWriter)
            val stackTrace = writer.toString()
            
            android.util.Log.e("CRASH_DUMP", "Uncaught exception intercepted: $stackTrace", throwable)
            
            try {
                crashPrefs.edit()
                    .putString("last_crash", stackTrace)
                    .commit()
            } catch (e: Exception) {
                // Ignore fallback persist failure
            }
            
            defaultHandler?.uncaughtException(thread, throwable)
        }

        enableEdgeToEdge()

        if (BuildConfig.DEBUG) {
            val builder = android.os.StrictMode.ThreadPolicy.Builder()
                .detectNetwork()
                .penaltyLog()
            
            val isRobolectricTest = try {
                Class.forName("org.robolectric.Robolectric")
                true
            } catch (e: Exception) {
                false
            }
            
            if (!isRobolectricTest) {
                builder.detectDiskReads().detectDiskWrites()
            }
            
            android.os.StrictMode.setThreadPolicy(builder.build())
        }

        val db = AppDatabase.getDatabase(this)
        val repository = CryptoRepository(db.coinDao(), db.paperTradeDao())
        val factory = CryptoViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[CryptoViewModel::class.java]

        setContent {
            MyApplicationTheme {
                var lastCrash by remember { mutableStateOf(initialCrash) }
                
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CyberDark),
                    containerColor = CyberDark
                ) { innerPadding ->
                    MainDesktopDashboard(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                    
                    if (lastCrash != null) {
                        androidx.compose.ui.window.Dialog(
                            onDismissRequest = {
                                crashPrefs.edit().clear().apply()
                                lastCrash = null
                            }
                        ) {
                            androidx.compose.material3.Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.85f),
                                shape = RoundedCornerShape(12.dp),
                                color = CyberSurface,
                                border = androidx.compose.foundation.BorderStroke(2.dp, CyberAccentRed)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🚨 SYSTEM DIAGNOSTICS DEVIATION",
                                            color = CyberAccentRed,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        IconButton(
                                            onClick = {
                                                crashPrefs.edit().clear().apply()
                                                lastCrash = null
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Dismiss",
                                                tint = CyberTextDim
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "An uncaught runtime exception has occurred. Please review the detailed telemetry dump beneath:",
                                        fontSize = 11.sp,
                                        color = CyberTextWhite
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                            .padding(8.dp)
                                    ) {
                                        val scrollState = androidx.compose.foundation.rememberScrollState()
                                        Text(
                                            text = lastCrash ?: "",
                                            color = CyberAccentRed,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .verticalScroll(scrollState)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(containerColor = CyberAccentRed),
                                            onClick = {
                                                crashPrefs.edit().clear().apply()
                                                lastCrash = null
                                            }
                                        ) {
                                            Text("RESET & RECOVERY", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
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
}

@Composable
fun MainDesktopDashboard(
    viewModel: CryptoViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val latestBacktestResults = remember { mutableStateMapOf<String, SimulationResult>() }
    val tabTitles = listOf("MARKET SCANNER", "CONFIRMED SIGNALS", "WATCHLIST", "BLUEPRINTS", "AUTO BOT", "PAPER TRADING", "MEXC CONFIG", "MEXC DEMO TRADES", "MEXC LIVE TRADES", "TRADE ANALYTICS")
    var selectedCoinForDetails by remember { mutableStateOf<Coin?>(null) }
    var showExitNotice by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.activity.compose.BackHandler(enabled = true) {
        if (selectedCoinForDetails != null) {
            selectedCoinForDetails = null
        } else if (selectedTab != 0) {
            selectedTab = 0
        } else {
            showExitNotice = true
            (context as? android.app.Activity)?.moveTaskToBack(true)
        }
    }

    val scannedCoins by viewModel.scannedCoins.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val scanLogs by viewModel.scanLogs.collectAsState()
    val error by viewModel.error.collectAsState()

    val confirmedSignals by viewModel.activeConfirmedSignals.collectAsState()
    val bookmarkedSignals by viewModel.bookmarkedSignals.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDark)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- Header Status Bar ---
            HeaderStatusBar(
                isScanning = isScanning,
                isKeyConfigured = viewModel.isModelKeyConfigured,
                activeTabTitle = tabTitles[selectedTab],
                onForceRefresh = { viewModel.forceFullRefresh() }
            )

            // --- Active Scan Progress Indicators ---
            if (isScanning) {
                ScannerLiveProgressOverlay(
                    progress = scanProgress,
                    logs = scanLogs
                )
            }

            // --- Navigation Tab Selection ---
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = CyberSurface,
                contentColor = CyberTextWhite,
                edgePadding = 12.dp,
                indicator = { tabPositions ->
                    if (selectedTab in tabPositions.indices) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = CyberAccentGreen
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nav_tabs")
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    val icon = when (index) {
                        0 -> Icons.Default.Search       // MARKET SCANNER
                        1 -> Icons.Default.CheckCircle   // CONFIRMED SIGNALS
                        2 -> Icons.Default.Favorite      // WATCHLIST
                        3 -> Icons.Default.Build         // BLUEPRINTS
                        4 -> Icons.Default.PlayArrow     // AUTO BOT
                        5 -> Icons.Default.List          // PAPER TRADING
                        6 -> Icons.Default.Settings      // MEXC CONFIG
                        7 -> Icons.Default.Star          // MEXC DEMO TRADES
                        8 -> Icons.Default.ShoppingCart  // MEXC LIVE TRADES
                        9 -> Icons.Default.Share         // TRADE ANALYTICS
                        else -> Icons.Default.Info
                    }
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        modifier = Modifier.testTag("tab_item_$index"),
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) CyberAccentGreen else CyberTextDim,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) CyberAccentGreen else CyberTextDim,
                                letterSpacing = 0.5.sp,
                                maxLines = 1
                            )
                        }
                    )
                }
            }



            // --- Active Tab Screen Router ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
            ) {
                when (selectedTab) {
                    0 -> ScannerMarketTab(
                        viewModel = viewModel,
                        coins = scannedCoins,
                        confirmedSignals = confirmedSignals,
                        isScanning = isScanning,
                        onScanClick = { viewModel.startFullMarketScan() },
                        onAnalyzeItem = { coin ->
                            selectedCoinForDetails = coin
                        }
                    )
                    1 -> ConfirmedSignalsTab(
                        viewModel = viewModel,
                        signals = confirmedSignals,
                        onBookmarkToggle = { viewModel.toggleBookmark(it) },
                        onDismiss = { viewModel.deleteSignal(it) },
                        emptyText = "No active trade signals. Click 'MARKET SCANNER' and hit 'START ANALYSIS ROUTINE' to scan microcaps for breakout targets."
                    )
                    2 -> ConfirmedSignalsTab(
                        viewModel = viewModel,
                        signals = bookmarkedSignals,
                        onBookmarkToggle = { viewModel.toggleBookmark(it) },
                        onDismiss = { viewModel.deleteSignal(it) },
                        emptyText = "Your Bookmarked signals live here. Tap the Bookmark icon on any confirmed signal inside active trades to preserve it."
                    )
                    3 -> StrategyBlueprintsTab(viewModel = viewModel, latestBacktestResults = latestBacktestResults)
                    4 -> AutoBotTradingConsoleTab(viewModel = viewModel)
                    5 -> PaperTradingPortfolioTab(viewModel = viewModel)
                    6 -> MexcTradingConsoleTab(viewModel = viewModel)
                    7 -> MexcTradesTab(viewModel = viewModel, isDemo = true)
                    8 -> MexcTradesTab(viewModel = viewModel, isDemo = false)
                    9 -> TradeAnalyticsTab(viewModel = viewModel)
                }
            }

            // --- Tiny Footer ---
            ThinFooterNotice()
        }

        selectedCoinForDetails?.let { coin ->
            CoinCredentialsOverlay(
                coin = coin,
                onDismiss = { selectedCoinForDetails = null }
            )
        }

        if (showExitNotice) {
            LaunchedEffect(showExitNotice) {
                kotlinx.coroutines.delay(3000L)
                showExitNotice = false
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .border(1.dp, CyberAccentGreen, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberCard.copy(alpha = 0.95f))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Active Status",
                            tint = CyberAccentGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TERMINAL ONLINE • Session remains active in background.",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextWhite,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderStatusBar(
    isScanning: Boolean,
    isKeyConfigured: Boolean,
    activeTabTitle: String,
    onForceRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberDark)
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activeTabTitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CyberAccentGreen,
                letterSpacing = 2.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(if (isScanning) Color(0xFF22C55E) else CyberTextDim, CircleShape)
                )
                Text(
                    text = if (isScanning) "LIVE SCANNING" else "SCAN INDEXED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberTextDim,
                    letterSpacing = 1.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "CTB",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = CyberTextWhite,
                    letterSpacing = (-1.5).sp,
                    lineHeight = 36.sp
                )
                Text(
                    text = "SIGNAL",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = CyberAccentGreen,
                    letterSpacing = (-1.5).sp,
                    lineHeight = 36.sp
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberSlate)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isKeyConfigured) "AI ENHANCED" else "QUANT ENGINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberAccentGreen
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onForceRefresh,
                    modifier = Modifier
                        .size(36.dp)
                        .background(CyberCard, CircleShape)
                        .border(1.dp, CyberSurface, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Force scan refresh",
                        tint = CyberAccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "THEME ENVELOPE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = CyberTextDim,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeMode.values().forEach { mode ->
                    val isModeSelected = currentThemeMode == mode
                    val (modeName, accentClr) = when (mode) {
                        ThemeMode.LIGHT -> Pair("LIGHT", Color(0xFF005AC1))
                        ThemeMode.DARK -> Pair("DARK", Color(0xFF4ADE80))
                        ThemeMode.BLUE -> Pair("BLUE", Color(0xFF00D2C4))
                        ThemeMode.PURPLE -> Pair("PURPLE", Color(0xFFCCFF00))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isModeSelected) accentClr.copy(alpha = 0.15f) else Color.Transparent)
                            .border(
                                1.dp,
                                if (isModeSelected) accentClr else CyberSurface,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { currentThemeMode = mode }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("theme_btn_${mode.name.lowercase()}")
                    ) {
                        Text(
                            text = modeName,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isModeSelected) accentClr else CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScannerLiveProgressOverlay(
    progress: Float,
    logs: List<String>
) {
    val lazyListState = rememberLazyListState()

    // Automatically scrolls logs to bottom as they receive triggers
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            try {
                kotlinx.coroutines.delay(50L)
                lazyListState.scrollToItem(logs.size - 1)
            } catch (e: Exception) {
                // Safely ignore scroll interruptions on content reset
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberSurface),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSlate)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = CyberAccentGreen,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "RADAR ANALYSIS RUNNING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = CyberAccentGreen,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = if (progress.isNaN() || progress.isInfinite()) 0f else progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = CyberAccentGreen,
                trackColor = CyberSlate
            )
            Spacer(modifier = Modifier.height(10.dp))
            
            // Console display box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(CyberDark, RoundedCornerShape(4.dp))
                    .padding(6.dp)
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(logs) { log ->
                        Text(
                            text = log,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = if (log.contains("🟢")) CyberAccentGreen else if (log.contains("🛑")) CyberAccentRed else CyberGold,
                            lineHeight = 12.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerMarketTab(
    viewModel: CryptoViewModel,
    coins: List<Coin>,
    confirmedSignals: List<SavedSignal>,
    isScanning: Boolean,
    onScanClick: () -> Unit,
    onAnalyzeItem: (Coin) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCoins = remember(searchQuery, coins) {
        val rawFiltered = if (searchQuery.trim().isEmpty()) coins else {
            coins.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.symbol.contains(searchQuery, ignoreCase = true)
            }
        }
        rawFiltered.distinctBy { it.id }
    }

    val selectedTier by viewModel.selectedMarketCapTier.collectAsState()
    val customMin by viewModel.customMinCap.collectAsState()
    val customMax by viewModel.customMaxCap.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CyberCard),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "VOLATILITY DISCOVERY",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = CyberTextWhite,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sweep different market cap tiers with high volatility divergence to extract high-conviction trade setups.",
                    fontSize = 12.sp,
                    color = CyberTextDim,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                
                // Beautiful Market Cap Tier Selector (Interactive Selector)
                Text(
                    text = "MARKET CAP TIER FILTER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberTextDim,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    com.example.ui.MarketCapTier.values().forEach { tier ->
                        val isSelected = (selectedTier == tier)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyberSlate else CyberDark)
                                .border(1.dp, if (isSelected) CyberAccentGreen else CyberSurface, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setMarketCapTier(tier) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = when (tier) {
                                        com.example.ui.MarketCapTier.LOW -> "LOW"
                                        com.example.ui.MarketCapTier.MID -> "MID"
                                        com.example.ui.MarketCapTier.HIGH -> "HIGH"
                                        com.example.ui.MarketCapTier.CUSTOM -> "CUSTOM"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) CyberAccentGreen else CyberTextWhite
                                )
                                Text(
                                    text = when (tier) {
                                        com.example.ui.MarketCapTier.LOW -> "50M - 200M"
                                        com.example.ui.MarketCapTier.MID -> "200M - 2B"
                                        com.example.ui.MarketCapTier.HIGH -> "2B+"
                                        com.example.ui.MarketCapTier.CUSTOM -> "Range"
                                    },
                                    fontSize = 9.sp,
                                    color = if (isSelected) CyberAccentGreen.copy(alpha = 0.7f) else CyberTextDim
                                )
                            }
                        }
                    }
                }

                // Custom limit editors
                if (selectedTier == com.example.ui.MarketCapTier.CUSTOM) {
                    var minInput by remember(selectedTier) { mutableStateOf((customMin / 1_000_000.0).toInt().toString()) }
                    var maxInput by remember(selectedTier) { mutableStateOf(if (customMax >= Double.MAX_VALUE || customMax.isInfinite() || customMax.isNaN()) "" else (customMax / 1_000_000.0).toInt().toString()) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = minInput,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.filter { it.isDigit() }.take(9)
                                minInput = digitsOnly
                                val parsedMin = digitsOnly.toDoubleOrNull() ?: 0.0
                                val parsedMax = maxInput.toDoubleOrNull() ?: Double.MAX_VALUE
                                viewModel.setCustomMarketCapRange(parsedMin * 1_000_000.0, parsedMax * 1_000_000.0)
                            },
                            label = { Text("Min Cap ($ Million)", fontSize = 10.sp, color = CyberTextDim) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark,
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = maxInput,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.filter { it.isDigit() }.take(9)
                                maxInput = digitsOnly
                                val parsedMin = minInput.toDoubleOrNull() ?: 0.0
                                val parsedMax = if (digitsOnly.isEmpty()) Double.MAX_VALUE else (digitsOnly.toDoubleOrNull() ?: Double.MAX_VALUE)
                                viewModel.setCustomMarketCapRange(parsedMin * 1_000_000.0, parsedMax * 1_000_000.0)
                            },
                            label = { Text("Max Cap ($ Million)", fontSize = 10.sp, color = CyberTextDim) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark,
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface
                            ),
                            singleLine = true
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_input")
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Filter asset classes...", fontSize = 12.sp, color = CyberTextDim) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon", tint = CyberTextDim, modifier = Modifier.size(16.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CyberTextWhite,
                            unfocusedTextColor = CyberTextWhite,
                            focusedContainerColor = CyberDark,
                            unfocusedContainerColor = CyberDark,
                            focusedBorderColor = CyberAccentGreen,
                            unfocusedBorderColor = CyberSurface
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = onScanClick,
                        enabled = !isScanning,
                        modifier = Modifier
                            .testTag("scan_button")
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberAccentGreen,
                            contentColor = CyberDark,
                            disabledContainerColor = CyberSlate
                        )
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start scanner")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isScanning) "SCAN IN RUN" else "START SCAN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid Headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ASSET DETAILS", fontSize = 10.sp, color = CyberTextDim, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.8f))
            Text(text = "PRICE", fontSize = 10.sp, color = CyberTextDim, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            Text(text = "MARKET CAP", fontSize = 10.sp, color = CyberTextDim, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
            Text(text = "ACTION", fontSize = 10.sp, color = CyberTextDim, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }

        if (filteredCoins.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Empty icon",
                        tint = CyberSlate,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No lowcaps indexed. Trigger 'START SCAN' above.",
                        fontSize = 12.sp,
                        color = CyberTextDim,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("scanned_coins_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filteredCoins, key = { index, coin -> "coin_${coin.id}_$index" }) { index, coin ->
                    val hasSignal = confirmedSignals.any { it.id == coin.id }
                    val currentSignal = confirmedSignals.find { it.id == coin.id }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAnalyzeItem(coin) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberCard),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (hasSignal) {
                                if (currentSignal?.signal == "LONG") CyberAccentGreen else CyberAccentRed
                            } else CyberSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left Details
                            Row(
                                modifier = Modifier.weight(1.8f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = coin.image,
                                    contentDescription = "${coin.name} logo",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(CyberDark)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = coin.symbol.uppercase(),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberTextWhite
                                        )
                                        if (hasSignal) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        if (currentSignal?.signal == "LONG") CyberAccentGreen else CyberAccentRed,
                                                        RoundedCornerShape(3.dp)
                                                    )
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = currentSignal?.signal ?: "",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = CyberDark
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = coin.name,
                                        fontSize = 10.sp,
                                        color = CyberTextDim,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // Price details
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "$${formatPrice(coin.currentPrice)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextWhite
                                )
                                val pChange = coin.priceChangePercentage24h ?: 0.0
                                Text(
                                    text = "${if (pChange >= 0) "+" else ""}${String.format(java.util.Locale.US, "%.2f", pChange)}%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (pChange >= 0) CyberAccentGreen else CyberAccentRed
                                )
                            }

                            // Market Cap details
                            Column(
                                modifier = Modifier.weight(1.2f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "$${formatLargeNumber(coin.marketCap)}",
                                    fontSize = 12.sp,
                                    color = CyberTextWhite,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Vol: $${formatLargeNumber(coin.totalVolume ?: 0.0)}",
                                    fontSize = 9.sp,
                                    color = CyberTextDim
                                )
                            }

                            // Action button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(CyberDark, RoundedCornerShape(8.dp))
                                        .border(1.dp, CyberSurface, RoundedCornerShape(8.dp))
                                        .clickable { onAnalyzeItem(coin) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Inspect item indicators",
                                        tint = CyberAccentGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmedSignalsTab(
    viewModel: CryptoViewModel,
    signals: List<SavedSignal>,
    onBookmarkToggle: (SavedSignal) -> Unit,
    onDismiss: (SavedSignal) -> Unit,
    emptyText: String
) {
    if (signals.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Empty active list",
                    tint = CyberSlate,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No High-Confirmation Signals Found",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberTextWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = emptyText,
                    fontSize = 11.sp,
                    color = CyberTextDim,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("confirmed_signals_list"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(signals, key = { index, signal -> "signal_${signal.id}_$index" }) { index, signal ->
                SignalTriggerCard(
                    viewModel = viewModel,
                    signal = signal,
                    onBookmarkToggle = { onBookmarkToggle(signal) },
                    onDismiss = { onDismiss(signal) }
                )
            }
        }
    }
}

@Composable
fun SignalTriggerCard(
    viewModel: CryptoViewModel,
    signal: SavedSignal,
    onBookmarkToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isBuy = signal.signal == "LONG"

    // Theme properties mapped dynamically to enforce Bold Typography spec
    val cardBg = if (isBuy) CyberCard else Color(0xFF1A1C1E)
    val textPrimary = if (isBuy) CyberTextWhite else Color.White
    val textSecondary = if (isBuy) CyberTextDim else Color(0xFF8E919E)
    val capBgColor = if (isBuy) Color(0xFFDCFCE7) else Color(0xFFFFDADA)
    val capTextColor = if (isBuy) Color(0xFF15803D) else Color(0xFF93000A)
    val targetRowBg = if (isBuy) CyberDark else Color(0xFF2E3033)
    val borderColor = if (isBuy) CyberSurface else Color(0xFF32353D)
    val actionButtonBg = if (isBuy) CyberDark else Color(0xFF2E3033)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("signal_card_${signal.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(32.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // --- Card Top Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = signal.image,
                        contentDescription = "${signal.name} icon",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(targetRowBg)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "$${signal.symbol.uppercase()}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = textPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Market Cap: $${formatLargeNumber(signal.marketCap)}",
                            fontSize = 11.sp,
                            color = textSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Badge (Dynamic text mapping from design guidelines)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(capBgColor)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (isBuy) "Highly Confirmed" else "Strategy Match",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = capTextColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onBookmarkToggle,
                        modifier = Modifier
                            .size(32.dp)
                            .background(actionButtonBg, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (signal.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Pick bookmark",
                            tint = if (signal.isBookmarked) CyberAccentRed else textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .background(actionButtonBg, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Dismiss trade setup",
                            tint = textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Large Typography Trade Direction & Rationale ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = if (isBuy) "LONG" else "SHORT",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isBuy) Color(0xFF161E2E) else Color(0xFFFFB4AB),
                        lineHeight = 36.sp,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = signal.strategy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${signal.confidence}%",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = textPrimary,
                        lineHeight = 30.sp
                    )
                    Text(
                        text = "Confidence",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- Strategy & Targets Block ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(targetRowBg, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Text(text = "STRATEGY THRESHOLD", fontSize = 8.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = signal.strategy, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Column(modifier = Modifier.weight(0.8f), horizontalAlignment = Alignment.End) {
                    Text(text = "ENTRY POINT", fontSize = 8.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "$${formatPrice(signal.currentPrice)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }

                Column(modifier = Modifier.weight(0.8f), horizontalAlignment = Alignment.End) {
                    Text(text = "STOP LOSS", fontSize = 8.sp, color = CyberAccentRed, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "$${formatPrice(signal.stopLoss)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberAccentRed)
                }

                Column(modifier = Modifier.weight(0.8f), horizontalAlignment = Alignment.End) {
                    Text(text = "TAKE PROFIT", fontSize = 8.sp, color = if (isBuy) Color(0xFF15803D) else CyberAccentGreen, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "$${formatPrice(signal.takeProfit)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isBuy) Color(0xFF15803D) else CyberAccentGreen)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- Spline Chart Area On Canvas ---
            TradeChartSpline(
                signal = signal,
                isBuy = isBuy
            )

            // --- Rationale Expansion ---
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (expanded) "HIDE AI SIGNAL ANALYSIS" else "SHOW DIVERGENT TRADE RATIONALE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isBuy) CyberAccentGreen else Color(0xFFFFDADA),
                    letterSpacing = 0.5.sp
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "TRADE SYNTHESIS THESIS:",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBuy) CyberGold else Color(0xFFFFB4AB),
                        letterSpacing = 0.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = signal.rationale,
                        fontSize = 12.sp,
                        color = textPrimary,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .background(targetRowBg, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val cashBalance by viewModel.cashBalance.collectAsState()
            var tradeAmountInput by remember { mutableStateOf("1000") }
            var isTradeSetupVisible by remember { mutableStateOf(false) }

            if (!isTradeSetupVisible) {
                Button(
                    onClick = { isTradeSetupVisible = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBuy) CyberAccentGreen else CyberAccentRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("open_paper_trade_setup_${signal.id}"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Run Trade",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EXECUTE LIVE PAPER TRADE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = targetRowBg),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().testTag("paper_trade_setup_${signal.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "PAPER POSITION CONFIGURATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = textSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("USD CAPITAL SIZE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("Available: $${formatCurrency(cashBalance)}", fontSize = 11.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        OutlinedTextField(
                            value = tradeAmountInput,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.filter { it.isDigit() }.take(8)
                                tradeAmountInput = digitsOnly
                            },
                            modifier = Modifier.fillMaxWidth().testTag("paper_trade_amount_input_${signal.id}"),
                            placeholder = { Text("1000", color = textSecondary.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (isBuy) CyberAccentGreen else CyberAccentRed,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { isTradeSetupVisible = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = textSecondary),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("CANCEL", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val amount = tradeAmountInput.toDoubleOrNull() ?: 1000.0
                                    val success = viewModel.executePaperTrade(
                                        coinId = signal.id,
                                        symbol = signal.symbol,
                                        name = signal.name,
                                        image = signal.image,
                                        signalType = signal.signal,
                                        entryPrice = signal.currentPrice,
                                        stopLoss = signal.stopLoss,
                                        takeProfit = signal.takeProfit,
                                        investedAmount = amount,
                                        strategy = signal.strategy
                                    )
                                    if (success) {
                                        isTradeSetupVisible = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isBuy) CyberAccentGreen else CyberAccentRed,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.weight(1.5f).testTag("confirm_paper_trade_btn_${signal.id}"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("CONFIRM TRADE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TradeChartSpline(
    signal: SavedSignal,
    isBuy: Boolean
) {
    val splineColor = if (isBuy) CyberAccentGreen else CyberAccentRed
    
    // Cache the trend point fractions on signal ID/direction updates
    val pointFractions = remember(signal.id, isBuy) {
        val fractions = ArrayList<Offset>()
        val pointsCount = 10
        val seedString = signal.id
        val charSum = seedString.sumOf { it.code }
        val generator = java.util.Random(charSum.toLong())
        val baselineOffset = if (isBuy) 0.6f else 0.4f

        for (i in 0 until pointsCount) {
            val xFraction = i.toFloat() / (pointsCount - 1).toFloat()
            val floatVolatility = generator.nextFloat() * 0.35f
            val sineMod = Math.sin(i.toDouble() * 1.2).toFloat() * 0.15f
            val deltaTrend = if (isBuy) (i.toFloat() / pointsCount.toFloat()) * 0.3f else -(i.toFloat() / pointsCount.toFloat()) * 0.3f
            val yFraction = (baselineOffset - deltaTrend + floatVolatility + sineMod).coerceIn(0.1f, 0.9f)
            fractions.add(Offset(xFraction, yFraction))
        }
        fractions
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(CyberDark, RoundedCornerShape(6.dp))
            .border(1.dp, CyberSlate.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            if (width <= 0f || height <= 0f) return@Canvas

            // Project fractions onto current canvas size
            val points = pointFractions.map { Offset(it.x * width, it.y * height) }

            // Create Bezier Path
            val path = Path()
            path.moveTo(points[0].x, points[0].y)
            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                val controlX = (p0.x + p1.x) / 2
                path.cubicTo(
                    controlX, p0.y,
                    controlX, p1.y,
                    p1.x, p1.y
                )
            }

            // Fill area under spline
            val fillPath = Path()
            fillPath.addPath(path)
            fillPath.lineTo(width, height)
            fillPath.lineTo(0f, height)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(splineColor.copy(alpha = 0.15f), Color.Transparent),
                    startY = 0f,
                    endY = height
                )
            )

            // Draw final smooth line
            drawPath(
                path = path,
                color = splineColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw indicator lines for Entry (dotted) or levels
            val entryY = points.last().y
            drawLine(
                color = CyberTextDim.copy(alpha = 0.5f),
                start = Offset(0f, entryY),
                end = Offset(width, entryY),
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw coordinate circle on current end point
            drawCircle(
                color = splineColor,
                radius = 4.dp.toPx(),
                center = points.last()
            )
            drawCircle(
                color = CyberTextWhite,
                radius = 2.dp.toPx(),
                center = points.last()
            )
        }
    }
}

@Composable
fun StrategyBlueprintsTab(viewModel: CryptoViewModel, latestBacktestResults: MutableMap<String, SimulationResult>) {
    var selectedStrategy by remember { mutableStateOf<StrategyBlueprint?>(null) }

    val currentStrategy = selectedStrategy
    if (currentStrategy != null) {
        BacktestSimulatorScreen(
            strategy = currentStrategy,
            viewModel = viewModel,
            onResultReady = { res -> latestBacktestResults[currentStrategy.title] = res }
        ) {
            selectedStrategy = null
        }
    } else {
        var directionFilter by remember { mutableStateOf("ALL") }
        var conditionFilter by remember { mutableStateOf("ALL") }

        val blueprints = listOf(
            StrategyBlueprint(
                title = "EMA Continuation Cross (V3)",
                trend = "Bullish Outperformance",
                metrics = "50 EMA / 200 EMA breakout threshold",
                description = "Analyzes high-volume support consolidating slightly above the 50 Exponential Moving Average. On lower cap parameters, large liquidity pools accumulate positions, predicting immediate swing continuation.",
                direction = "LONG",
                marketCondition = "Trending",
                premiumTag = "CTA Trend Engine"
            ),
            StrategyBlueprint(
                title = "High-Volume Momentum Breakout",
                trend = "Bullish Momentum Continuation",
                metrics = "High 24h gain + Relative volume > 1.5",
                description = "Capitalizes on momentum expansion above critical resistance envelopes. Sustained volume spikes signal high-density institutional flow, driving a strong momentum surge toward overhead liquidity levels. Highly effective for rapid trend riders.",
                direction = "LONG",
                marketCondition = "Trending",
                premiumTag = "HFT Breakout Tracker"
            ),
            StrategyBlueprint(
                title = "Order Flow Imbalance (FVG Recovery)",
                trend = "Bullish Imbalance Rebound",
                metrics = "Fair Value Gap touch + 0.5 discount level",
                description = "Smart Money Concept (SMC) protocol. Monitors weekly and daily liquidity voids. Triggers buy limits upon algorithmic retests of inefficient price zones, predicting quick absorption and swift upwards re-pricing.",
                direction = "LONG",
                marketCondition = "Trending",
                premiumTag = "SMC Imbalance Filler"
            ),
            StrategyBlueprint(
                title = "Mean Reversion & Oversold Bounce",
                trend = "Oversold Rebound Pivot",
                metrics = "RSI-14 values < 25 + Daily retracement > -10%",
                description = "Focuses on premium, high-utility projects suffering cascading liquidations. When capitalization areas hit support bottoms on exhausting volume, it triggers low-risk, high-velocity reverse-bounce plays.",
                direction = "LONG",
                marketCondition = "Choppy",
                premiumTag = "Quant Reversion Engine"
            ),
            StrategyBlueprint(
                title = "VWAP Deviation Band Mean Reversion",
                trend = "Statistical Range Bottom",
                metrics = "Price < -2.0 VWAP SD band expansion",
                description = "Used extensively by institutional block desks. Identifies temporary asset dislocations from the volume-weighted average price in rangebound environments, forecasting swift return to mean equilibrium.",
                direction = "LONG",
                marketCondition = "Choppy",
                premiumTag = "Statistical Arbitrage Core"
            ),
            StrategyBlueprint(
                title = "Wyckoff Spring & Phase C Accumulation",
                trend = "Bullish Phase C Markup",
                metrics = "Consolidation sweep + Volume > 1.8x average",
                description = "Models structural market transitions by identifying early markup. Detects a quick downward flush (the 'Spring') that sweeps low liquidity stops, immediately followed by strong buying volume that reclaims the trading range, initiating a high-accuracy upward advance.",
                direction = "LONG",
                marketCondition = "Sideways",
                premiumTag = "Smart Money Concepts"
            ),
            StrategyBlueprint(
                title = "Institutional Order Block Grab",
                trend = "Bullish Order Reconstruction",
                metrics = "Historical demand re-test + Volumetric support",
                description = "Tracks historical institutional demand zones on 4-hour charts. When price touches a major discount order block, limit order clusters of massive size are triggered. This shields the trader with an exceptionally tight, low-risk stop-loss and premium entry precision.",
                direction = "LONG",
                marketCondition = "Sideways",
                premiumTag = "Hedge Fund Order Block"
            ),
            StrategyBlueprint(
                title = "MACD Divergence & Momentum Exhaustion",
                trend = "Bearish Multi-drive Divergence",
                metrics = "Lower MACD highs + Higher Price highs",
                description = "An early-warning indicator for structural trend reversals. Recognizes instances where the price presses new intraday highs but the MACD momentum histogram exhibits consecutive lower peaks, warning of severe upward exhaustion and highly profitable short pivots.",
                direction = "SHORT",
                marketCondition = "Trending",
                premiumTag = "Momentum Divergence Key"
            ),
            StrategyBlueprint(
                title = "Parabolic Arc Breakdown Squeeze",
                trend = "Bearish Trend Cascade",
                metrics = "Parabolic slope leak + 20 SMA break",
                description = "Detects systemic momentum failures in highly over-extended parabolics. Popularized by macro CTA desks, it triggers short-orders upon dynamic breakdown of steep price-trend relationships, targeting massive liquidity cascades.",
                direction = "SHORT",
                marketCondition = "Trending",
                premiumTag = "Macro CTA Reversal"
            ),
            StrategyBlueprint(
                title = "Volumetric Liquidity Sweep",
                trend = "Bearish Exhaustion Rallies",
                metrics = "Relative volume (RV) < 0.6 + Price spike > 8%",
                description = "This strategy highlights rallies in thin-orderbook setups. Lower liquidity easily causes sharp, unsustainable upward spikes on extremely low volume, yielding high-probability, short-term reversion sweeps.",
                direction = "SHORT",
                marketCondition = "Choppy",
                premiumTag = "Liquidity Sweep Capture"
            ),
            StrategyBlueprint(
                title = "Funding Rate Arbitrage Squeeze",
                trend = "Bearish Leverage Flusher",
                metrics = "Funding > 0.12% per 8h + OI exhaustion",
                description = "Exploits extreme retail leverage skew. Whenever funding costs skyrocket, it establishes short vectors to align with institutional market makers who hunt downstream leveraged stop-losses through rapid flush-outs.",
                direction = "SHORT",
                marketCondition = "Choppy",
                premiumTag = "Leverage Arbitrage Alpha"
            ),
            StrategyBlueprint(
                title = "Weekly Pivot Resistance Rejection",
                trend = "Bearish Range Envelope",
                metrics = "Weekly R2/R3 touches + Volume divergence",
                description = "High-timeframe quantitative range play. Monitors the critical mathematical standard boundaries. Initiates scalp short entries upon decisive exhaustion candles rejecting weekly R2 or R3 pivot bands back toward range equilibrium.",
                direction = "SHORT",
                marketCondition = "Sideways",
                premiumTag = "Pivot Theory Engine"
            ),
            StrategyBlueprint(
                title = "Order Flow Overexpansion (Bearish FVG)",
                trend = "Bearish Premium Selloff",
                metrics = "Bearish Fair Value Gap fill + supply sweep",
                description = "Applies SMC order block discipline on the short side. Detects rapid downside impulse zones and places sell limits directly inside the premium imbalance envelope, targeting the sweep of historic lows.",
                direction = "SHORT",
                marketCondition = "Sideways",
                premiumTag = "SMC Imbalance Hunter"
            )
        )

        val filteredBlueprints = blueprints.filter { bp ->
            (directionFilter == "ALL" || bp.direction == directionFilter) &&
            (conditionFilter == "ALL" || bp.marketCondition == conditionFilter)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // CENTRAL FILTER DASHBOARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = CyberDark.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "QUANT-STRATEGY CONTROL CENTERS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // DIRECTION FILTERS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DIRECTION:",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.width(72.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            listOf("ALL" to "ALL DIRECTIONS", "LONG" to "LONG ONLY", "SHORT" to "SHORT ONLY").forEach { (code, label) ->
                                val active = directionFilter == code
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (active) (if (code == "LONG") CyberAccentGreen.copy(alpha = 0.25f) else if (code == "SHORT") CyberAccentRed.copy(alpha = 0.25f) else CyberSurface) else CyberDark)
                                        .border(
                                            width = 1.dp,
                                            color = if (active) (if (code == "LONG") CyberAccentGreen else if (code == "SHORT") CyberAccentRed else CyberGold) else Color.Transparent,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { directionFilter = code }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (active) CyberTextWhite else CyberTextDim,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // MARKET PROFILE FILTERS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MARKET TYPE:",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.width(72.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            listOf("ALL" to "ALL CONDITIONS", "Trending" to "TRENDING", "Choppy" to "CHOPPY", "Sideways" to "SIDEWAYS").forEach { (code, label) ->
                                val active = conditionFilter == code
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (active) CyberSurface else CyberDark)
                                        .border(
                                            width = 1.dp,
                                            color = if (active) CyberGold else Color.Transparent,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { conditionFilter = code }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (active) CyberTextWhite else CyberTextDim,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (filteredBlueprints.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = CyberTextDim,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No strategies align with selected filters.",
                            color = CyberTextDim,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("blueprints_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredBlueprints) { setup ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = CyberCard),
                            shape = RoundedCornerShape(24.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                // METADATA & DIRECTION BADGES
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = setup.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Black,
                                            color = CyberTextWhite,
                                            letterSpacing = (-0.3).sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        // Dynamic Sub-Tagging Row
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            // 1. DIRECTION
                                            Text(
                                                text = if (setup.direction == "LONG") "▲ LONG" else "▼ SHORT",
                                                color = if (setup.direction == "LONG") CyberAccentGreen else CyberAccentRed,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 8.5.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                            // Divider Dot
                                            Box(modifier = Modifier.size(3.dp).background(CyberTextDim, CircleShape))
                                            // 2. CONDITION
                                            Text(
                                                text = when (setup.marketCondition) {
                                                    "Trending" -> "⚡ Trending"
                                                    "Choppy" -> "≈ Choppy"
                                                    else -> "↔ Sideways"
                                                },
                                                color = when (setup.marketCondition) {
                                                    "Trending" -> Color(0xFF38BDF8)
                                                    "Choppy" -> Color(0xFFF59E0B)
                                                    else -> Color(0xFFA3A3A3)
                                                },
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 8.5.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                            // Divider Dot
                                            Box(modifier = Modifier.size(3.dp).background(CyberTextDim, CircleShape))
                                            // 3. CODE TAG
                                            Text(
                                                text = "★ ${setup.premiumTag}",
                                                color = CyberGold,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 8.5.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(CyberSlate)
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Text(text = setup.trend, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberPercentColor(setup.trend))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Trigger Threshold: ${setup.metrics}",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberGold,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = setup.description, fontSize = 12.sp, color = CyberTextWhite, lineHeight = 18.sp)
                                
                                val lastResult = latestBacktestResults[setup.title]
                                if (lastResult != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                                        drawLine(
                                            color = CyberSurface,
                                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                            strokeWidth = 1.dp.toPx(),
                                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "LATEST BACKTEST PERFORMANCE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberGold,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // ROI
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(CyberSlate.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("ROI", fontSize = 8.sp, color = CyberTextDim, fontWeight = FontWeight.Bold)
                                                Text(
                                                    text = "${if (lastResult.roi >= 0) "+" else ""}${String.format(java.util.Locale.US, "%.1f", lastResult.roi)}%",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (lastResult.roi >= 0) Color(0xFF15803D) else CyberAccentRed
                                                )
                                            }
                                        }
                                        // Win Rate
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(CyberSlate.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("WIN RATE", fontSize = 8.sp, color = CyberTextDim, fontWeight = FontWeight.Bold)
                                                Text(
                                                    text = "${String.format(java.util.Locale.US, "%.1f", lastResult.winRate)}%",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = CyberTextWhite
                                                )
                                            }
                                        }
                                        // Drawdown
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(CyberSlate.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("MAX DD", fontSize = 8.sp, color = CyberTextDim, fontWeight = FontWeight.Bold)
                                                Text(
                                                    text = "-${String.format(java.util.Locale.US, "%.1f", lastResult.maxDrawdown)}%",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (lastResult.maxDrawdown > 25.0) CyberAccentRed else CyberTextDim
                                                )
                                            }
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = { selectedStrategy = setup },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyberAccentGreen,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("run_backtest_btn_${setup.title.replace(" ", "_").lowercase()}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Run Backtest",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "RUN BACKTEST SIMULATION",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BacktestSimulatorScreen(
    strategy: StrategyBlueprint,
    viewModel: CryptoViewModel,
    onResultReady: (SimulationResult) -> Unit,
    onBack: () -> Unit
) {
    var initialCapital by remember { mutableStateOf(10000f) }
    var customCapitalInput by remember { mutableStateOf("10000") }
    var selectedAsset by remember { mutableStateOf("ALL") }
    var leverage by remember { mutableStateOf(5) }
    var timeframeDays by remember { mutableStateOf(90) }
    val botSelectedBlueprints by viewModel.botSelectedBlueprints.collectAsState()

    LaunchedEffect(Unit) {
        // Automatically fetch updated, fresh coin market data when entering backtester
        viewModel.forceFullRefresh()
    }

    var isSimulating by remember { mutableStateOf(false) }
    var simulationProgress by remember { mutableStateOf(0f) }
    var simulationLogs by remember { mutableStateOf<List<String>>(emptyList()) }
    var showResults by remember { mutableStateOf(false) }

    // Simulation calculation values
    var finalCapital by remember { mutableStateOf(10000.0) }
    var roi by remember { mutableStateOf(0.0) }
    var winRate by remember { mutableStateOf(0.0) }
    var profitFactor by remember { mutableStateOf(0.0) }
    var maxDrawdown by remember { mutableStateOf(0.0) }
    var tradesLedger by remember { mutableStateOf<List<SimulatedTrade>>(emptyList()) }
    var equityPoints by remember { mutableStateOf<List<Double>>(emptyList()) }

    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("backtest_simulator_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBack() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Go Back",
                    tint = CyberAccentGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "BACK TO BLUEPRINTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberAccentGreen,
                    letterSpacing = 1.sp
                )
            }
        }

        // Strategy info Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberSlate.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = strategy.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberTextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Trigger Core: ${strategy.metrics}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold
                    )
                }
            }
        }

        // Adjustable Parameters Selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "SIMULATION ENGINE INPUTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberTextDim,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Initial Capital Selection
                    Text(
                        text = "Initial Capital: $${formatLargePriceDecimal(initialCapital.toDouble())}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5000f, 10000f, 25000f, 50000f).forEach { capitalVal ->
                            val isSelected = initialCapital == capitalVal
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberAccentGreen else CyberSlate)
                                    .clickable { 
                                        initialCapital = capitalVal
                                        customCapitalInput = capitalVal.toInt().toString()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$${formatLargePriceDecimal(capitalVal.toDouble())}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White else CyberTextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Manual Capital Input Box
                    OutlinedTextField(
                        value = customCapitalInput,
                        onValueChange = { inputVal ->
                            val filtered = inputVal.filter { it.isDigit() }.take(8)
                            customCapitalInput = filtered
                            initialCapital = if (filtered.isNotEmpty()) {
                                val floatVal = filtered.toFloatOrNull() ?: 10000f
                                if (floatVal <= 0f) 10000f else floatVal
                            } else {
                                10000f
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_capital_input")
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Enter manual Capital amount...", fontSize = 12.sp, color = CyberTextDim) },
                        prefix = { Text("$ ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyberAccentGreen) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CyberTextWhite,
                            unfocusedTextColor = CyberTextWhite,
                            focusedContainerColor = CyberDark,
                            unfocusedContainerColor = CyberDark,
                            focusedBorderColor = CyberAccentGreen,
                            unfocusedBorderColor = CyberSurface
                        ),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1b. Particular Coin Selector
                    Text(
                        text = "Simulated Asset Focus: ${selectedAsset}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("ALL", "POPCAT", "WIF", "BRETT", "MEW", "BOME", "MYRO", "TOSHI", "COQ", "DEGEN", "SILLY")) { assetName ->
                            val isSelected = selectedAsset == assetName
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberAccentGreen else CyberSlate)
                                    .clickable { selectedAsset = assetName }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("coin_asset_chip_${assetName.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = assetName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White else CyberTextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 2. Leverage Selection
                    Text(
                        text = "Leverage Multiplier: ${leverage}x",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1, 3, 5, 10, 20).forEach { levVal ->
                            val isSelected = leverage == levVal
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberAccentGreen else CyberSlate)
                                    .clickable { leverage = levVal }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${levVal}x",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White else CyberTextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Timeframe selector
                    Text(
                        text = "Historical Backtest Span",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(30, 90, 180).forEach { days ->
                            val isSelected = timeframeDays == days
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberAccentGreen else CyberSlate)
                                    .clickable { timeframeDays = days }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${days} Days",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White else CyberTextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                isSimulating = true
                                simulationProgress = 0f
                                showResults = false
                                val logsList = mutableListOf<String>()
                                simulationLogs = logsList
                                
                                val sequence = listOf(
                                    "Establishing SSL link to historical node storage..." to 0.12f,
                                    "Retrieving hourly candle OHLCV baskets for ${if (selectedAsset == "ALL") "all microcaps" else selectedAsset} (Market Cap tag: 50M-200M)..." to 0.30f,
                                    "Simulating trend matrices (EMA thresholds, ATR bands)..." to 0.50f,
                                    "Modeling liquidity depths & slip tolerances on synthetic entries..." to 0.70f,
                                    "Computing leverage margin & liquidation event triggers..." to 0.85f,
                                    "Formulating backtest trade sheets and risk factors..." to 0.95f,
                                    "Collation successfully finished." to 1.0f
                                )
                                
                                for ((log, targetProgress) in sequence) {
                                    logsList.add("[${getCurrentTime()}] $log")
                                    simulationLogs = logsList.toList()
                                    
                                    val steps = 8
                                    val startProgress = simulationProgress
                                    val stepSize = (targetProgress - startProgress) / steps
                                    for (step in 1..steps) {
                                        kotlinx.coroutines.delay(25)
                                        simulationProgress = startProgress + stepSize * step
                                    }
                                    kotlinx.coroutines.delay(100 + (0..100).random().toLong())
                                }
                                
                                val results = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                                    runBacktestCalculation(strategy.title, initialCapital, leverage, timeframeDays, selectedAsset, viewModel.scannedCoins.value)
                                }
                                finalCapital = results.finalCapital
                                roi = results.roi
                                winRate = results.winRate
                                profitFactor = results.profitFactor
                                maxDrawdown = results.maxDrawdown
                                tradesLedger = results.trades
                                equityPoints = results.equityCurve
                                
                                onResultReady(results)
                                
                                isSimulating = false
                                showResults = true
                            }
                        },
                        enabled = !isSimulating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberAccentGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (isSimulating) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "RUN SIMULATED HISTORICAL RANGE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Live status simulator terminal
        if (isSimulating) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberTextWhite),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SIMULATION LOGS",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CyberSlate
                            )
                            Text(
                                text = "${Math.round(simulationProgress * 100)}%",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LinearProgressIndicator(
                            progress = { if (simulationProgress.isNaN() || simulationProgress.isInfinite()) 0f else simulationProgress.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = CyberAccentGreen,
                            trackColor = CyberTextDim.copy(alpha = 0.3f),
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            simulationLogs.takeLast(4).forEach { logLine ->
                                Text(
                                    text = logLine,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberAccentGreen,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Completed Results Panel
        if (showResults) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "BACKTEST RUN PERFORMANCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberTextDim,
                        letterSpacing = 1.sp
                    )

                    val isWhitelisted = botSelectedBlueprints.contains(strategy.title)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("backtest_bot_deployment_card"),
                        colors = CardDefaults.cardColors(containerColor = CyberSlate.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(if (isWhitelisted) Color(0xFF22C55E) else CyberAccentRed, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ALPHABOT DEPLOYMENT PROTOCOL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberGold,
                                        letterSpacing = 1.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isWhitelisted) "Strategy Whitelisted & Active in Auto Bot" else "Ready to Deploy to Auto Bot Whitelist",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyberTextWhite
                                )
                                Text(
                                    text = if (isWhitelisted) "AlphaBot will automatically trade and execute paper orders for any scanner setups matching this strategy." else "Add this backtested strategy blueprint to the active whitelist range of the trading bot.",
                                    fontSize = 10.sp,
                                    color = CyberTextDim,
                                    lineHeight = 14.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Switch(
                                checked = isWhitelisted,
                                onCheckedChange = { viewModel.toggleBotBlueprint(strategy.title) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF005AC1),
                                    uncheckedThumbColor = CyberTextDim,
                                    uncheckedTrackColor = CyberSurface
                                ),
                                modifier = Modifier.testTag("backtest_deploy_switch")
                            )
                        }
                    }

                    // 1. KPI Stats grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // ROI Card
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberCard),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("NET ROI", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberTextDim)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${if (roi >= 0) "+" else ""}${String.format(java.util.Locale.US, "%.2f", roi)}%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (roi >= 0) CyberAccentGreen else CyberAccentRed
                                )
                            }
                        }

                        // Final Capital Card
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberCard),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("FINAL BALANCE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberTextDim)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$${formatLargePriceDecimal(finalCapital)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyberTextWhite
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Win Rate Card
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberCard),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("WIN RATE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberTextDim)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${String.format(java.util.Locale.US, "%.1f", winRate)}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyberTextWhite
                                )
                            }
                        }

                        // Profit Factor Card
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberCard),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("PROFIT FACTOR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberTextDim)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = String.format(java.util.Locale.US, "%.2f", profitFactor),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (profitFactor >= 1.5) CyberAccentGreen else CyberGold
                                )
                            }
                        }

                        // Max Drawdown Card
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberCard),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("MAX DRAWDOWN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberTextDim)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "-${String.format(java.util.Locale.US, "%.1f", maxDrawdown)}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (maxDrawdown > 25.0) CyberAccentRed else CyberTextDim
                                )
                            }
                        }
                    }

                    // 2. Chart
                    Text(
                        text = "EQUITY OSCILLATION RANGE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim,
                        letterSpacing = 0.5.sp
                    )
                    EquityCurveChart(
                        points = equityPoints,
                        initialCapital = initialCapital.toDouble(),
                        finalCapital = finalCapital
                    )

                    // 3. Trade sheet header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SIMULATED TRADES LEDGER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            letterSpacing = 0.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberSlate)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${tradesLedger.size} Trades Executed",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen
                            )
                        }
                    }

                    // List of trades
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        tradesLedger.forEach { trade ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = CyberCard),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Visual Coin avatar
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(CyberSlate),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = trade.asset.take(1).uppercase(),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black,
                                                color = CyberAccentGreen
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "${trade.asset}/USDT",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CyberTextWhite
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(if (trade.type == "LONG") CyberAccentGreen.copy(alpha = 0.15f) else CyberAccentRed.copy(alpha = 0.15f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = trade.type,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (trade.type == "LONG") CyberAccentGreen else CyberAccentRed
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = trade.date,
                                                    fontSize = 9.sp,
                                                    color = CyberTextDim
                                                )
                                            }
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${if (trade.profitUsd >= 0) "+" else ""}$${String.format(java.util.Locale.US, "%.2f", trade.profitUsd)} USD",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (trade.profitUsd >= 0) CyberAccentGreen else CyberAccentRed
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${if (trade.pchange >= 0) "+" else ""}${String.format(java.util.Locale.US, "%.1f", trade.pchange)}%",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (trade.profitUsd >= 0) CyberAccentGreen else CyberAccentRed
                                        )
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

@Composable
fun EquityCurveChart(
    points: List<Double>,
    initialCapital: Double,
    finalCapital: Double,
    modifier: Modifier = Modifier
) {
    val cleanPoints = remember(points) {
        val rawFiltered = points.filter { !it.isNaN() && !it.isInfinite() }
        if (rawFiltered.size > 50) {
            val step = rawFiltered.size.toDouble() / 50.0
            List(50) { i ->
                rawFiltered[(i * step).toInt().coerceIn(0, rawFiltered.size - 1)]
            }
        } else {
            rawFiltered
        }
    }
    if (cleanPoints.size < 2) return
    
    val isProfit = finalCapital >= initialCapital
    val strokeColor = if (isProfit) CyberAccentGreen else CyberAccentRed
    val gradientColor = if (isProfit) CyberAccentGreen.copy(alpha = 0.2f) else CyberAccentRed.copy(alpha = 0.2f)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(CyberSlate.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .border(1.dp, CyberSurface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            if (cleanPoints.size < 2) return@Canvas
            
            val minVal = cleanPoints.minOrNull() ?: 0.0
            val maxVal = cleanPoints.maxOrNull() ?: 1.0
            val range = if (maxVal - minVal == 0.0) 1.0 else maxVal - minVal
            
            val path = Path()
            val fillPath = Path()
            
            val stepX = width / (cleanPoints.size - 1)
            
            cleanPoints.forEachIndexed { index, value ->
                val x = index * stepX
                val normalizedY = (value - minVal) / range
                val y = height - (normalizedY.toFloat() * height)
                
                if (index == 0) {
                    path.moveTo(x, y)
                    fillPath.moveTo(x, height)
                    fillPath.lineTo(x, y)
                } else {
                    path.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
                
                if (index == cleanPoints.size - 1) {
                    fillPath.lineTo(x, height)
                    fillPath.close()
                }
            }
            
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(gradientColor, Color.Transparent),
                    startY = 0f,
                    endY = if (height <= 0f) 1f else height
                )
            )
            
            drawPath(
                path = path,
                color = strokeColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
            
            val gridLines = 3
            for (i in 1..gridLines) {
                val gridY = height * (i.toFloat() / (gridLines + 1))
                drawLine(
                    color = CyberTextDim.copy(alpha = 0.1f),
                    start = Offset(0f, gridY),
                    end = Offset(width, gridY),
                    strokeWidth = 1.dp.toPx()
                )
            }
            
            val lastX = width
            val lastNormalizedY = (cleanPoints.last() - minVal) / range
            val lastY = height - (lastNormalizedY.toFloat() * height)
            
            drawCircle(
                color = strokeColor,
                radius = 5.dp.toPx(),
                center = Offset(lastX, lastY)
            )
            drawCircle(
                color = strokeColor.copy(alpha = 0.4f),
                radius = 10.dp.toPx(),
                center = Offset(lastX, lastY)
            )
        }
    }
}

fun runBacktestCalculation(
    strategyTitle: String,
    initialCapital: Float,
    leverage: Int,
    timeframeDays: Int,
    selectedAsset: String = "ALL",
    scannedCoins: List<com.example.data.model.Coin> = emptyList()
): SimulationResult {
    val seed = (strategyTitle.hashCode() + leverage * 31 + timeframeDays * 17 + selectedAsset.hashCode()).toLong()
    val random = java.util.Random(seed)
    
    val baseWinRate = when (strategyTitle) {
        "EMA Continuation Cross (V3)" -> 0.68
        "Volumetric Liquidity Sweep" -> 0.58
        "Mean Reversion & Oversold Bounce" -> 0.72
        "Wyckoff Spring & Phase C Accumulation" -> 0.76
        "High-Volume Momentum Breakout" -> 0.64
        "Institutional Order Block Grab" -> 0.81
        "MACD Divergence & Momentum Exhaustion" -> 0.69
        else -> 0.70
    }
    
    val avgWinPct = when (strategyTitle) {
        "EMA Continuation Cross (V3)" -> 0.08
        "Volumetric Liquidity Sweep" -> 0.15
        "Mean Reversion & Oversold Bounce" -> 0.11
        "Wyckoff Spring & Phase C Accumulation" -> 0.14
        "High-Volume Momentum Breakout" -> 0.12
        "Institutional Order Block Grab" -> 0.07
        "MACD Divergence & Momentum Exhaustion" -> 0.09
        else -> 0.08
    }
    
    val avgLossPct = when (strategyTitle) {
        "EMA Continuation Cross (V3)" -> -0.03
        "Volumetric Liquidity Sweep" -> -0.08
        "Mean Reversion & Oversold Bounce" -> -0.04
        "Wyckoff Spring & Phase C Accumulation" -> -0.045
        "High-Volume Momentum Breakout" -> -0.05
        "Institutional Order Block Grab" -> -0.02
        "MACD Divergence & Momentum Exhaustion" -> -0.035
        else -> -0.03
    }
    
    val tradeFrequencyDays = when (strategyTitle) {
        "EMA Continuation Cross (V3)" -> 3
        "Volumetric Liquidity Sweep" -> 4
        "Mean Reversion & Oversold Bounce" -> 5
        "Wyckoff Spring & Phase C Accumulation" -> 6
        "High-Volume Momentum Breakout" -> 2
        "Institutional Order Block Grab" -> 4
        "MACD Divergence & Momentum Exhaustion" -> 3
        else -> 4
    }
    
    val totalTrades = timeframeDays / tradeFrequencyDays
    val tradesList = mutableListOf<SimulatedTrade>()
    val equityCurve = mutableListOf<Double>()
    
    val safeInitialCapital = if (initialCapital <= 0f) 10000.0 else initialCapital.toDouble()
    var currentBalance = safeInitialCapital
    equityCurve.add(currentBalance)
    
    val defaultAssets = listOf("POPCAT", "WIF", "BRETT", "MEW", "BOME", "MYRO", "TOSHI", "COQ", "DEGEN", "SILLY")
    val assets = if (scannedCoins.isNotEmpty()) scannedCoins.map { it.symbol.uppercase() } else defaultAssets
    
    var peak = currentBalance
    var maxDrawdown = 0.0
    var totalWins = 0
    var totalLosses = 0
    var grossProfits = 0.0
    var grossLosses = 0.0
    
    val calendar = java.util.Calendar.getInstance()
    calendar.add(java.util.Calendar.DAY_OF_YEAR, -timeframeDays)
    
    for (i in 0 until totalTrades) {
        if (currentBalance <= 0.0) {
            currentBalance = 0.0
            equityCurve.add(0.0)
            break
        }
        
        val asset = if (selectedAsset == "ALL") assets[random.nextInt(assets.size)] else selectedAsset
        val type = if (strategyTitle.contains("Sweep") || strategyTitle.contains("Divergence") || strategyTitle.contains("Exhaustion") || strategyTitle.contains("Bearish") || strategyTitle.contains("Short")) "SHORT" else "LONG"
        
        val matchingCoin = scannedCoins.find { it.symbol.uppercase() == asset }
        val coinPrice = matchingCoin?.currentPrice ?: (0.10 + random.nextDouble() * 4.5)
        val coinPriceChange24h = matchingCoin?.priceChangePercentage24h ?: 0.0
        
        // Base trend matching logic from coin data
        val coinIsUp = coinPriceChange24h >= 0.0
        val isTrendMatching = (type == "LONG" && coinIsUp) || (type == "SHORT" && !coinIsUp)
        
        val finalWinRate = if (matchingCoin != null) {
            if (isTrendMatching) {
                (baseWinRate + 0.10).coerceAtMost(0.95)
            } else {
                (baseWinRate - 0.10).coerceAtLeast(0.35)
            }
        } else {
            baseWinRate
        }
        
        val isWin = random.nextDouble() < finalWinRate
        
        val changePctBase = if (isWin) {
            avgWinPct + (random.nextDouble() * 0.05)
        } else {
            avgLossPct - (random.nextDouble() * 0.02)
        }
        
        // Dynamic price volatility factor based on coin Geck data
        val targetChangePct = if (matchingCoin != null) {
            val volFactor = (kotlin.math.abs(coinPriceChange24h) / 100.0).coerceIn(0.01, 0.35)
            changePctBase * (1.0 + volFactor)
        } else {
            changePctBase
        }
        
        var leveragedChange = targetChangePct * leverage
        var liquidated = false
        
        if (leveragedChange <= -1.0) {
            leveragedChange = -1.0
            liquidated = true
        }
        
        val allocation = 0.20
        val positionSize = currentBalance * allocation
        val tradePnL = positionSize * leveragedChange
        
        currentBalance += tradePnL
        if (currentBalance < 0.0) {
            currentBalance = 0.0
        }
        
        equityCurve.add(currentBalance)
        
        if (currentBalance > peak) {
            peak = currentBalance
        } else if (peak > 0.0) {
            val drawdown = (peak - currentBalance) / peak * 100.0
            if (drawdown > maxDrawdown) {
                maxDrawdown = drawdown
            }
        }
        
        if (tradePnL > 0) {
            totalWins++
            grossProfits += tradePnL
        } else {
            totalLosses++
            grossLosses += kotlin.math.abs(tradePnL)
        }
        
        calendar.add(java.util.Calendar.DAY_OF_YEAR, tradeFrequencyDays)
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
        val dateStr = sdf.format(calendar.time)
        
        val entryPrice = coinPrice * (0.95 + random.nextDouble() * 0.10)
        val exitPrice = entryPrice * (1.0 + targetChangePct)
        
        tradesList.add(
            SimulatedTrade(
                asset = asset,
                type = type,
                entryPrice = entryPrice,
                exitPrice = exitPrice,
                pchange = leveragedChange * 100.0,
                profitUsd = tradePnL,
                isWin = isWin && !liquidated,
                date = dateStr
            )
        )
    }
    
    val netProfit = currentBalance - safeInitialCapital
    val roi = (netProfit / safeInitialCapital) * 100.0
    val winRatePct = if (totalTrades > 0) (totalWins.toDouble() / totalTrades.toDouble()) * 100.0 else 0.0
    val profitFactorValue = if (grossLosses > 0.0) grossProfits / grossLosses else grossProfits

    val safeRoi = if (roi.isNaN() || roi.isInfinite()) 0.0 else roi
    val safeWinRatePct = if (winRatePct.isNaN() || winRatePct.isInfinite()) 0.0 else winRatePct
    val safeProfitFactorValue = if (profitFactorValue.isNaN() || profitFactorValue.isInfinite()) 0.0 else profitFactorValue
    val safeMaxDrawdown = if (maxDrawdown.isNaN() || maxDrawdown.isInfinite()) 0.0 else maxDrawdown
    
    return SimulationResult(
        finalCapital = currentBalance,
        roi = safeRoi,
        winRate = safeWinRatePct,
        profitFactor = safeProfitFactorValue,
        maxDrawdown = safeMaxDrawdown,
        trades = tradesList.reversed(),
        equityCurve = equityCurve
    )
}

data class SimulationResult(
    val finalCapital: Double,
    val roi: Double,
    val winRate: Double,
    val profitFactor: Double,
    val maxDrawdown: Double,
    val trades: List<SimulatedTrade>,
    val equityCurve: List<Double>
)

data class SimulatedTrade(
    val asset: String,
    val type: String,
    val entryPrice: Double,
    val exitPrice: Double,
    val pchange: Double,
    val profitUsd: Double,
    val isWin: Boolean,
    val date: String
)

private fun getCurrentTime(): String {
    val formatter = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
    return formatter.format(java.util.Date())
}

fun formatLargePriceDecimal(price: Double): String {
    if (price.isNaN() || price.isInfinite()) return "0"
    return String.format(java.util.Locale.US, "%,.0f", price)
}

data class StrategyBlueprint(
    val title: String,
    val trend: String,
    val metrics: String,
    val description: String,
    val direction: String = "LONG",
    val marketCondition: String = "Trending",
    val premiumTag: String = "Institutional Strategy"
)

@Composable
fun CyberPercentColor(trend: String): Color {
    return if (trend.contains("Bullish") || trend.contains("Bounce")) CyberAccentGreen else CyberAccentRed
}

@Composable
fun CoinCredentialsOverlay(
    coin: Coin,
    onDismiss: () -> Unit
) {
    BackHandler(enabled = true) {
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Consume click events on card itself */ }
                .border(2.dp, CyberAccentGreen, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CyberCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (coin.image != null) {
                            AsyncImage(
                                model = coin.image,
                                contentDescription = "${coin.name} logo",
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(CyberDark)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(CyberSlate),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = coin.symbol.take(2).uppercase(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextWhite
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = coin.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = coin.symbol.uppercase(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen
                            )
                        }
                    }
                    
                    // Close button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(CyberDark, CircleShape)
                            .border(1.dp, CyberSurface, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss credentials",
                            tint = CyberTextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(18.dp))
                
                // Real-time badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberDark, RoundedCornerShape(8.dp))
                        .border(1.dp, CyberSurface, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CyberAccentGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REAL-TIME MARKET CREDENTIALS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "LIVE DATA",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberAccentGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Grid of items: Market Cap & 24h Volume
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CredentialStatCard(
                        title = "MARKET CAP",
                        formattedVal = "$${formatLargeNumber(coin.marketCap)}",
                        rawVal = "$${safeFormatDouble(coin.marketCap, "%,.2f")}",
                        modifier = Modifier.weight(1f)
                    )
                    CredentialStatCard(
                        title = "VOLUME (24H)",
                        formattedVal = "$${formatLargeNumber(coin.totalVolume ?: 0.0)}",
                        rawVal = if (coin.totalVolume != null) "$${safeFormatDouble(coin.totalVolume, "%,.2f")}" else "N/A",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Supply metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CredentialStatCard(
                        title = "CIRCULATING SUPPLY",
                        formattedVal = formatSupply(coin.circulatingSupply, coin.symbol),
                        rawVal = coin.circulatingSupply?.let { safeFormatDouble(it, "%,.0f") } ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )
                    CredentialStatCard(
                        title = "TOTAL SUPPLY",
                        formattedVal = formatSupply(coin.totalSupply, coin.symbol),
                        rawVal = coin.totalSupply?.let { safeFormatDouble(it, "%,.0f") } ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Max Supply
                Row(modifier = Modifier.fillMaxWidth()) {
                    CredentialStatCard(
                        title = "MAX SUPPLY",
                        formattedVal = coin.maxSupply?.let { formatSupply(it, coin.symbol) } ?: "∞ (No Hard Cap)",
                        rawVal = coin.maxSupply?.let { safeFormatDouble(it, "%,.0f") } ?: "Unbounded",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // If Circulating & Max percentage bar, show it
                val circulating = coin.circulatingSupply
                val total = coin.totalSupply ?: coin.maxSupply
                if (circulating != null && total != null && total > 0.0 &&
                    !circulating.isNaN() && !circulating.isInfinite() &&
                    !total.isNaN() && !total.isInfinite()) {
                    val rawPct = circulating / total * 100.0
                    val pct = if (rawPct.isNaN() || rawPct.isInfinite()) 0.0 else rawPct.coerceIn(0.0, 100.0)
                    val fraction = (pct / 100.0).toFloat().coerceIn(0f, 1f)
                    val safeFraction = if (fraction.isNaN() || fraction.isInfinite()) 0f else fraction
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .border(1.dp, CyberSurface, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CIRCULATING SUPPLY RATIO",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextDim,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = safeFormatDouble(pct, "%.2f") + "%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberAccentGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Progress bar container
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(CyberSurface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(safeFraction)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(CyberAccentGreen.copy(alpha = 0.5f), CyberAccentGreen)
                                        )
                                    )
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Action Dismiss Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberAccentGreen,
                        contentColor = CyberDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "CLOSE SYSTEM MONITOR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CredentialStatCard(
    title: String,
    formattedVal: String,
    rawVal: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CyberDark),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = CyberTextDim,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formattedVal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = CyberTextWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = rawVal,
                fontSize = 9.sp,
                color = CyberTextDim,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun formatSupply(value: Double?, symbol: String): String {
    if (value == null || value <= 0.0) return "N/A"
    return "${formatLargeNumber(value)} ${symbol.uppercase()}"
}

fun safeFormatDouble(value: Double?, format: String, fallback: String = "N/A"): String {
    if (value == null || value.isNaN() || value.isInfinite()) return fallback
    return try {
        String.format(java.util.Locale.US, format, value)
    } catch (e: Exception) {
        fallback
    }
}

@Composable
fun ThinFooterNotice() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface)
            .padding(horizontal = 12.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Risk Warning: Smallcap cryptocurrencies carry high liquidity risks. All predictions are generated algorithmically for informative and training purposes.",
            fontSize = 7.2.sp,
            color = CyberTextDim,
            textAlign = TextAlign.Center,
            lineHeight = 9.sp
        )
    }
}

// --- High Precision Utility Helpers ---
fun formatPrice(price: Double): String {
    if (price.isNaN() || price.isInfinite()) return "0.00"
    return when {
        price >= 1000.0 -> String.format(java.util.Locale.US, "%,.2f", price)
        price >= 1.0 -> String.format(java.util.Locale.US, "%.3f", price)
        price >= 0.001 -> String.format(java.util.Locale.US, "%.4f", price)
        else -> String.format(java.util.Locale.US, "%.6f", price)
    }
}

fun formatCurrency(amount: Double): String {
    if (amount.isNaN() || amount.isInfinite()) return "0.00"
    return String.format(java.util.Locale.US, "%,.2f", amount)
}

fun formatLargeNumber(num: Double): String {
    if (num.isNaN() || num.isInfinite()) return "0.0"
    return when {
        num >= 1_000_000_000.0 -> String.format(java.util.Locale.US, "%.2f B", num / 1_000_000_000.0)
        num >= 1_000_000.0 -> String.format(java.util.Locale.US, "%.2f M", num / 1_000_000.0)
        num >= 1_000.0 -> String.format(java.util.Locale.US, "%.1f K", num / 1_000.0)
        else -> String.format(java.util.Locale.US, "%.1f", num)
    }
}

fun formatEpochToDate(epochMs: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US)
    return sdf.format(java.util.Date(epochMs))
}

@Composable
fun PaperTradingPortfolioTab(viewModel: CryptoViewModel) {
    val cashBalance by viewModel.cashBalance.collectAsState()
    val rawOpenTrades by viewModel.openTrades.collectAsState()
    val rawClosedTrades by viewModel.closedTrades.collectAsState()
    val botEnabled by viewModel.botEnabled.collectAsState()
    val scanLogs by viewModel.scanLogs.collectAsState()

    // Only include non-MEXC regular paper trades in the paper portfolio tab
    val openTrades = rawOpenTrades.filter { !it.isMexcTrade }
    val closedTrades = rawClosedTrades.filter { !it.isMexcTrade }

    // Calculate portfolio valuation
    val unrealizedPnL = openTrades.sumOf { it.pnl }
    val totalPortfolioValue = cashBalance + unrealizedPnL
    val totalRealizedPnL = closedTrades.sumOf { it.pnl }
    
    var isHistoryActive by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("paper_trading_tab_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Account Metrics Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("portfolio_summary_card"),
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
                        Column {
                            Text(
                                text = "NET PORTFOLIO VALUATION",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextDim,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$${formatCurrency(totalPortfolioValue)}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite,
                                letterSpacing = (-1).sp
                            )
                        }
                        
                        IconButton(
                            onClick = { viewModel.resetPaperTradingAccount() },
                            modifier = Modifier
                                .background(CyberDark, CircleShape)
                                .size(36.dp)
                                .testTag("reset_account_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Account Balance",
                                tint = CyberAccentRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AVAILABLE BALANCE", fontSize = 8.sp, color = CyberTextDim, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$${formatCurrency(cashBalance)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                              )
                        }
                        
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("ACTIVE UNREALIZED P&L", fontSize = 8.sp, color = CyberTextDim, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            val pnlColor = if (unrealizedPnL >= 0.0) Color(0xFF15803D) else CyberAccentRed
                            val pnlSign = if (unrealizedPnL >= 0.0) "+" else ""
                            Text(
                                text = "$pnlSign$${formatCurrency(unrealizedPnL)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = pnlColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Realized P&L: ${if (totalRealizedPnL >= 0) "+" else ""}$${formatCurrency(totalRealizedPnL)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (totalRealizedPnL >= 0) Color(0xFF15803D) else CyberAccentRed
                        )

                        Text(
                            text = "Completed Trades: ${closedTrades.size}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim
                        )
                    }
                }
            }
        }

        // 2. Sub-tabs Selector
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberSurface, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (!isHistoryActive) CyberCard else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { isHistoryActive = false }
                        .padding(vertical = 8.dp)
                        .testTag("open_positions_sub_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "OPEN POSITIONS (${openTrades.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (!isHistoryActive) CyberTextWhite else CyberTextDim
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isHistoryActive) CyberCard else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { isHistoryActive = true }
                        .padding(vertical = 8.dp)
                        .testTag("history_ledger_sub_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HISTORY LEDGER (${closedTrades.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isHistoryActive) CyberTextWhite else CyberTextDim
                    )
                }
            }
        }

        // 3. Conditional Content
        if (!isHistoryActive) {
            if (openTrades.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Empty trades",
                                tint = CyberSlate,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "No Active Positions Open",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Go to the 'CONFIRMED SIGNALS' tab and tap 'EXECUTE LIVE PAPER TRADE' on any premium signal to launch digital orders.",
                                fontSize = 10.sp,
                                color = CyberTextDim,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(openTrades, key = { index, trade -> "open_${trade.id}_$index" }) { index, trade ->
                    OpenPositionCard(
                        trade = trade,
                        onCloseClick = { viewModel.closePaperTradeManually(trade) }
                    )
                }
            }
        } else {
            if (closedTrades.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Empty History",
                                tint = CyberSlate,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "No Trade History Record",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Your finalized paper trades with details, realized profit margins, and SL/TP flags will populate here.",
                                fontSize = 10.sp,
                                color = CyberTextDim,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(closedTrades, key = { index, trade -> "closed_${trade.id}_$index" }) { index, trade ->
                    ClosedPositionCard(trade = trade)
                }
            }
        }

        // 4. ALPHABOT LIVE CONSOLE TERMINAL AND STATUS (only show status in paper trading tab only)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("paper_bot_status_card"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(if (botEnabled) Color(0xFF22C55E) else CyberAccentRed, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ALPHABOT QUANT ENGINE STATUS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGold,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (botEnabled) "ENGINE: RUNNING (Scanning CoinGecko API...)" else "ENGINE: STANDBY (Off)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberTextWhite
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("paper_bot_terminal_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF22C55E), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "ALPHABOT TELEMETRY FEED",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }

                        IconButton(
                            onClick = { viewModel.clearLogs() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Clear Terminal logs",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val filteredPaperLogs = scanLogs.filter { !it.contains("MEXC") }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color(0xFF020617), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        if (filteredPaperLogs.isEmpty()) {
                            Text(
                                "[SYSTEM STANDBY] Feed awaiting routing instructions...",
                                color = Color(0xFF64748B),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                reverseLayout = true
                            ) {
                                items(filteredPaperLogs.reversed()) { log ->
                                    Text(
                                        text = log,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = if (log.contains("🤖") || log.contains("🟢") || log.contains("🚀")) {
                                            Color(0xFF4ADE80)
                                        } else if (log.contains("🛑") || log.contains("❌") || log.contains("🔴")) {
                                            Color(0xFFF87171)
                                        } else {
                                            Color(0xFF38BDF8)
                                        },
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OpenPositionCard(trade: PaperTrade, onCloseClick: () -> Unit) {
    val isBuy = trade.signalType == "LONG"
    val pnlColor = if (trade.pnl >= 0.0) Color(0xFF15803D) else CyberAccentRed
    val sign = if (trade.pnl >= 0.0) "+" else ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("open_trade_item_${trade.id}"),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = trade.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CyberDark)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$${(trade.symbol as? String ?: "UNKNOWN").uppercase()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyberTextWhite
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CyberSurface)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = (trade.strategy as? String ?: "Manual Position").uppercase(),
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            if (trade.isOkxTrade) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CyberAccentGreen)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "OKX LIVE",
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CyberDark,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            if (trade.isMexcTrade) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CyberAccentGreen)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "MEXC LIVE",
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CyberDark,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                        Text(
                            text = trade.name,
                            fontSize = 10.sp,
                            color = CyberTextDim
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isBuy) Color(0xFFDCFCE7) else Color(0xFFFFDADA))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isBuy) "LONG" else "SHORT",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isBuy) Color(0xFF15803D) else Color(0xFF93000A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberDark, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("ENTRY PRICE", fontSize = 7.5.sp, color = CyberTextDim)
                    Text("$${formatPrice(trade.entryPrice)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberTextWhite)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("CURRENT PRICE", fontSize = 7.5.sp, color = CyberTextDim)
                    Text("$${formatPrice(trade.currentPrice)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberTextWhite)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("UNREALIZED P&L", fontSize = 7.5.sp, color = CyberTextDim)
                    Text("$sign$${formatCurrency(trade.pnl)}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = pnlColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text("SL", fontSize = 7.sp, color = CyberAccentRed, fontWeight = FontWeight.Bold)
                        Text("$${formatPrice(trade.stopLoss)}", fontSize = 9.sp, color = CyberTextWhite)
                    }
                    Column {
                        Text("TP", fontSize = 7.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                        Text("$${formatPrice(trade.takeProfit)}", fontSize = 9.sp, color = CyberTextWhite)
                    }
                    Column {
                        Text("SIZE", fontSize = 7.sp, color = CyberTextDim, fontWeight = FontWeight.Bold)
                        Text("$${String.format(java.util.Locale.US, "%.1f", trade.investedAmount)}", fontSize = 9.sp, color = CyberTextWhite)
                    }
                }

                Button(
                    onClick = onCloseClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberAccentRed, contentColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(28.dp).testTag("close_position_btn_${trade.id}"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CLOSE POSITION", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ClosedPositionCard(trade: PaperTrade) {
    val isBuy = trade.signalType == "LONG"
    val isProfit = trade.pnl >= 0.0
    val pnlColor = if (isProfit) Color(0xFF15803D) else CyberAccentRed
    val sign = if (isProfit) "+" else ""
    
    val statusText = when (trade.status) {
        "CLOSED_TP" -> "TAKE PROFIT HIT"
        "CLOSED_SL" -> "STOP LOSS TRIGGERED"
        else -> "MANUAL SETTLED"
    }
    
    val statusColor = when (trade.status) {
        "CLOSED_TP" -> Color(0xFF15803D)
        "CLOSED_SL" -> CyberAccentRed
        else -> CyberTextDim
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = trade.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(CyberDark)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$${(trade.symbol as? String ?: "UNKNOWN").uppercase()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberDark)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = (trade.strategy as? String ?: "Manual Position").uppercase(),
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            if (trade.isOkxTrade) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyberAccentGreen.copy(alpha = 0.2f))
                                        .border(0.5.dp, CyberAccentGreen, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "OKX SPOT",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CyberAccentGreen,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            if (trade.isMexcTrade) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyberAccentGreen.copy(alpha = 0.2f))
                                        .border(0.5.dp, CyberAccentGreen, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "MEXC SPOT",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CyberAccentGreen,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = statusText,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = statusColor,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text("ENTRY PRICE", fontSize = 7.sp, color = CyberTextDim)
                        Text("$${formatPrice(trade.entryPrice)}", fontSize = 10.sp, color = CyberTextWhite)
                    }
                    Column {
                        Text("EXIT PRICE", fontSize = 7.sp, color = CyberTextDim)
                        Text("$${formatPrice(trade.exitPrice ?: trade.currentPrice)}", fontSize = 10.sp, color = CyberTextWhite)
                    }
                    Column {
                        Text("POSITION SIZE", fontSize = 7.sp, color = CyberTextDim)
                        Text("$${String.format(java.util.Locale.US, "%.1f", trade.investedAmount)}", fontSize = 10.sp, color = CyberTextWhite)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("REALIZED P&L", fontSize = 7.sp, color = CyberTextDim)
                    Text("$sign$${formatCurrency(trade.pnl)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = pnlColor)
                }
            }
        }
    }
}

@Composable
fun AutoBotTradingConsoleTab(viewModel: CryptoViewModel) {
    val botEnabled by viewModel.botEnabled.collectAsState()
    val botMaxTrades by viewModel.botMaxDailyTrades.collectAsState()
    val botSelectionMode by viewModel.botSelectionMode.collectAsState()
    val botTradeSize by viewModel.botTradeSize.collectAsState()
    val botSelectedBlueprints by viewModel.botSelectedBlueprints.collectAsState()
    val cashBalance by viewModel.cashBalance.collectAsState()
    val openTrades by viewModel.openTrades.collectAsState()
    val scanLogs by viewModel.scanLogs.collectAsState()
    val botTargetCoinMode by viewModel.botTargetCoinMode.collectAsState()
    val botSelectedCoinIds by viewModel.botSelectedCoinIds.collectAsState()
    val scannedCoins by viewModel.scannedCoins.collectAsState()

    val mexcEnabled by viewModel.mexcEnabled.collectAsState()
    val mexcIsDemo by viewModel.mexcIsDemo.collectAsState()
    val mexcApiKey by viewModel.mexcApiKey.collectAsState()
    val mexcSecretKey by viewModel.mexcSecretKey.collectAsState()
    val mexcConnectionStatus by viewModel.mexcConnectionStatus.collectAsState()
    val mexcBalance by viewModel.mexcBalance.collectAsState()

    val blueprints = listOf(
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

    val mexcDemoBalance by viewModel.mexcDemoBalance.collectAsState()

    var maxTradesInput by remember { mutableStateOf(botMaxTrades.toString()) }
    var tradeSizeInput by remember { mutableStateOf(String.format(java.util.Locale.US, "%.0f", botTradeSize)) }
    var botDemoBalanceInput by remember { mutableStateOf(String.format(java.util.Locale.US, "%.0f", mexcDemoBalance)) }

    LaunchedEffect(botMaxTrades, botTradeSize, mexcDemoBalance) {
        maxTradesInput = botMaxTrades.toString()
        tradeSizeInput = String.format(java.util.Locale.US, "%.0f", botTradeSize)
        botDemoBalanceInput = String.format(java.util.Locale.US, "%.0f", mexcDemoBalance)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("auto_bot_console_tab_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. ENGINE RUNTIME CONTROLLER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_engine_control_card"),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(if (botEnabled) Color(0xFF22C55E) else CyberAccentRed, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ALPHABOT QUANT ENGINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (botEnabled) "BOT ON: Active Market Sweep Route" else "BOT OFF: Engine Standby",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                            )
                        }

                        Switch(
                            checked = botEnabled,
                            onCheckedChange = { viewModel.setBotEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF22C55E),
                                uncheckedThumbColor = CyberTextDim,
                                uncheckedTrackColor = CyberSurface
                            ),
                            modifier = Modifier.testTag("bot_toggle_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "MOCK CASH BALANCE",
                                fontSize = 8.sp,
                                color = CyberTextDim,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$${formatCurrency(cashBalance)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                            )
                        }

                        Column(modifier = Modifier.weight(1.2f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "MEXC DEMO BALANCE",
                                fontSize = 8.sp,
                                color = CyberTextDim,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$${formatCurrency(mexcDemoBalance)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberAccentGreen
                            )
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text(
                                "ACTIVE BOT ENGAGEMENTS",
                                fontSize = 8.sp,
                                color = CyberTextDim,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${openTrades.size} / $botMaxTrades Positions",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = if (openTrades.size >= botMaxTrades) CyberAccentRed else CyberAccentGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = maxTradesInput,
                            onValueChange = {
                                maxTradesInput = it
                                it.toIntOrNull()?.let { num ->
                                    viewModel.setBotMaxDailyTrades(num.coerceIn(1, 100))
                                }
                            },
                            label = { Text("Max Trades (1-100)", color = CyberTextDim, fontSize = 9.sp) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = tradeSizeInput,
                            onValueChange = {
                                tradeSizeInput = it
                                it.toDoubleOrNull()?.let { size ->
                                    viewModel.setBotTradeSize(size)
                                }
                            },
                            label = { Text("Cost per Trade ($)", color = CyberTextDim, fontSize = 9.sp) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = botDemoBalanceInput,
                            onValueChange = {
                                botDemoBalanceInput = it
                                it.toDoubleOrNull()?.let { dbal ->
                                    viewModel.setMexcDemoBalance(dbal)
                                }
                            },
                            label = { Text("MEXC Demo Capital ($)", color = CyberTextDim, fontSize = 9.sp) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberAccentGreen, fontSize = 11.sp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                viewModel.setMexcDemoBalance(10000.0)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberSlate,
                                contentColor = CyberTextWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(
                                "RESET",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // 2. TARGET COIN FILTER CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_target_coin_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "TARGET COIN CRUSADER RANGE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Mode switch (ALL / CUSTOM)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("ALL", "CUSTOM").forEach { mode ->
                            val isSelected = botTargetCoinMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberSlate else Color.Transparent)
                                    .clickable { viewModel.setBotTargetCoinMode(mode) }
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                                    .testTag("target_mode_$mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setBotTargetCoinMode(mode) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberAccentGreen,
                                            unselectedColor = CyberTextDim
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (mode == "ALL") "ALL COINS" else "SPECIFIC COINS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim
                                    )
                                }
                            }
                        }
                    }

                    if (botTargetCoinMode == "CUSTOM") {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "SELECT TARGET COINS FOR STRATEGY EXECUTION",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (scannedCoins.isEmpty()) {
                            Text(
                                text = "No scanned assets available. Execute market scan or load defaults.",
                                fontSize = 11.sp,
                                color = CyberTextDim
                            )
                        } else {
                            scannedCoins.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { coin ->
                                        val isCoinSelected = botSelectedCoinIds.contains(coin.id)
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isCoinSelected) CyberSlate.copy(alpha = 0.4f) else CyberDark)
                                                .clickable { viewModel.toggleBotSelectedCoin(coin.id) }
                                                .border(
                                                    1.dp,
                                                    if (isCoinSelected) CyberAccentGreen.copy(alpha = 0.6f) else Color.Transparent,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isCoinSelected,
                                                onCheckedChange = { viewModel.toggleBotSelectedCoin(coin.id) },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = CyberAccentGreen,
                                                    uncheckedColor = CyberTextDim
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = coin.symbol.uppercase(),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCoinSelected) CyberAccentGreen else CyberTextWhite
                                                )
                                                Text(
                                                    text = coin.name,
                                                    fontSize = 9.sp,
                                                    color = CyberTextDim,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                    if (pair.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${botSelectedCoinIds.size} coin(s) selected",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )

                                if (botSelectedCoinIds.isNotEmpty()) {
                                    Text(
                                        text = "CLEAR ALL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberAccentRed,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier
                                            .clickable { viewModel.clearBotSelectedCoins() }
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. TARGET BLUEPRINTS SELECTOR CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_blueprints_selector_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "BLUEPRINT TARGETING PROTOCOL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("AUTO", "MANUAL").forEach { mode ->
                            val isSelected = botSelectionMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberSlate else Color.Transparent)
                                    .clickable { viewModel.setBotSelectionMode(mode) }
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                                    .testTag("mode_toggle_$mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setBotSelectionMode(mode) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberAccentGreen,
                                            unselectedColor = CyberTextDim
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (mode == "AUTO") "AUTO (All Strategies)" else "MANUAL (Whitelist)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "TARGET SELECTION RANGE",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        blueprints.forEach { blueprintTitle ->
                            val isWhitelisted = botSelectedBlueprints.contains(blueprintTitle)
                            val isEnabled = botSelectionMode == "MANUAL"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isWhitelisted && isEnabled) CyberSlate.copy(alpha = 0.4f) else CyberDark)
                                    .clickable(enabled = isEnabled) { viewModel.toggleBotBlueprint(blueprintTitle) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                if (blueprintTitle.contains("Sweep") || blueprintTitle.contains("Divergence")) CyberAccentRed else CyberAccentGreen,
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = blueprintTitle,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isEnabled) CyberTextWhite else CyberTextDim
                                    )
                                }

                                Checkbox(
                                    checked = if (isEnabled) isWhitelisted else true,
                                    onCheckedChange = { viewModel.toggleBotBlueprint(blueprintTitle) },
                                    enabled = isEnabled,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CyberAccentGreen,
                                        uncheckedColor = CyberTextDim
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/*
fun OldAutoBotTradingConsoleTab(viewModel: CryptoViewModel) {
    // Truncated commented-out duplicate code to prevent IDE search collisions and compilation errors
    // Since this is 100% commented-out and disabled code, we safely simplify it.
*/
/*
    Text(
        text = if (botEnabled) "BOT ON: Active Market Sweep Route" else "BOT OFF: Engine Standby",
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                            )
                        }

                        Switch(
                            checked = botEnabled,
                            onCheckedChange = { viewModel.setBotEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF22C55E),
                                uncheckedThumbColor = CyberTextDim,
                                uncheckedTrackColor = CyberSurface
                            ),
                            modifier = Modifier.testTag("bot_toggle_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "MOCK CASH BALANCE",
                                fontSize = 8.sp,
                                color = CyberTextDim,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$${formatCurrency(cashBalance)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                            )
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text(
                                "ACTIVE BOT ENGAGEMENTS",
                                fontSize = 8.sp,
                                color = CyberTextDim,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${openTrades.size} / $botMaxTrades Positions",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = if (openTrades.size >= botMaxTrades) CyberAccentRed else CyberAccentGreen
                            )
                        }
                    }
                }            // 1B. CONSOLIDATED EXCHANGE SETUP CARD WITH DROP DOWN TABS (OKX / MEXC)
        item {
            var okxApiKeyInput by remember { mutableStateOf(okxApiKey) }
            var okxSecretKeyInput by remember { mutableStateOf(okxSecretKey) }
            var okxPassphraseInput by remember { mutableStateOf(okxPassphrase) }

            var mexcApiKeyInput by remember { mutableStateOf(mexcApiKey) }
            var mexcSecretKeyInput by remember { mutableStateOf(mexcSecretKey) }

            // Sync with viewModel when credentials change in DB or SharedPreferences
            LaunchedEffect(okxApiKey, okxSecretKey, okxPassphrase) {
                okxApiKeyInput = okxApiKey
                okxSecretKeyInput = okxSecretKey
                okxPassphraseInput = okxPassphrase
            }
            LaunchedEffect(mexcApiKey, mexcSecretKey) {
                mexcApiKeyInput = mexcApiKey
                mexcSecretKeyInput = mexcSecretKey
            }

            // Drop down tabs state
            var selectedExchangeTab by remember { mutableStateOf("OKX") } // "OKX" or "MEXC"
            var dropdownExpanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("exchange_setup_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Title and Routing Switches
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "EXCHANGE API GATEWAY ROUTING",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGold,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$selectedExchangeTab API Protocol",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                            )
                        }

                        // Switch connected to current selected exchange
                        val currentEnabled = if (selectedExchangeTab == "OKX") okxEnabled else mexcEnabled
                        Switch(
                            checked = currentEnabled,
                            onCheckedChange = { isChecked ->
                                if (selectedExchangeTab == "OKX") {
                                    viewModel.setOkxEnabled(isChecked)
                                } else {
                                    viewModel.setMexcEnabled(isChecked)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CyberAccentGreen,
                                uncheckedThumbColor = CyberTextDim,
                                uncheckedTrackColor = CyberSurface
                            ),
                            modifier = Modifier.testTag("exchange_routing_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- DROP DOWN TABS FOR SELECTING OKX, MEXC ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SELECT ACTIVE EXCHANGE",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        
                        Box {
                            // Dropdown selector trigger
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberDark)
                                    .clickable { dropdownExpanded = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedExchangeTab == "OKX") "🔥 OKX GLOBAL" else "⚡ MEXC PRO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyberAccentGreen,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Drop Down Tabs",
                                    tint = CyberAccentGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier.background(CyberCard)
                            ) {
                                DropdownMenuItem(
                                    text = { 
                                        Text(
                                            "OKX Global Exchange", 
                                            color = CyberTextWhite, 
                                            fontSize = 11.sp, 
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        ) 
                                    },
                                    onClick = {
                                        selectedExchangeTab = "OKX"
                                        dropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { 
                                        Text(
                                            "MEXC Pro Exchange", 
                                            color = CyberTextWhite, 
                                            fontSize = 11.sp, 
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        ) 
                                    },
                                    onClick = {
                                        selectedExchangeTab = "MEXC"
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Inline tabs row for beautiful alternative selection
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("OKX", "MEXC").forEach { exch ->
                            val isSelected = selectedExchangeTab == exch
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberSlate else Color.Transparent)
                                    .clickable { selectedExchangeTab = exch }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (exch == "OKX") "OKX GLOBAL CONNECTION" else "MEXC PLATFORM CONNECTION",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) CyberAccentGreen else CyberTextDim,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedExchangeTab == "OKX") {
                        // --- OKX PANEL RENDER ---
                        // Balance and connection status banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberDark, RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "OKX EXCHANGE BALANCE",
                                    fontSize = 8.sp,
                                    color = CyberTextDim,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (okxEnabled && okxConnectionStatus.contains("Connected")) {
                                        "$${formatCurrency(okxBalance)} USDT"
                                    } else {
                                        "Locked / Standby"
                                    },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (okxEnabled && okxConnectionStatus.contains("Connected")) CyberAccentGreen else CyberTextDim
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "ROUTING GATEWAY",
                                    fontSize = 8.sp,
                                    color = CyberTextDim,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = okxConnectionStatus,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        okxConnectionStatus.contains("Connected") -> CyberAccentGreen
                                        okxConnectionStatus.contains("Connecting") -> CyberGold
                                        okxConnectionStatus.contains("Error") -> CyberAccentRed
                                        else -> CyberTextDim
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Demo vs Live Switch Mode
                        Text(
                            text = "EXECUTION ROUTE MODE (OKX)",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberDark, RoundedCornerShape(12.dp))
                                .padding(4.dp)
                        ) {
                            listOf(true, false).forEach { isDemo ->
                                val isSelected = okxIsDemo == isDemo
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CyberSlate else Color.Transparent)
                                        .clickable { viewModel.setOkxIsDemo(isDemo) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isDemo) "DEMO/SIMULATED TRADING" else "LIVE ORDER BOOK",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Form Key Inputs
                        Text(
                            text = "OKX MANUALLY CONFIGURED API KEY",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = okxApiKeyInput,
                            onValueChange = { okxApiKeyInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("okx_api_key_input"),
                            placeholder = { Text("Enter your OKX API Key", color = CyberTextDim.copy(alpha = 0.4f), fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "OKX API SECRET KEY",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = okxSecretKeyInput,
                            onValueChange = { okxSecretKeyInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("okx_secret_key_input"),
                            placeholder = { Text("Enter your OKX Secret Key", color = CyberTextDim.copy(alpha = 0.4f), fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "OKX API PASSPHRASE",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = okxPassphraseInput,
                            onValueChange = { okxPassphraseInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("okx_passphrase_input"),
                            placeholder = { Text("Enter your OKX Passphrase", color = CyberTextDim.copy(alpha = 0.4f), fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.saveOkxCredentials(
                                    apiKey = okxApiKeyInput.trim(),
                                    secretKey = okxSecretKeyInput.trim(),
                                    passphrase = okxPassphraseInput.trim()
                                )
                            },
                            enabled = okxApiKeyInput.isNotBlank() && okxSecretKeyInput.isNotBlank() && okxPassphraseInput.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("okx_connect_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (okxApiKeyInput.isNotBlank() && okxSecretKeyInput.isNotBlank() && okxPassphraseInput.isNotBlank()) {
                                    CyberAccentGreen
                                } else {
                                    CyberSlate
                                },
                                disabledContainerColor = CyberSlate
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (okxApiKeyInput.isNotBlank() && okxSecretKeyInput.isNotBlank() && okxPassphraseInput.isNotBlank()) CyberDark else CyberTextDim,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "TEST & SAVE OKX CONFIG",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (okxApiKeyInput.isNotBlank() && okxSecretKeyInput.isNotBlank() && okxPassphraseInput.isNotBlank()) CyberDark else CyberTextDim,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    } else {
                        // --- MEXC PANEL RENDER ---
                        // Balance and connection status banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberDark, RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "MEXC EXCHANGE BALANCE",
                                    fontSize = 8.sp,
                                    color = CyberTextDim,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (mexcEnabled && mexcConnectionStatus.contains("Connected")) {
                                        "$${formatCurrency(mexcBalance)} USDT"
                                    } else {
                                        "Locked / Standby"
                                    },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (mexcEnabled && mexcConnectionStatus.contains("Connected")) CyberAccentGreen else CyberTextDim
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "ROUTING GATEWAY",
                                    fontSize = 8.sp,
                                    color = CyberTextDim,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = mexcConnectionStatus,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        mexcConnectionStatus.contains("Connected") -> CyberAccentGreen
                                        mexcConnectionStatus.contains("Connecting") -> CyberGold
                                        mexcConnectionStatus.contains("Error") -> CyberAccentRed
                                        else -> CyberTextDim
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Demo vs Live Switch Mode
                        Text(
                            text = "EXECUTION ROUTE MODE (MEXC)",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberDark, RoundedCornerShape(12.dp))
                                .padding(4.dp)
                        ) {
                            listOf(true, false).forEach { isDemo ->
                                val isSelected = mexcIsDemo == isDemo
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CyberSlate else Color.Transparent)
                                        .clickable { viewModel.setMexcIsDemo(isDemo) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isDemo) "DEMO/SIMULATED TRADING" else "LIVE ORDER BOOK",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Form Key Inputs
                        Text(
                            text = "MEXC API ACCESS KEY (MANUAL)",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = mexcApiKeyInput,
                            onValueChange = { mexcApiKeyInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mexc_api_key_input"),
                            placeholder = { Text("Enter your MEXC Access Key", color = CyberTextDim.copy(alpha = 0.4f), fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "MEXC API SECRET SIGN KEY (MANUAL)",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = mexcSecretKeyInput,
                            onValueChange = { mexcSecretKeyInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mexc_secret_key_input"),
                            placeholder = { Text("Enter your MEXC Secret Key", color = CyberTextDim.copy(alpha = 0.4f), fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.saveMexcCredentials(
                                    apiKey = mexcApiKeyInput.trim(),
                                    secretKey = mexcSecretKeyInput.trim()
                                )
                            },
                            enabled = mexcApiKeyInput.isNotBlank() && mexcSecretKeyInput.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("mexc_connect_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (mexcApiKeyInput.isNotBlank() && mexcSecretKeyInput.isNotBlank()) {
                                    CyberAccentGreen
                                } else {
                                    CyberSlate
                                },
                                disabledContainerColor = CyberSlate
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (mexcApiKeyInput.isNotBlank() && mexcSecretKeyInput.isNotBlank()) CyberDark else CyberTextDim,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "TEST & SAVE MEXC CONFIG",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (mexcApiKeyInput.isNotBlank() && mexcSecretKeyInput.isNotBlank()) CyberDark else CyberTextDim,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. BOT PARAMETERS CONFIGURATION
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_parameters_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CORE EXECUTION CONTROLS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // A: Max concurrent trades
                    Text(
                        text = "MAX CONCURRENT ACTIVE POSITION LIMIT (1 - 50)",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var maxTradesInput by remember { mutableStateOf(botMaxTrades.toString()) }
                        var isMaxFocused by remember { mutableStateOf(false) }
                        LaunchedEffect(botMaxTrades) {
                            if (!isMaxFocused) {
                                maxTradesInput = botMaxTrades.toString()
                            }
                        }

                        OutlinedTextField(
                            value = maxTradesInput,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.filter { it.isDigit() }
                                if (digitsOnly.length <= 2) {
                                    maxTradesInput = digitsOnly
                                    val parsed = digitsOnly.toIntOrNull()
                                    if (parsed != null && parsed in 1..50) {
                                        viewModel.setBotMaxDailyTrades(parsed)
                                        viewModel.saveBotMaxDailyTrades(parsed)
                                    }
                                } else if (digitsOnly.isEmpty()) {
                                    maxTradesInput = ""
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { isMaxFocused = it.isFocused }
                                .testTag("bot_max_trades_input"),
                            label = { Text("Limit (1-50)", color = CyberTextDim, fontSize = 11.sp) },
                            placeholder = { Text("10", color = CyberTextDim.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedLabelColor = CyberAccentGreen,
                                unfocusedLabelColor = CyberTextDim,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(CyberSlate, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$botMaxTrades limit",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // B: Trade size allocation size
                    Text(
                        text = "VIRTUAL CAPITAL PER POSITION SIZE ($)",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var sizeInput by remember { mutableStateOf(botTradeSize.toInt().toString()) }
                        var isSizeFocused by remember { mutableStateOf(false) }
                        LaunchedEffect(botTradeSize) {
                            if (!isSizeFocused) {
                                sizeInput = botTradeSize.toInt().toString()
                            }
                        }

                        OutlinedTextField(
                            value = sizeInput,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.filter { it.isDigit() }.take(7)
                                sizeInput = digitsOnly
                                val parsed = digitsOnly.toDoubleOrNull()
                                if (parsed != null && parsed >= 1.0) {
                                    viewModel.setBotTradeSize(parsed)
                                    viewModel.saveBotTradeSize(parsed)
                                } else if (digitsOnly.isEmpty()) {
                                    sizeInput = ""
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { isSizeFocused = it.isFocused }
                                .testTag("bot_trade_size_input"),
                            label = { Text("Capital Amount ($)", color = CyberTextDim, fontSize = 11.sp) },
                            placeholder = { Text("1000", color = CyberTextDim.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedLabelColor = CyberAccentGreen,
                                unfocusedLabelColor = CyberTextDim,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(CyberSlate, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$${formatCurrency(botTradeSize)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // C: Custom Risk-to-Reward Ratio Input Configuration
                    Text(
                        text = "CUSTOM RISK-TO-REWARD RATIO (REWARD : RISK)",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val customRR by viewModel.customRiskRewardRatio.collectAsState()
                        var rrInput by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", customRR)) }
                        var isRrFocused by remember { mutableStateOf(false) }
                        LaunchedEffect(customRR) {
                            if (!isRrFocused) {
                                rrInput = String.format(java.util.Locale.US, "%.1f", customRR)
                            }
                        }

                        OutlinedTextField(
                            value = rrInput,
                            onValueChange = { newValue ->
                                rrInput = newValue
                                val parsed = newValue.toDoubleOrNull()
                                if (parsed != null && parsed >= 1.0 && parsed <= 10.0) {
                                    viewModel.setCustomRiskRewardRatio(parsed)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { isRrFocused = it.isFocused }
                                .testTag("bot_custom_rr_input"),
                            label = { Text("Ratio (1.0 - 10.0)", color = CyberTextDim, fontSize = 11.sp) },
                            placeholder = { Text("2.0", color = CyberTextDim.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberAccentGreen,
                                unfocusedBorderColor = CyberSurface,
                                focusedTextColor = CyberTextWhite,
                                unfocusedTextColor = CyberTextWhite,
                                focusedLabelColor = CyberAccentGreen,
                                unfocusedLabelColor = CyberTextDim,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(CyberSlate, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", customRR)} : 1.0",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen
                            )
                        }
                    }
                }
            }
        }

        // 2B. ASSET TARGETING PROTOCOL CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_asset_targeting_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "ASSET TARGETING PROTOCOL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Radios for botTargetCoinMode: ALL vs CUSTOM
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("ALL", "CUSTOM").forEach { mode ->
                            val isSelected = botTargetCoinMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberSlate else Color.Transparent)
                                    .clickable { viewModel.setBotTargetCoinMode(mode) }
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                                    .testTag("coin_mode_toggle_$mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setBotTargetCoinMode(mode) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberAccentGreen,
                                            unselectedColor = CyberTextDim
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (mode == "ALL") "ALL COINS" else "SPECIFIC COINS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim
                                    )
                                }
                            }
                        }
                    }

                    if (botTargetCoinMode == "CUSTOM") {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "SELECT TARGET COINS FOR STRATEGY EXECUTION",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (scannedCoins.isEmpty()) {
                            Text(
                                text = "No scanned assets available. Execute market scan or load defaults.",
                                fontSize = 11.sp,
                                color = CyberTextDim
                            )
                        } else {
                            // Grid/row list of selectable coins
                            scannedCoins.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { coin ->
                                        val isCoinSelected = botSelectedCoinIds.contains(coin.id)
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isCoinSelected) CyberSlate.copy(alpha = 0.4f) else CyberDark)
                                                .clickable { viewModel.toggleBotSelectedCoin(coin.id) }
                                                .border(
                                                    1.dp,
                                                    if (isCoinSelected) CyberAccentGreen.copy(alpha = 0.6f) else Color.Transparent,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isCoinSelected,
                                                onCheckedChange = { viewModel.toggleBotSelectedCoin(coin.id) },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = CyberAccentGreen,
                                                    uncheckedColor = CyberTextDim
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = coin.symbol.uppercase(),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCoinSelected) CyberAccentGreen else CyberTextWhite
                                                 )
                                                 Text(
                                                     text = coin.name,
                                                     fontSize = 9.sp,
                                                     color = CyberTextDim,
                                                     maxLines = 1
                                                 )
                                            }
                                        }
                                    }
                                    if (pair.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${botSelectedCoinIds.size} coin(s) selected",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )

                                if (botSelectedCoinIds.isNotEmpty()) {
                                    Text(
                                        text = "CLEAR ALL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberAccentRed,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier
                                            .clickable { viewModel.clearBotSelectedCoins() }
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. TARGET BLUEPRINTS SELECTOR CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_blueprints_selector_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "BLUEPRINT TARGETING PROTOCOL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Radio toggles for Selection mode
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("AUTO", "MANUAL").forEach { mode ->
                            val isSelected = botSelectionMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberSlate else Color.Transparent)
                                    .clickable { viewModel.setBotSelectionMode(mode) }
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                                    .testTag("mode_toggle_$mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setBotSelectionMode(mode) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberAccentGreen,
                                            unselectedColor = CyberTextDim
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (mode == "AUTO") "AUTO (All Strategies)" else "MANUAL (Whitelist)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Blueprint Whitelist Options
                    Text(
                        text = "TARGET SELECTION RANGE",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        blueprints.forEach { blueprintTitle ->
                            val isWhitelisted = botSelectedBlueprints.contains(blueprintTitle)
                            val isEnabled = botSelectionMode == "MANUAL"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isWhitelisted && isEnabled) CyberSlate.copy(alpha = 0.4f) else CyberDark)
                                    .clickable(enabled = isEnabled) { viewModel.toggleBotBlueprint(blueprintTitle) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                if (blueprintTitle.contains("Sweep") || blueprintTitle.contains("Divergence")) CyberAccentRed else CyberAccentGreen,
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = blueprintTitle,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isEnabled) CyberTextWhite else CyberTextDim
                                    )
                                }

                                Checkbox(
                                    checked = if (isEnabled) isWhitelisted else true,
                                    onCheckedChange = { viewModel.toggleBotBlueprint(blueprintTitle) },
                                    enabled = isEnabled,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CyberAccentGreen,
                                        uncheckedColor = CyberTextDim
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. ALPHABOT LIVE CONSOLE TERMINAL
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_terminal_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF22C55E), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "ALPHABOT TELEMETRY FEED",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }

                        IconButton(
                            onClick = { viewModel.clearLogs() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Clear Terminal logs",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF020617), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        if (scanLogs.isEmpty()) {
                            Text(
                                "[SYSTEM STANDBY] Feed awaiting routing instructions...",
                                color = Color(0xFF64748B),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                reverseLayout = true
                            ) {
                                items(scanLogs.reversed()) { log ->
                                    Text(
                                        text = log,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = if (log.contains("🤖") || log.contains("🟢") || log.contains("🚀")) {
                                            Color(0xFF4ADE80)
                                        } else if (log.contains("🛑") || log.contains("❌") || log.contains("🔴")) {
                                            Color(0xFFF87171)
                                        } else {
                                            Color(0xFF38BDF8)
                                        },
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
*/

@Composable
fun MexcTradingConsoleTab(viewModel: CryptoViewModel) {
    val mexcEnabled by viewModel.mexcEnabled.collectAsState()
    val mexcIsDemo by viewModel.mexcIsDemo.collectAsState()
    val mexcApiKey by viewModel.mexcApiKey.collectAsState()
    val mexcSecretKey by viewModel.mexcSecretKey.collectAsState()
    val mexcConnectionStatus by viewModel.mexcConnectionStatus.collectAsState()
    val mexcBalance by viewModel.mexcBalance.collectAsState()
    val mexcDemoBalance by viewModel.mexcDemoBalance.collectAsState()
    
    val mexcBotEnabled by viewModel.mexcBotEnabled.collectAsState()
    val mexcBotMaxTrades by viewModel.mexcBotMaxTrades.collectAsState()
    val mexcBotTradeSize by viewModel.mexcBotTradeSize.collectAsState()
    val mexcBotSelectionMode by viewModel.mexcBotSelectionMode.collectAsState()
    val mexcBotSelectedBlueprints by viewModel.mexcBotSelectedBlueprints.collectAsState()
    val mexcBotScanModeByViewModel by viewModel.mexcBotScanMode.collectAsState()
    val mexcBotScanMode = mexcBotScanModeByViewModel // resolve any potential naming clash

    val mexcBotTargetCoinMode by viewModel.mexcBotTargetCoinMode.collectAsState()
    val mexcBotSelectedCoinIds by viewModel.mexcBotSelectedCoinIds.collectAsState()
    val scannedCoins by viewModel.scannedCoins.collectAsState()
    
    val openTradesAll by viewModel.openTrades.collectAsState()
    val closedTradesAll by viewModel.closedTrades.collectAsState()
    val mexcOpenTrades = openTradesAll.filter { it.isMexcTrade }
    val mexcClosedTrades = closedTradesAll.filter { it.isMexcTrade }
    val scanLogs by viewModel.scanLogs.collectAsState()

    var apiKeyInput by remember { mutableStateOf(mexcApiKey) }
    var secretKeyInput by remember { mutableStateOf(mexcSecretKey) }
    
    LaunchedEffect(mexcApiKey, mexcSecretKey) {
        apiKeyInput = mexcApiKey
        secretKeyInput = mexcSecretKey
    }

    var parallelTradesInput by remember { mutableStateOf(mexcBotMaxTrades.toString()) }
    var demoBalanceInput by remember { mutableStateOf(String.format(Locale.US, "%.0f", mexcDemoBalance)) }
    var tradeCostInput by remember { mutableStateOf(String.format(Locale.US, "%.0f", mexcBotTradeSize)) }
    
    LaunchedEffect(mexcBotMaxTrades, mexcDemoBalance, mexcBotTradeSize) {
        parallelTradesInput = mexcBotMaxTrades.toString()
        demoBalanceInput = String.format(Locale.US, "%.0f", mexcDemoBalance)
        tradeCostInput = String.format(Locale.US, "%.0f", mexcBotTradeSize)
    }

    var isGatewayExpanded by remember { mutableStateOf(mexcApiKey.isBlank() || !mexcConnectionStatus.contains("Connected")) }
    
    LaunchedEffect(mexcConnectionStatus) {
        if (mexcConnectionStatus.contains("Connected")) {
            isGatewayExpanded = false
        }
    }

    val blueprints = listOf(
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

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("mexc_console_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // --- 1. MEXC GATEWAY INTEGRATION ---
        item {
            val isConnected = mexcConnectionStatus.contains("Connected")
            if (isConnected && !isGatewayExpanded) {
                // MINIMIZED STATE
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("mexc_api_gateway_card_minimized"),
                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(if (mexcEnabled) CyberAccentGreen else CyberGold, androidx.compose.foundation.shape.CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("MEXC PREMIUM GATEWAY ROUTER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                                Text(
                                    text = if (mexcEnabled) "GATEWAY: ACTIVE (CONNECTED)" else "GATEWAY: STANDBY (CONNECTED)", 
                                    fontSize = 12.sp, 
                                    fontWeight = FontWeight.Bold, 
                                    color = CyberTextWhite
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Balance: $${formatCurrency(mexcBalance)} USDT",
                                    fontSize = 11.sp,
                                    color = CyberAccentGreen,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { isGatewayExpanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("MANAGE API", color = CyberAccentGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Switch(
                                checked = mexcEnabled,
                                onCheckedChange = { viewModel.setMexcEnabled(it) },
                                colors = SwitchDefaults.colors(checkedTrackColor = CyberAccentGreen)
                            )
                        }
                    }
                }
            } else {
                // EXPANDED STATE
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("mexc_api_gateway_card"),
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
                            Column {
                                Text("MEXC PREMIUM GATEWAY ROUTING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                                Text(if (mexcEnabled) "GATEWAY: ACTIVE" else "GATEWAY: STANDBY", fontSize = 18.sp, fontWeight = FontWeight.Black, color = CyberTextWhite)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (isConnected) {
                                    Button(
                                        onClick = { isGatewayExpanded = false },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("MINIMIZE", color = CyberTextWhite, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                Switch(
                                    checked = mexcEnabled,
                                    onCheckedChange = { viewModel.setMexcEnabled(it) },
                                    colors = SwitchDefaults.colors(checkedTrackColor = CyberAccentGreen)
                                )
                            }
                        }
                        
                        if (isConnected) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(CyberAccentGreen, androidx.compose.foundation.shape.CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("CONNECTED TO MEXC API", fontSize = 8.sp, color = CyberTextDim, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    Text("BALANCE: $${formatCurrency(mexcBalance)} USDT", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyberAccentGreen, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = apiKeyInput, onValueChange = { apiKeyInput = it },
                            label = { Text("MEXC Access API Key", color = CyberTextDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = secretKeyInput, onValueChange = { secretKeyInput = it },
                            label = { Text("MEXC Signature Secret Key", color = CyberTextDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(mexcConnectionStatus, fontSize = 11.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                            Button(
                                onClick = { viewModel.saveMexcCredentials(apiKeyInput, secretKeyInput) },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurface)
                            ) {
                                Text("SAVE & INTEGRATE API", color = CyberAccentGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        // --- 2. VECTOR ACCOUNT MODE CALIBRATION ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("ACCOUNT RUNTIME VECTOR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(4.dp)) {
                        Button(
                            onClick = { viewModel.setMexcIsDemo(true) }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (mexcIsDemo) CyberSurface else Color.Transparent)
                        ) {
                            Text("MOCK DEMO SPOT", fontSize = 9.sp, color = if (mexcIsDemo) CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace)
                        }
                        Button(
                            onClick = { viewModel.setMexcIsDemo(false) }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (!mexcIsDemo) CyberSurface else Color.Transparent)
                        ) {
                            Text("MEXC LIVE SPOT", fontSize = 9.sp, color = if (!mexcIsDemo) CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (mexcIsDemo) {
                        Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("SIMULATED ACCOUNT BALANCE", fontSize = 8.sp, color = CyberTextDim)
                                Text("$${formatCurrency(mexcDemoBalance)} USDT", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CyberAccentGreen)
                            }
                            Text("DEMO ENVIRONMENT", fontSize = 9.sp, color = CyberGold, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("REAL LIVE ACCOUNT BALANCE", fontSize = 8.sp, color = CyberTextDim)
                                Text("$${formatCurrency(mexcBalance)} USDT", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CyberAccentGreen)
                            }
                            Text("SECURE SPOT VECTOR", fontSize = 9.sp, color = CyberGold, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("BOT PROTOCOL EXECUTION CALIBRATION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = parallelTradesInput, onValueChange = { 
                                parallelTradesInput = it
                                it.toIntOrNull()?.let { v -> viewModel.setMexcBotMaxTrades(v.coerceIn(1, 50)) }
                            },
                            label = { Text("Max Trades (1-50)", color = CyberTextDim, fontSize = 9.sp) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp),
                            modifier = Modifier.weight(1f), singleLine = true
                        )
                        OutlinedTextField(
                            value = tradeCostInput, onValueChange = { 
                                tradeCostInput = it
                                it.toDoubleOrNull()?.let { v -> viewModel.setMexcBotTradeSize(v) }
                            },
                            label = { Text("Cost per Trade ($)", color = CyberTextDim, fontSize = 9.sp) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp),
                            modifier = Modifier.weight(1f), singleLine = true
                        )
                    }
                    
                    if (mexcIsDemo) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = demoBalanceInput, onValueChange = { 
                                    demoBalanceInput = it
                                    it.toDoubleOrNull()?.let { v -> viewModel.setMexcDemoBalance(v) }
                                },
                                label = { Text("Set Custom Demo Balance ($)", color = CyberTextDim, fontSize = 9.sp) },
                                textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp),
                                modifier = Modifier.weight(1.3f), singleLine = true
                            )
                            Button(
                                onClick = { viewModel.resetMexcDemoBalance() }, 
                                colors = ButtonDefaults.buttonColors(containerColor = CyberDark),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("RESET DEMO LEDGER", color = CyberAccentRed, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // --- 3. INDEPENDENT MEXC AUTO QUANT BOT ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        Column {
                            Text("INDEPENDENT MEXC CORE QUANT BOT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                            Text(if (mexcBotEnabled) "BOT STATUS: RUNNING" else "BOT STATUS: HOLD", fontSize = 16.sp, fontWeight = FontWeight.Black, color = CyberTextWhite)
                        }
                        Switch(
                            checked = mexcBotEnabled, onCheckedChange = { viewModel.setMexcBotEnabled(it) },
                            colors = SwitchDefaults.colors(checkedTrackColor = CyberAccentGreen)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("BOT RADAR SCAN TARGETS", fontSize = 9.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(4.dp)) {
                        Button(
                            onClick = { viewModel.setMexcBotScanMode("COINGECKO") }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (mexcBotScanMode == "COINGECKO") CyberSurface else Color.Transparent)
                        ) {
                            Text("COINGECKO MATCH", fontSize = 9.sp, color = if (mexcBotScanMode == "COINGECKO") CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace)
                        }
                        Button(
                            onClick = { viewModel.setMexcBotScanMode("MEXC_DIRECT") }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (mexcBotScanMode == "MEXC_DIRECT") CyberSurface else Color.Transparent)
                        ) {
                            Text("MEXC DIRECT SCAN", fontSize = 9.sp, color = if (mexcBotScanMode == "MEXC_DIRECT") CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("STRATEGY BLUEPRINT TARGET PROTOCOL", fontSize = 9.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(4.dp)) {
                        Button(
                            onClick = { viewModel.setMexcBotSelectionMode("AUTO") }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (mexcBotSelectionMode == "AUTO") CyberSurface else Color.Transparent)
                        ) {
                            Text("RUN ALL BLUEPRINTS", fontSize = 9.sp, color = if (mexcBotSelectionMode == "AUTO") CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace)
                        }
                        Button(
                            onClick = { viewModel.setMexcBotSelectionMode("CUSTOM") }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (mexcBotSelectionMode == "CUSTOM") CyberSurface else Color.Transparent)
                        ) {
                            Text("SELECT CUSTOM TARGETS", fontSize = 9.sp, color = if (mexcBotSelectionMode == "CUSTOM") CyberAccentGreen else CyberTextDim, fontFamily = FontFamily.Monospace)
                        }
                    }
                    
                    if (mexcBotSelectionMode == "AUTO") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = CyberAccentGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "🤖 ALL active Strategy Blueprints targets will trigger automated trade vectors independently on MEXC Spot.",
                                fontSize = 10.sp,
                                color = CyberAccentGreen,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    
                    if (mexcBotSelectionMode == "CUSTOM") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            blueprints.forEach { bp ->
                                val isSelected = mexcBotSelectedBlueprints.contains(bp)
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleMexcBotBlueprint(bp) }.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null, tint = if (isSelected) CyberAccentGreen else CyberTextDim,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(bp, fontSize = 10.sp, color = if (isSelected) CyberTextWhite else CyberTextDim, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("BOT ASSET SCOPE PROTOCOL", fontSize = 9.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("ALL", "CUSTOM").forEach { mode ->
                            val isSelected = mexcBotTargetCoinMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberSurface else Color.Transparent)
                                    .clickable { viewModel.setMexcBotTargetCoinMode(mode) }
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                                    .testTag("mexc_coin_mode_toggle_$mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.setMexcBotTargetCoinMode(mode) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberAccentGreen,
                                            unselectedColor = CyberTextDim
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (mode == "ALL") "ALL COINS" else "SPECIFIC COINS",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }

                    if (mexcBotTargetCoinMode == "CUSTOM") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "SELECT MEXC TARGET ASSETS",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (scannedCoins.isEmpty()) {
                            Text(
                                text = "No active scanned assets available to select. Run market scan first.",
                                fontSize = 10.sp,
                                color = CyberTextDim,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        } else {
                            scannedCoins.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { coin ->
                                        val isCoinSelected = mexcBotSelectedCoinIds.contains(coin.id)
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isCoinSelected) CyberSlate.copy(alpha = 0.4f) else CyberDark)
                                                .clickable { viewModel.toggleMexcBotSelectedCoin(coin.id) }
                                                .border(
                                                    1.dp,
                                                    if (isCoinSelected) CyberAccentGreen.copy(alpha = 0.6f) else Color.Transparent,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isCoinSelected,
                                                onCheckedChange = { viewModel.toggleMexcBotSelectedCoin(coin.id) },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = CyberAccentGreen,
                                                    uncheckedColor = CyberTextDim
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Column {
                                                Text(
                                                    text = coin.symbol.uppercase(),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCoinSelected) CyberAccentGreen else CyberTextWhite
                                                )
                                                Text(
                                                    text = coin.name,
                                                    fontSize = 8.sp,
                                                    color = CyberTextDim,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                    if (pair.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${mexcBotSelectedCoinIds.size} coin(s) selected",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )
                                if (mexcBotSelectedCoinIds.isNotEmpty()) {
                                    Text(
                                        text = "CLEAR ALL",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberAccentRed,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.clickable { viewModel.clearMexcBotSelectedCoins() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MexcTradesTab(viewModel: CryptoViewModel, isDemo: Boolean) {
    LaunchedEffect(isDemo) {
        viewModel.validateAndRefreshMexcBalance(isDemo)
    }
    
    val openTradesAll by viewModel.openTrades.collectAsState()
    val closedTradesAll by viewModel.closedTrades.collectAsState()
    val scanLogs by viewModel.scanLogs.collectAsState()
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
                                    Text((tr.symbol as? String ?: "UNKNOWN").uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyberTextWhite)
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
                            Text("SETUP JUSTIFICATION: ${tr.whyTradeReason.ifBlank { "System analysis algorithm trigger" }}", fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.closePaperTradeManually(tr) }, modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurface), shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("MANUAL EXIT FORCEFULLY", color = CyberAccentRed, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        } else {
            if (mexcClosedTrades.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("No closed $labelPrefix trade history logged.", color = CyberTextDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            } else {
                itemsIndexed(mexcClosedTrades, key = { index, tr -> "mexc_closed_${tr.id}_$index" }) { index, tr ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CyberCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text((tr.symbol as? String ?: "UNKNOWN").uppercase(), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyberTextWhite)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isDemo) "[DEMO]" else "[LIVE]", fontSize = 8.sp, color = CyberTextDim)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    val safeStatus = tr.status ?: "CLOSED"
                                    Text(safeStatus, color = if (safeStatus.contains("TP")) CyberAccentGreen else CyberAccentRed, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                }
                                Text("$${formatCurrency(tr.pnl)}", color = if (tr.pnl >= 0) CyberAccentGreen else CyberAccentRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("EP / EXIT PRICE", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.entryPrice)} / $${formatPrice(tr.exitPrice ?: tr.currentPrice)}", fontSize = 10.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("SL / TP", fontSize = 8.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.stopLoss)} / $${formatPrice(tr.takeProfit)}", fontSize = 10.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("CLOSED TIME", fontSize = 8.sp, color = CyberTextDim)
                                    val formattedTime = java.text.SimpleDateFormat("MMM dd, HH:mm:ss", java.util.Locale.US).format(java.util.Date(tr.exitTimestamp ?: tr.timestamp))
                                    Text(formattedTime, fontSize = 10.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("JUSTIFICATION: ${tr.whyTradeReason}", fontSize = 9.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // --- MEXC TELEMETRY TERMINAL FEED ---
        item {
            val filteredLogs = scanLogs.filter { log ->
                val isMexc = log.contains("MEXC") || log.contains("P&L Balancer")
                val isDemoLog = log.contains("DEMO") || log.contains("Simulated")
                if (isDemo) {
                    isMexc
                } else {
                    isMexc && !isDemoLog
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().testTag("mexc_terminal_card_${if (isDemo) "demo" else "live"}"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("$labelPrefix TELEMETRY STREAM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp)) {
                        if (filteredLogs.isEmpty()) {
                            Text("[SYSTEM STREAM] Awaiting $labelPrefix telemetry triggers...", color = Color(0xFF64748B), fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                reverseLayout = true
                            ) {
                                items(filteredLogs.reversed()) { log ->
                                    Text(
                                        text = log,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = if (log.contains("🤖") || log.contains("🟢") || log.contains("🚀") || log.contains("Connected")) {
                                            CyberAccentGreen
                                        } else if (log.contains("❌") || log.contains("🔴") || log.contains("Failed")) {
                                            CyberAccentRed
                                        } else {
                                            Color(0xFF38BDF8)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun buildDetailedShareReport(
    trades: List<PaperTrade>,
    winRate: Double,
    totalPnl: Double,
    maxDrawdown: Double,
    avgRR: Double,
    bestStrategy: String,
    bestCoin: String,
    directionPnl: Pair<Double, Double>,
    aiInsights: String
): String {
    val sb = java.lang.StringBuilder()
    sb.append("📊 *QUANTITATIVE STRATEGY METRICS & ANALYSIS REPORT* 📊\n")
    sb.append("=========================\n\n")
    sb.append("📈 *CORE PERFORMANCE STATISTICS*:\n")
    sb.append("• Total Transactions: ${trades.size} trades\n")
    val wins = trades.filter { it.pnl > 0.0 }.size
    val losses = trades.filter { it.pnl <= 0.0 }.size
    sb.append("• Win / Loss: $wins W - $losses L (Win Rate: ${String.format(java.util.Locale.US, "%.1f", winRate)}%)\n")
    sb.append("• Net Cumulative P&L: \$${String.format(java.util.Locale.US, "%.2f", totalPnl)} USDT\n")
    sb.append("• Max Portfolio Drawdown: \$${String.format(java.util.Locale.US, "%.2f", maxDrawdown)} USD\n")
    sb.append("• Average Risk-To-Reward Expectancy: ${String.format(java.util.Locale.US, "%.2f", avgRR)}:1\n")
    sb.append("• Optimal Active Strategy: $bestStrategy\n")
    sb.append("• Top Performer Asset: $bestCoin\n")
    sb.append("• Longs cumulative P&L: \$${String.format(java.util.Locale.US, "%.2f", directionPnl.first)} USDT\n")
    sb.append("• Shorts cumulative P&L: \$${String.format(java.util.Locale.US, "%.2f", directionPnl.second)} USDT\n")
    sb.append("\n")
    
    if (aiInsights.isNotBlank() && !aiInsights.contains("Analyzing local databases")) {
        sb.append("🧠 *AI DECISION ADVISORY & STRATEGY CRUNCH*:\n")
        val cleanInsights = aiInsights.replace(Regex("<[^>]*>"), "")
        sb.append(if (cleanInsights.length > 500) cleanInsights.take(500) + "..." else cleanInsights)
        sb.append("\n\n")
    }
    
    sb.append("📋 *SYSTEMATIC HISTORICAL TRANSACTION LOGS JOURNAL*:\n")
    sb.append("-------------------------\n")
    trades.forEachIndexed { idx, tr ->
        val statusEmoji = when {
            tr.status == "OPEN" -> "⏳"
            tr.pnl > 0.0 -> "🟢"
            else -> "🔴"
        }
        val pnlSign = if (tr.pnl >= 0.0) "+" else ""
        val pnlPct = if (tr.entryPrice > 0.0) {
            ((tr.exitPrice ?: tr.currentPrice) - tr.entryPrice) / tr.entryPrice * 100.0 * (if (tr.signalType == "LONG") 1.0 else -1.0)
        } else 0.0
        val cleanPnlPctVal = if (pnlPct.isNaN() || pnlPct.isInfinite()) 0.0 else pnlPct
        val pnlPctSign = if (cleanPnlPctVal >= 0.0) "+" else ""
        
        sb.append("${idx + 1}. $statusEmoji *[${tr.exchange ?: "UNKNOWN"}] ${(tr.symbol as? String ?: "UNKNOWN").uppercase()} (${tr.signalType ?: "LONG"})* - ${tr.status ?: "OPEN"}\n")
        sb.append("   • Strategy: ${tr.strategy ?: "Manual Position"}\n")
        sb.append("   • Entry Price: \$${String.format(java.util.Locale.US, "%.4f", tr.entryPrice)} | Exit/Current Price: \$${String.format(java.util.Locale.US, "%.4f", tr.exitPrice ?: tr.currentPrice)}\n")
        sb.append("   • Risk Parameters: SL: \$${String.format(java.util.Locale.US, "%.4f", tr.stopLoss)} | TP: \$${String.format(java.util.Locale.US, "%.4f", tr.takeProfit)}\n")
        sb.append("   • Net Log P&L: $pnlSign\$${String.format(java.util.Locale.US, "%.2f", tr.pnl)} USDT ($pnlPctSign${String.format(java.util.Locale.US, "%.1f", cleanPnlPctVal)}%)\n")
        sb.append("   • Leverage / Type: ${tr.leverage}x | Size: \$${String.format(java.util.Locale.US, "%.1f", tr.investedAmount)}\n")
        sb.append("   • Strategy Telemetry: Captured RSI: ${String.format(java.util.Locale.US, "%.1f", tr.rsi)} | Volatility: ${String.format(java.util.Locale.US, "%.1f", tr.volatility * 100.0)}% | Trend: ${tr.trend}\n")
        sb.append("   • Trade Rationale: ${tr.whyTradeReason.ifBlank { "Executed upon dynamic signal breakout validation." }}\n")
        sb.append("   • Timestamp: ${formatEpochToDate(tr.timestamp)}\n")
        sb.append("-------------------------\n")
    }
    sb.append("\n*Crypto Analytics Telemetry securely compiled & synced.*")
    return sb.toString()
}

fun shareToWhatsApp(context: android.content.Context, reportText: String) {
    try {
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, reportText)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            val whatsappIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                `package` = "com.whatsapp"
                putExtra(android.content.Intent.EXTRA_TEXT, reportText)
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(whatsappIntent)
        } catch (e: Exception) {
            val chooser = android.content.Intent.createChooser(shareIntent, "Share Strategy Audit Report").apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
            android.widget.Toast.makeText(context, "WhatsApp not installed. Opened standard sharing.", android.widget.Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        android.widget.Toast.makeText(context, "Unable to perform share operation: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
    }
}

fun generateExcelCsvString(trades: List<com.example.data.local.PaperTrade>): String {
    val sb = java.lang.StringBuilder()
    // UTF-8 BOM for Excel compatibility
    sb.append('\uFEFF')
    // Headers
    sb.append("Trade ID,Strategy,Coin,Direction,Entry Price,Exit Price,SL,TP,PnL (USD),PnL (%),Leverage,Invested Amount (USD),Quantity,Risk Reward Ratio,RSI,Volatility (%),Trend,Rationale,Exchange,Timestamp,Exit Timestamp\n")
    
    trades.forEach { tr ->
        val cleanStrategy = (tr.strategy ?: "Manual").replace("\"", "\"\"")
        val cleanRationale = ((tr.whyTradeReason ?: "").let { if (it.isBlank()) "Executed upon dynamic signal breakout validation." else it }).replace("\"", "\"\"")
        val pnlPct = if (tr.entryPrice > 0.0) {
            val directionFactor = if (tr.signalType == "LONG") 1.0 else -1.0
            val exitP = tr.exitPrice ?: tr.currentPrice
            val basePnl = ((exitP - tr.entryPrice) / tr.entryPrice) * 100.0 * directionFactor * tr.leverage
            if (basePnl.isNaN() || basePnl.isInfinite()) 0.0 else basePnl
        } else 0.0
        
        val safeVolatility = if (tr.volatility.isNaN() || tr.volatility.isInfinite()) 0.0 else tr.volatility
        val safePnl = if (tr.pnl.isNaN() || tr.pnl.isInfinite()) 0.0 else tr.pnl
        val safeInvested = if (tr.investedAmount.isNaN() || tr.investedAmount.isInfinite()) 0.0 else tr.investedAmount
        val safeQuantity = if (tr.quantity.isNaN() || tr.quantity.isInfinite()) 0.0 else tr.quantity
        val safeRsi = if (tr.rsi.isNaN() || tr.rsi.isInfinite()) 50.0 else tr.rsi
        val safeRiskReward = if (tr.riskRewardRatio.isNaN() || tr.riskRewardRatio.isInfinite()) 2.0 else tr.riskRewardRatio

        sb.append("${tr.id},")
        sb.append("\"$cleanStrategy\",")
        sb.append("${(tr.symbol ?: "").uppercase()},")
        sb.append("${tr.signalType ?: "LONG"},")
        sb.append("${tr.entryPrice},")
        sb.append("${tr.exitPrice ?: tr.currentPrice},")
        sb.append("${tr.stopLoss},")
        sb.append("${tr.takeProfit},")
        sb.append("${safePnl},")
        sb.append("${String.format(java.util.Locale.US, "%.2f", pnlPct)}%,")
        sb.append("${tr.leverage}x,")
        sb.append("${safeInvested},")
        sb.append("${safeQuantity},")
        sb.append("${safeRiskReward},")
        sb.append("${safeRsi},")
        sb.append("${String.format(java.util.Locale.US, "%.2f", safeVolatility * 100.0)}%,")
        sb.append("${tr.trend ?: "NEUTRAL"},")
        sb.append("\"$cleanRationale\",")
        sb.append("${tr.exchange ?: "UNKNOWN"},")
        sb.append("\"${formatEpochToDate(tr.timestamp)}\",")
        sb.append("\"${tr.exitTimestamp?.let { formatEpochToDate(it) } ?: "OPEN"}\"\n")
    }
    return sb.toString()
}

fun shareExcelFileToWhatsApp(context: android.content.Context, trades: List<com.example.data.local.PaperTrade>) {
    try {
        val csvText = generateExcelCsvString(trades)
        val file = java.io.File(context.cacheDir, "Crypto_Signals_Performance_Audit.csv")
        file.writeText(csvText, Charsets.UTF_8)

        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/comma-separated-values"
            putExtra(android.content.Intent.EXTRA_STREAM, uri)
            putExtra(android.content.Intent.EXTRA_SUBJECT, "Crypto Signals Strategy Performance Excel Audit")
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            val whatsappIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/comma-separated-values"
                `package` = "com.whatsapp"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(whatsappIntent)
        } catch (e: Throwable) {
            val chooser = android.content.Intent.createChooser(shareIntent, "Share Strategy Excel Report").apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
            android.widget.Toast.makeText(context, "WhatsApp not installed. Opened standard file share.", android.widget.Toast.LENGTH_SHORT).show()
        }
    } catch (e: Throwable) {
        android.widget.Toast.makeText(context, "Failed to share Excel report: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
    }
}

@Composable
fun TradeAnalyticsTab(viewModel: CryptoViewModel) {
    val allTransactions by viewModel.closedTrades.collectAsState()
    val allTradesList by viewModel.allTransactionsList.collectAsState()
    val aiInsights by viewModel.aiInsights.collectAsState()
    val isGeneratingAi by viewModel.isGeneratingAiInsights.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current

    // Filters States
    var selectedStrategyFilter by remember { mutableStateOf("ALL") }
    var selectedCoinFilter by remember { mutableStateOf("ALL") }
    var selectedTimeframeFilter by remember { mutableStateOf("ALL") }
    var selectedExchangeFilter by remember { mutableStateOf("ALL") }
    var selectedDirectionFilter by remember { mutableStateOf("ALL") }
    var exportStatusText by remember { mutableStateOf("") }

    // Logs Journal Specific State
    var logSearchQuery by remember { mutableStateOf("") }
    var logStatusFilter by remember { mutableStateOf("ALL") } // "ALL", "OPEN", "CLOSED"

    // Dynamic Filter Options
    val listStrategies = remember(allTransactions) {
        listOf("ALL") + allTransactions.map { it.strategy ?: "Manual Position" }.distinct().sorted()
    }
    val listCoins = remember(allTransactions) {
        listOf("ALL") + allTransactions.map { (it.symbol ?: "UNKNOWN").uppercase() }.distinct().sorted()
    }
    val listTimeframes = listOf("ALL", "5m", "15m", "1h", "4h")
    val listExchanges = listOf("ALL", "PAPER", "MEXC_DEMO", "MEXC_LIVE")
    val listDirections = listOf("ALL", "LONG", "SHORT")

    // Filtered trades
    val filteredTrades = remember(allTransactions, selectedStrategyFilter, selectedCoinFilter, selectedTimeframeFilter, selectedExchangeFilter, selectedDirectionFilter) {
        allTransactions.filter { tr ->
            val matchStrat = selectedStrategyFilter == "ALL" || (tr.strategy ?: "").uppercase() == selectedStrategyFilter.uppercase()
            val matchCoin = selectedCoinFilter == "ALL" || (tr.symbol ?: "").uppercase() == selectedCoinFilter.uppercase()
            val matchTf = selectedTimeframeFilter == "ALL" || (tr.timeframe ?: "") == selectedTimeframeFilter
            val matchEx = selectedExchangeFilter == "ALL" || (tr.exchange ?: "") == selectedExchangeFilter
            val matchDir = selectedDirectionFilter == "ALL" || (tr.signalType ?: "") == selectedDirectionFilter
            matchStrat && matchCoin && matchTf && matchEx && matchDir
        }
    }

    // Filtered Logs list specifically
    val filteredLogs = remember(allTradesList, logSearchQuery, logStatusFilter, selectedStrategyFilter, selectedCoinFilter, selectedTimeframeFilter, selectedExchangeFilter, selectedDirectionFilter) {
        allTradesList.filter { tr ->
            val symbolSafe = (tr.symbol ?: "").uppercase()
            val strategySafe = (tr.strategy ?: "").uppercase()
            val matchesSearch = symbolSafe.contains(logSearchQuery.trim().uppercase()) ||
                    strategySafe.contains(logSearchQuery.trim().uppercase())
            val matchesStatus = when (logStatusFilter) {
                "OPEN" -> (tr.status ?: "") == "OPEN"
                "CLOSED" -> (tr.status ?: "") != "OPEN"
                else -> true
            }
            val matchStrat = selectedStrategyFilter == "ALL" || (tr.strategy ?: "").uppercase() == selectedStrategyFilter.uppercase()
            val matchCoin = selectedCoinFilter == "ALL" || (tr.symbol ?: "").uppercase() == selectedCoinFilter.uppercase()
            val matchTf = selectedTimeframeFilter == "ALL" || (tr.timeframe ?: "") == selectedTimeframeFilter
            val matchEx = selectedExchangeFilter == "ALL" || (tr.exchange ?: "") == selectedExchangeFilter
            val matchDir = selectedDirectionFilter == "ALL" || (tr.signalType ?: "") == selectedDirectionFilter
            matchesSearch && matchesStatus && matchStrat && matchCoin && matchTf && matchEx && matchDir
        }
    }

    // Quantitative metrics
    val totalTrades = filteredTrades.size
    val winTrades = filteredTrades.filter { it.pnl > 0.0 }
    val lossTrades = filteredTrades.filter { it.pnl <= 0.0 }
    val winRate = if (totalTrades > 0) (winTrades.size.toDouble() / totalTrades) * 100.0 else 0.0
    val totalRealized = filteredTrades.sumOf { it.pnl }
    
    // Average RR ratio setup
    val avgRR = if (filteredTrades.isNotEmpty()) filteredTrades.map { it.riskRewardRatio }.average() else 1.5
    val safeRR = if (avgRR.isNaN()) 1.5 else avgRR

    // Optimal Strategy
    val strategyRankings = remember(filteredTrades) {
        filteredTrades.groupBy { it.strategy ?: "Manual Position" }
            .map { (strat, trades) ->
                val w = trades.filter { it.pnl > 0.0 }.size
                val rate = if (trades.isNotEmpty()) (w.toDouble() / trades.size) * 100.0 else 0.0
                val p = trades.sumOf { it.pnl }
                strat to Triple(trades.size, rate, p)
            }.sortedByDescending { it.second.third } // sort by profitable
    }
    val bestStrategyName = strategyRankings.firstOrNull()?.first ?: "N/A"
    val bestStrategyPnl = strategyRankings.firstOrNull()?.second?.third ?: 0.0

    // Optimal Coin
    val coinPerformance = remember(filteredTrades) {
        filteredTrades.groupBy { it.symbol ?: "UNKNOWN" }
            .map { (coin, trades) ->
                coin.uppercase() to trades.sumOf { it.pnl }
            }.sortedByDescending { it.second }
    }
    val bestCoinName = coinPerformance.firstOrNull()?.first ?: "N/A"
    val bestCoinPnl = coinPerformance.firstOrNull()?.second ?: 0.0

    // Drawdown Calculation
    val maxDrawdown = remember(filteredTrades) {
        var maxPeak = 0.0
        var runningEquity = 0.0
        var maxDD = 0.0
        filteredTrades.sortedBy { it.exitTimestamp ?: it.timestamp }.forEach { tr ->
            runningEquity += tr.pnl
            if (runningEquity > maxPeak) maxPeak = runningEquity
            val dd = maxPeak - runningEquity
            if (dd > maxDD) maxDD = dd
        }
        maxDD
    }

    // Direction performance
    val directionPnl = remember(filteredTrades) {
        val groups = filteredTrades.groupBy { it.signalType ?: "LONG" }
        val longP = groups["LONG"]?.sumOf { it.pnl } ?: 0.0
        val shortP = groups["SHORT"]?.sumOf { it.pnl } ?: 0.0
        longP to shortP
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("trade_analytics_console"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // --- Tab Header ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TRADE ANALYTICS & STRATEGY OPTIMIZATION",
                        fontFamily = FontFamily.Monospace,
                        color = CyberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Permanent cloud-aligned transactional intelligence database & adaptive strategy rankers.",
                        fontSize = 10.sp,
                        color = CyberTextDim
                    )
                }
            }
        }

        // --- Interactive Filters Drawer ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("MULTI-AXIS METRIC FILTERS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Filter row: Strategy
                    Text("STRATEGY FILTER", fontSize = 8.sp, color = CyberTextDim)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(listStrategies) { strat ->
                            val isSel = selectedStrategyFilter == strat
                            Button(
                                onClick = { selectedStrategyFilter = strat },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isSel) CyberAccentGreen else CyberSurface),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(strat, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) CyberDark else CyberTextWhite)
                            }
                        }
                    }
                    
                    // Filter row: Coin
                    Text("COIN / PAIR FILTER", fontSize = 8.sp, color = CyberTextDim)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(listCoins) { coin ->
                            val isSel = selectedCoinFilter == coin
                            Button(
                                onClick = { selectedCoinFilter = coin },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isSel) CyberAccentGreen else CyberSurface),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(coin, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) CyberDark else CyberTextWhite)
                            }
                        }
                    }

                    // Bottom filters grid
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Timeframe Column
                        Column(modifier = Modifier.weight(1f)) {
                            Text("TIMEFRAME", fontSize = 8.sp, color = CyberTextDim)
                            Spacer(modifier = Modifier.height(2.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(listTimeframes) { tf ->
                                    val isSel = selectedTimeframeFilter == tf
                                    Box(
                                        modifier = Modifier
                                            .background(if (isSel) CyberGold else CyberSurface, RoundedCornerShape(4.dp))
                                            .clickable { selectedTimeframeFilter = tf }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Text(tf, fontSize = 8.sp, color = if (isSel) CyberDark else CyberTextWhite, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        
                        // Exchange Column
                        Column(modifier = Modifier.weight(1f)) {
                            Text("EXCHANGE", fontSize = 8.sp, color = CyberTextDim)
                            Spacer(modifier = Modifier.height(2.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(listExchanges) { ex ->
                                    val isSel = selectedExchangeFilter == ex
                                    Box(
                                        modifier = Modifier
                                            .background(if (isSel) CyberGold else CyberSurface, RoundedCornerShape(4.dp))
                                            .clickable { selectedExchangeFilter = ex }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Text(ex.removePrefix("MEXC_"), fontSize = 8.sp, color = if (isSel) CyberDark else CyberTextWhite, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Direction filter row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("DIRECTION: ", fontSize = 8.sp, color = CyberTextDim)
                        listDirections.forEach { dir ->
                            val isSel = selectedDirectionFilter == dir
                            Box(
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .background(if (isSel) CyberAccentGreen else CyberSurface, RoundedCornerShape(4.dp))
                                    .clickable { selectedDirectionFilter = dir }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(dir, fontSize = 8.sp, color = if (isSel) CyberDark else CyberTextWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // --- Performance metric summary overview ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("FILTERED GENERAL PERFORMANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(12.dp)).padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CUMULATIVE METRIC P&L", fontSize = 8.sp, color = CyberTextDim)
                            Text("$${formatCurrency(totalRealized)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (totalRealized >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("WIN RATE", fontSize = 8.sp, color = CyberTextDim)
                            Text("${String.format(java.util.Locale.US, "%.1f", winRate)}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (winRate >= 50.0) CyberAccentGreen else CyberGold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            shareExcelFileToWhatsApp(context, filteredTrades)
                        },
                        modifier = Modifier.fillMaxWidth().testTag("whatsapp_excel_share_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberAccentGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "SHARE EXCEL AUDIT REPORT OVER WHATSAPP 📈",
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("TOTAL COUNTER", fontSize = 7.sp, color = CyberTextDim)
                                Text("$totalTrades Trades", fontSize = 11.sp, color = CyberTextWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Text("Win/Loss: ${winTrades.size}/${lossTrades.size}", fontSize = 8.sp, color = CyberTextDim)
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("MAX DD (DRAWDOWN)", fontSize = 7.sp, color = CyberTextDim)
                                Text("$${formatCurrency(maxDrawdown)}", fontSize = 11.sp, color = CyberAccentRed, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Text("Peak Retracement", fontSize = 8.sp, color = CyberTextDim)
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CyberDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("BEST SETUP RR", fontSize = 7.sp, color = CyberTextDim)
                                Text("${String.format(java.util.Locale.US, "%.2f", safeRR)}:1", fontSize = 11.sp, color = CyberAccentGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Text("Expected Ratio", fontSize = 8.sp, color = CyberTextDim)
                            }
                        }
                    }
                }
            }
        }

        // --- Reactive Equity Curve Line Graph ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CUMULATIVE PERFORMANCE EQUITY CURVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Text("Visual representation of capital appreciation over sequence index.", fontSize = 9.sp, color = CyberTextDim)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    CyberEquityCurveCanvas(filteredTrades)
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("◀ Sequence index progress (Earliest to Latest) ▶", fontSize = 8.sp, color = CyberTextDim, modifier = Modifier.align(Alignment.CenterHorizontally), fontFamily = FontFamily.Monospace)
                }
            }
        }

        // --- Deep analysis segment tables ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("STRATEGY PERFORMANCE RANKINGS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    if (strategyRankings.isEmpty()) {
                        Text("No strategy data recorded.", color = CyberTextDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            strategyRankings.forEachIndexed { i, (strat, stats) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(8.dp)).padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${i+1}. $strat", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberTextWhite)
                                        Text("Total: ${stats.first} | WinRate: ${String.format(java.util.Locale.US, "%.1f", stats.second)}%", fontSize = 9.sp, color = CyberTextDim)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("$${formatCurrency(stats.third)}", color = if (stats.third >= 0) CyberAccentGreen else CyberAccentRed, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // More insights panel
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("TACTICAL MATRIX INSIGHTS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MOST PROFITABLE COIN", fontSize = 8.sp, color = CyberTextDim)
                            Text(bestCoinName, fontSize = 12.sp, color = CyberTextWhite, fontWeight = FontWeight.Bold)
                            Text("PnL: +$${formatCurrency(bestCoinPnl)}", fontSize = 8.sp, color = CyberAccentGreen, fontFamily = FontFamily.Monospace)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("LONG VS SHORT PERFORMANCE", fontSize = 8.sp, color = CyberTextDim)
                            Text("Longs: $${formatCurrency(directionPnl.first)}", fontSize = 10.sp, color = if (directionPnl.first >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace)
                            Text("Shorts: $${formatCurrency(directionPnl.second)}", fontSize = 10.sp, color = if (directionPnl.second >= 0) CyberAccentGreen else CyberAccentRed, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // --- AI strategy crunch segment ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ai_insight_optimization_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("AI BRAIN ADVISORY CRUNCHER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                            Text("Let Gemini analyze local trade datasets to isolate edge and leverage suggestions.", fontSize = 9.sp, color = CyberTextDim)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    if (aiInsights.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberDark, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = aiInsights,
                                color = CyberTextWhite,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    
                    Button(
                        onClick = { viewModel.generateAiOptimizationInsights() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                        enabled = !isGeneratingAi,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isGeneratingAi) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CyberDark, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("COMPILING CORES...", color = CyberDark, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        } else {
                            Text("EXECUTE DEEP AI STRATEGY CRUNCH", color = CyberDark, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // --- DETAILED HISTORICAL TRANSACTION LOGS JOURNAL ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "⚡ DETAILED HISTORICAL TRANSACTION JOURNAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Real-time capture of every active/closed market entry parameter, technical indicator, and strategy rationale.",
                        fontSize = 9.sp,
                        color = CyberTextDim
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Filter bar: SEARCH INPUT
                    androidx.compose.material3.OutlinedTextField(
                        value = logSearchQuery,
                        onValueChange = { logSearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("log_search_text_input"),
                        placeholder = { Text("Search by symbol (e.g. BTC) or strategy...", color = CyberTextDim.copy(alpha = 0.5f), fontSize = 11.sp) },
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(color = CyberTextWhite, fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberGold,
                            unfocusedBorderColor = CyberSurface,
                            focusedContainerColor = CyberDark,
                            unfocusedContainerColor = CyberDark
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Filter row: STATUS SELECTOR (ALL, OPEN, CLOSED)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("STATUS:", fontSize = 8.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                        listOf("ALL", "OPEN", "CLOSED").forEach { status ->
                            val isSel = logStatusFilter == status
                            Box(
                                modifier = Modifier
                                    .background(if (isSel) CyberAccentGreen else CyberDark, RoundedCornerShape(4.dp))
                                    .border(1.dp, if (isSel) CyberAccentGreen else CyberSurface, RoundedCornerShape(4.dp))
                                    .clickable { logStatusFilter = status }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = status,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) CyberDark else CyberTextWhite,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        // Counter text
                        Text(
                            text = "${filteredLogs.size} MATCHES",
                            fontSize = 8.sp,
                            color = CyberGold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        if (filteredLogs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "⚡ NO LOGGED MATCHES DISCOVERED",
                            fontSize = 10.sp,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Render each log entry as an individual item with a composite unique key to prevent any duplicate key crashes
            itemsIndexed(filteredLogs, key = { index, tr -> "log_${tr.id}_$index" }) { index, tr ->
                var isExpanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .testTag("log_trade_card_${tr.id}"),
                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isExpanded) CyberGold.copy(alpha = 0.5f) else CyberSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Header row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val safeSignalType = tr.signalType ?: "LONG"
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (safeSignalType == "LONG") CyberAccentGreen.copy(alpha = 0.15f)
                                            else CyberAccentRed.copy(alpha = 0.15f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = safeSignalType,
                                        color = if (safeSignalType == "LONG") CyberAccentGreen else CyberAccentRed,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = (tr.symbol as? String ?: "UNKNOWN").uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextWhite,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            
                            // Status Badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        when (tr.status) {
                                            "OPEN" -> CyberGold.copy(alpha = 0.15f)
                                            "CLOSED_TP" -> CyberAccentGreen.copy(alpha = 0.15f)
                                            "CLOSED_SL" -> CyberAccentRed.copy(alpha = 0.15f)
                                            else -> Color(0xFF38BDF8).copy(alpha = 0.15f)
                                        },
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = tr.status,
                                    color = when (tr.status) {
                                        "OPEN" -> CyberGold
                                        "CLOSED_TP" -> CyberAccentGreen
                                        "CLOSED_SL" -> CyberAccentRed
                                        else -> Color(0xFF38BDF8)
                                    },
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        // Sub-info row: Strategy name and exchange
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = tr.strategy,
                                color = CyberTextDim,
                                fontSize = 10.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(0.7f)
                            )
                            Text(
                                text = tr.exchange,
                                color = CyberGold.copy(alpha = 0.7f),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(0.3f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        // Prices & PnL row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left side: Prices
                            Column {
                                Text("ENTRY / CURRENT", fontSize = 7.sp, color = CyberTextDim)
                                Text(
                                    text = "$${formatPrice(tr.entryPrice)} / $${formatPrice(tr.exitPrice ?: tr.currentPrice)}",
                                    fontSize = 10.sp,
                                    color = CyberTextWhite,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            // Right side: Realized/Unrealized PnL
                            Column(horizontalAlignment = Alignment.End) {
                                val pnlPct = if (tr.entryPrice > 0.0) {
                                    ((tr.exitPrice ?: tr.currentPrice) - tr.entryPrice) / tr.entryPrice * 100.0 * (if (tr.signalType == "LONG") 1.0 else -1.0)
                                } else 0.0
                                val cleanPnlPctVal = if (pnlPct.isNaN() || pnlPct.isInfinite()) 0.0 else pnlPct
                                val sign = if (tr.pnl > 0.0) "+" else ""
                                val signPct = if (cleanPnlPctVal >= 0.0) "+" else ""
                                
                                Text(
                                    text = if (tr.status == "OPEN") "UNREALIZED P&L" else "REALIZED P&L",
                                    fontSize = 7.sp,
                                    color = CyberTextDim
                                )
                                Text(
                                    text = "$sign$${formatCurrency(tr.pnl)} ($signPct${String.format(java.util.Locale.US, "%.2f", cleanPnlPctVal)}%)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (tr.pnl > 0.0) CyberAccentGreen else if (tr.pnl < 0.0) CyberAccentRed else CyberTextWhite,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        
                        // Expandable details block
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(10.dp))
                            androidx.compose.material3.Divider(color = CyberSurface, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // Grid of 4 parameter fields
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("📐 RISK-REWARD", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Text("1 : ${String.format(java.util.Locale.US, "%.1f", tr.riskRewardRatio)}", fontSize = 9.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("🔮 RSI Indicator", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Text("${String.format(java.util.Locale.US, "%.1f", tr.rsi)}", fontSize = 9.sp, color = CyberGold, fontFamily = FontFamily.Monospace)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("📊 VOLATILITY", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Text("${String.format(java.util.Locale.US, "%.2f", tr.volatility * 100.0)}%", fontSize = 9.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("⚡ ENTRY VOLUME", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Text("$${formatLargeNumber(tr.volume)}", fontSize = 9.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("📈 TREND TREND", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Text(tr.trend, fontSize = 9.sp, color = if (tr.trend == "UPTREND") CyberAccentGreen else if (tr.trend == "DOWNTREND") CyberAccentRed else CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("⏱️ TF / LEVERAGE", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    Text("${tr.timeframe} | ${tr.leverage}x", fontSize = 9.sp, color = CyberTextWhite, fontFamily = FontFamily.Monospace)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Stop Loss & Take Profit limits
                            Row(
                                modifier = Modifier.fillMaxWidth().background(CyberDark, RoundedCornerShape(6.dp)).padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("STOP LOSS LIMIT TRIGGER", fontSize = 7.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.stopLoss)}", fontSize = 9.sp, color = CyberAccentRed, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("TAKE PROFIT TARGET LIMIT", fontSize = 7.sp, color = CyberTextDim)
                                    Text("$${formatPrice(tr.takeProfit)}", fontSize = 9.sp, color = CyberAccentGreen, fontFamily = FontFamily.Monospace)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Why trade reason of execution
                            Text("📋 STRATEGIC JOURNAL RATIONALE NOTES", fontSize = 7.sp, color = CyberTextDim)
                            Text(
                                text = tr.whyTradeReason.ifBlank { "Executed automatically by the core strategy bot engine upon signal breakout match." },
                                fontSize = 9.sp,
                                color = CyberTextWhite,
                                lineHeight = 13.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // Dynamic timestamp info
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ENTRY: ${formatEpochToDate(tr.timestamp)}", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    if (tr.exitTimestamp != null) {
                                        Text("EXIT: ${formatEpochToDate(tr.exitTimestamp)}", fontSize = 7.sp, color = CyberTextDim, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                
                                // Direct WhatsApp button for specific trade
                                Button(
                                    onClick = {
                                        val singlePnlPct = if (tr.entryPrice > 0.0) {
                                            ((tr.exitPrice ?: tr.currentPrice) - tr.entryPrice) / tr.entryPrice * 100.0 * (if (tr.signalType == "LONG") 1.0 else -1.0)
                                        } else 0.0
                                        val statusEmoji = if ((tr.status ?: "OPEN") == "OPEN") "⏳" else if (tr.pnl > 0.0) "🟢" else "🔴"
                                        val singleReport = """
                                            📱 *SINGLE TRADE TELEMETRY REPORT* 📱
                                            $statusEmoji *[${tr.exchange ?: "UNKNOWN"}] ${(tr.symbol as? String ?: "UNKNOWN").uppercase()} (${tr.signalType ?: "LONG"})* - ${tr.status ?: "OPEN"}
                                            
                                            • Strategy: ${tr.strategy ?: "Manual Position"}
                                            • Entry Price: $$tr.entryPrice
                                            • Exit/Current Price: $${tr.exitPrice ?: tr.currentPrice}
                                            • Limits: SL: $tr.stopLoss | TP: $tr.takeProfit
                                            • P&L: $${tr.pnl} USDT (${String.format(java.util.Locale.US, "%+.2f", singlePnlPct)}%)
                                            • Leverage: ${tr.leverage}x | Size: $${tr.investedAmount}
                                            • RSI: ${String.format(java.util.Locale.US, "%.1f", tr.rsi)} | Volatility: ${String.format(java.util.Locale.US, "%.1f", tr.volatility * 100.0)}% | Trend: ${tr.trend ?: "NEUTRAL"}
                                            • Rationale: ${(tr.whyTradeReason ?: "").ifBlank { "Executed based on quantitative indicators." }}
                                            • Log Entry Time: ${formatEpochToDate(tr.timestamp)}
                                        """.trimIndent()
                                        shareToWhatsApp(context, singleReport)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp elegant green
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text("SHARE TRADE 💬", fontSize = 8.sp, color = Color.White, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Text(
                                text = "▶ Tap log message to expand strategic telemetry indicators & reasoning.",
                                fontSize = 7.sp,
                                color = CyberGold.copy(alpha = 0.5f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- Export reports tools card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DATA EXPORT ENGINE & SHARING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                    Text("Export trade historical logs to standardized CSV files or share deep audit reports.", fontSize = 9.sp, color = CyberTextDim)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (exportStatusText.isNotEmpty()) {
                        Text(exportStatusText, fontSize = 9.sp, color = CyberAccentGreen, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(bottom = 10.dp))
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                if (filteredTrades.isEmpty()) {
                                    exportStatusText = "❌ No trades to export."
                                    return@Button
                                }
                                try {
                                    val sb = java.lang.StringBuilder()
                                    sb.append("ID,Strategy,Coin,Direction,EntryPrice,ExitPrice,SL,TP,PnL,Ratio,Timeframe,Leverage,Exchange,Timestamp\n")
                                    filteredTrades.forEach { tr ->
                                        sb.append("${tr.id},\"${tr.strategy ?: "Manual Position"}\",${(tr.symbol as? String ?: "UNKNOWN").uppercase()},${tr.signalType ?: "LONG"},${tr.entryPrice},${tr.exitPrice ?: tr.currentPrice},${tr.stopLoss},${tr.takeProfit},${tr.pnl},${tr.riskRewardRatio},${tr.timeframe ?: "15m"},${tr.leverage},${tr.exchange ?: "UNKNOWN"},${tr.timestamp}\n")
                                    }
                                    
                                    val file = java.io.File(context.getExternalFilesDir(null), "TradeHistoryAnalyticsReport.csv")
                                    file.writeText(sb.toString())
                                    exportStatusText = "🟢 Exported spreadsheet to: ${file.absolutePath} successfully!"
                                } catch (e: Exception) {
                                    exportStatusText = "❌ Export failed: ${e.message}"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("EXPORT CSV (EXCEL)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberAccentGreen, fontFamily = FontFamily.Monospace)
                        }
                        
                        Button(
                            onClick = {
                                if (filteredTrades.isEmpty()) {
                                    exportStatusText = "❌ No trades to copy."
                                    return@Button
                                }
                                val sb = java.lang.StringBuilder()
                                sb.append("=== TRADING BOT METRICS REPORT ===\n")
                                filteredTrades.forEach { tr ->
                                    val statusSym = if (tr.pnl > 0.0) "🟢" else "🔴"
                                    sb.append("$statusSym [${tr.exchange ?: "UNKNOWN"}] ${(tr.symbol as? String ?: "UNKNOWN").uppercase()} (${tr.signalType ?: "LONG"}) | EP: ${tr.entryPrice} SL: ${tr.stopLoss} TP: ${tr.takeProfit} | Gain/Loss: \$${String.format(java.util.Locale.US, "%.2f", tr.pnl)} | Strategy: ${tr.strategy ?: "Manual Position"}\n")
                                }
                                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(sb.toString()))
                                exportStatusText = "🟢 Successfully copied telemetry data report to clipboard!"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("COPY DATA REPORT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberGold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    // Direct WhatsApp full report sharing button
                    Button(
                        onClick = {
                            if (filteredTrades.isEmpty()) {
                                exportStatusText = "❌ No trades to share."
                                return@Button
                            }
                            shareExcelFileToWhatsApp(context, filteredTrades)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp Green
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Text("💬 SHARE EXCEL REPORT TO WHATSAPP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@Composable
fun CyberEquityCurveCanvas(trades: List<PaperTrade>) {
    val curveData = remember(trades) {
        var accumulated = 0.0
        val rawDataPoints = mutableListOf<Float>()
        rawDataPoints.add(0f)
        trades.sortedBy { it.exitTimestamp ?: it.timestamp }.forEach { tr ->
            val safePnL = if (tr.pnl.isNaN() || tr.pnl.isInfinite()) 0.0 else tr.pnl
            accumulated += safePnL
            rawDataPoints.add(accumulated.toFloat())
        }
        
        val rawFiltered = rawDataPoints.filter { !it.isNaN() && !it.isInfinite() }
        
        // Downsample to a maximum of 50 points to prevent layout/drawing performance drops and system process terminations
        val dataPoints = if (rawFiltered.size > 50) {
            val step = rawFiltered.size.toDouble() / 50.0
            List(50) { i ->
                rawFiltered[(i * step).toInt().coerceIn(0, rawFiltered.size - 1)]
            }
        } else {
            rawFiltered
        }
        
        val maxP = dataPoints.maxOrNull()?.coerceAtLeast(10f) ?: 10f
        val minP = dataPoints.minOrNull()?.coerceAtMost(-10f) ?: -10f
        val range = (maxP - minP).coerceAtLeast(1f)
        
        Triple(dataPoints, range, Pair(minP, maxP))
    }

    val dataPoints = curveData.first
    val range = curveData.second
    val minP = curveData.third.first

    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(160.dp).background(CyberDark, RoundedCornerShape(12.dp)).padding(8.dp)) {
        val width = size.width
        val height = size.height
        
        // Draw Grid Lines
        val gridLines = 4
        for (i in 1..gridLines) {
            val y = (height / (gridLines + 1)) * i
            drawLine(
                color = Color(0xFF1E293B),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }
        
        // Plot values
        if (dataPoints.size > 1) {
            val stepX = width / (dataPoints.size - 1)
            val path = androidx.compose.ui.graphics.Path()
            val fillPath = androidx.compose.ui.graphics.Path()
            
            val rawFirstY = height - ((dataPoints[0] - minP) / range * height)
            val firstY = if (rawFirstY.isNaN() || rawFirstY.isInfinite()) height / 2f else rawFirstY.coerceIn(0f, height)
            
            path.moveTo(0f, firstY)
            fillPath.moveTo(0f, height)
            fillPath.lineTo(0f, firstY)
            
            for (idx in 1 until dataPoints.size) {
                val valX = idx * stepX
                val rawY = height - ((dataPoints[idx] - minP) / range * height)
                val valY = if (rawY.isNaN() || rawY.isInfinite()) height / 2f else rawY.coerceIn(0f, height)
                path.lineTo(valX, valY)
                fillPath.lineTo(valX, valY)
            }
            
            fillPath.lineTo(width, height)
            fillPath.close()
            
            // Draw Gradient fill
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(CyberAccentGreen.copy(alpha = 0.25f), Color.Transparent)
                )
            )
            
            // Draw stroke line
            drawPath(
                path = path,
                color = CyberAccentGreen,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
            
            // Draw dots at peak points
            for (idx in dataPoints.indices) {
                val valX = idx * stepX
                val rawY = height - ((dataPoints[idx] - minP) / range * height)
                val valY = if (rawY.isNaN() || rawY.isInfinite()) height / 2f else rawY.coerceIn(0f, height)
                drawCircle(
                    color = if (dataPoints[idx] >= 0) CyberAccentGreen else CyberAccentRed,
                    radius = 5f,
                    center = Offset(valX, valY)
                )
            }
        } else {
            // Draw placeholder line
            drawLine(
                color = CyberAccentGreen.copy(alpha = 0.5f),
                start = Offset(0f, height/2),
                end = Offset(width, height/2),
                strokeWidth = 2f
            )
        }
    }
}
