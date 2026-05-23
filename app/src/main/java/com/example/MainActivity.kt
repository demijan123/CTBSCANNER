package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val repository = CryptoRepository(db.coinDao(), db.paperTradeDao())
        val factory = CryptoViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[CryptoViewModel::class.java]

        setContent {
            MyApplicationTheme {
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
    val tabTitles = listOf("MARKET SCANNER", "CONFIRMED SIGNALS", "WATCHLIST", "BLUEPRINTS", "PAPER TRADING", "AUTO BOT")

    val scannedCoins by viewModel.scannedCoins.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val scanLogs by viewModel.scanLogs.collectAsState()
    val error by viewModel.error.collectAsState()

    val confirmedSignals by viewModel.activeConfirmedSignals.collectAsState()
    val bookmarkedSignals by viewModel.bookmarkedSignals.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDark)
    ) {
        // --- Header Status Bar ---
        HeaderStatusBar(
            isScanning = isScanning,
            isKeyConfigured = viewModel.isModelKeyConfigured,
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
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = CyberAccentGreen
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("nav_tabs")
        ) {
            tabTitles.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                val icon = when (index) {
                    0 -> Icons.Default.Search
                    1 -> Icons.Default.Check
                    2 -> Icons.Default.Favorite
                    3 -> Icons.Default.Info
                    4 -> Icons.Default.PlayArrow
                    5 -> Icons.Default.Refresh
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
                        // Analyze specific item instantly
                        viewModel.addLog("Triggering instant manual AI analysis on ${coin.symbol.uppercase()}...")
                        viewModel.startFullMarketScan()
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
                4 -> PaperTradingPortfolioTab(viewModel = viewModel)
                5 -> AutoBotTradingConsoleTab(viewModel = viewModel)
            }
        }

        // --- Tiny Footer ---
        ThinFooterNotice()
    }
}

@Composable
fun HeaderStatusBar(
    isScanning: Boolean,
    isKeyConfigured: Boolean,
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
                text = "MARKET SCANNER",
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
                progress = progress,
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
                items(filteredCoins, key = { it.id }) { coin ->
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
            items(signals, key = { it.id }) { signal ->
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
            val pointsCount = 10

            // Plot a realistic volatile curve
            val points = ArrayList<Offset>()
            // Generate deterministic curves using the coin's DB metrics as keys
            val seedString = signal.id
            val charSum = seedString.sumOf { it.code }
            val generator = java.util.Random(charSum.toLong())

            val baselineOffset = if (isBuy) 0.6f else 0.4f

            for (i in 0 until pointsCount) {
                val x = (width / (pointsCount - 1)) * i
                val floatVolatility = generator.nextFloat() * 0.35f
                val sineMod = Math.sin(i.toDouble() * 1.2).toFloat() * 0.15f
                val deltaTrend = if (isBuy) (i.toFloat() / pointsCount.toFloat()) * 0.3f else -(i.toFloat() / pointsCount.toFloat()) * 0.3f
                
                val yFraction = baselineOffset - deltaTrend + floatVolatility + sineMod
                val y = height * yFraction.coerceIn(0.1f, 0.9f)
                points.add(Offset(x, y))
            }

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
        val blueprints = listOf(
            StrategyBlueprint(
                title = "EMA Continuation Cross (V3)",
                trend = "Bullish Outperformance",
                metrics = "50 EMA / 200 EMA breakout threshold",
                description = "Analyzes high-volume support consolidating slightly above the 50 Exponential Moving Average. On lower cap parameters, large liquidity pools accumulate positions, predicting immediate swing continuation."
            ),
            StrategyBlueprint(
                title = "Volumetric Liquidity Sweep",
                trend = "Bearish Exhaustion Rallies",
                metrics = "Relative volume (RV) < 0.6 + Price spike > 8%",
                description = "This strategy highlights rallies in thin-orderbook setups. Lower liquidity easily causes sharp, unsustainable upward spikes on extremely low volume, yielding high-probability, short-term reversion sweeps."
            ),
            StrategyBlueprint(
                title = "Mean Reversion & Oversold Bounce",
                trend = "Oversold Rebound Pivot",
                metrics = "RSI-14 values < 25 + Daily retracement > -10%",
                description = "Focuses on premium, high-utility projects suffering cascading liquidations. When capitalization areas hit support bottoms on exhausting volume, it triggers low-risk, high-velocity reverse-bounce plays."
            ),
            StrategyBlueprint(
                title = "Wyckoff Spring & Phase C Accumulation",
                trend = "Bullish Phase C Markup",
                metrics = "Consolidation sweep + Volume > 1.8x average",
                description = "Models structural market transitions by identifying early markup. Detects a quick downward flush (the 'Spring') that sweeps low liquidity stops, immediately followed by strong buying volume that reclaims the trading range, initiating a high-accuracy upward advance."
            ),
            StrategyBlueprint(
                title = "High-Volume Momentum Breakout",
                trend = "Bullish Momentum Continuation",
                metrics = "High 24h gain + Relative volume > 1.5",
                description = "Capitalizes on momentum expansion above critical resistance envelopes. Sustained volume spikes signal high-density institutional flow, driving a strong momentum surge toward overhead liquidity levels. Highly effective for rapid trend riders."
            ),
            StrategyBlueprint(
                title = "Institutional Order Block Grab",
                trend = "Bullish Order Reconstruction",
                metrics = "Historical demand re-test + Volumetric support",
                description = "Tracks historical institutional demand zones on 4-hour charts. When price touches a major discount order block, limit order clusters of massive size are triggered. This shields the trader with an exceptionally tight, low-risk stop-loss and premium entry precision."
            ),
            StrategyBlueprint(
                title = "MACD Divergence & Momentum Exhaustion",
                trend = "Bearish Multi-drive Divergence",
                metrics = "Lower MACD highs + Higher Price highs",
                description = "An early-warning indicator for structural trend reversals. Recognizes instances where the price presses new intraday highs but the MACD momentum histogram exhibits consecutive lower peaks, warning of severe upward exhaustion and highly profitable short pivots."
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("blueprints_list"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(blueprints) { setup ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = setup.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite,
                                letterSpacing = (-0.3).sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberSlate)
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(text = setup.trend, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberPercentColor(setup.trend))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
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
                                    runBacktestCalculation(strategy.title, initialCapital, leverage, timeframeDays, selectedAsset)
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
                            progress = { simulationProgress },
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
        points.filter { !it.isNaN() && !it.isInfinite() }
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
                    endY = height
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
    selectedAsset: String = "ALL"
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
    
    val assets = listOf("POPCAT", "WIF", "BRETT", "MEW", "BOME", "MYRO", "TOSHI", "COQ", "DEGEN", "SILLY")
    
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
        
        val isWin = random.nextDouble() < baseWinRate
        
        val changePct = if (isWin) {
            avgWinPct + (random.nextDouble() * 0.05)
        } else {
            avgLossPct - (random.nextDouble() * 0.02)
        }
        
        var leveragedChange = changePct * leverage
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
        
        val entryPrice = 0.10 + random.nextDouble() * 4.5
        val exitPrice = entryPrice * (1.0 + changePct)
        
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
    val description: String
)

@Composable
fun CyberPercentColor(trend: String): Color {
    return if (trend.contains("Bullish") || trend.contains("Bounce")) CyberAccentGreen else CyberAccentRed
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

@Composable
fun PaperTradingPortfolioTab(viewModel: CryptoViewModel) {
    val cashBalance by viewModel.cashBalance.collectAsState()
    val openTrades by viewModel.openTrades.collectAsState()
    val closedTrades by viewModel.closedTrades.collectAsState()

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
                items(openTrades, key = { it.id }) { trade ->
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
                items(closedTrades, key = { it.id }) { trade ->
                    ClosedPositionCard(trade = trade)
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
                                text = "$${trade.symbol.uppercase()}",
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
                                    text = trade.strategy.uppercase(),
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )
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
                                text = "$${trade.symbol.uppercase()}",
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
                                    text = trade.strategy.uppercase(),
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    fontFamily = FontFamily.Monospace
                                )
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

    val blueprints = listOf(
        "EMA Continuation Cross (V3)",
        "Volumetric Liquidity Sweep",
        "Mean Reversion & Oversold Bounce",
        "Wyckoff Spring & Phase C Accumulation",
        "High-Volume Momentum Breakout",
        "Institutional Order Block Grab",
        "MACD Divergence & Momentum Exhaustion"
    )

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
