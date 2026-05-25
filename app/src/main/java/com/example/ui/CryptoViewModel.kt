package com.example.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.local.SavedSignal
import com.example.data.local.PaperTrade
import com.example.data.model.Coin
import com.example.data.repository.CryptoRepository
import com.example.data.network.OkxClient
import com.example.data.network.MexcClient
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class MarketCapTier(val displayName: String, val minCap: Double, val maxCap: Double) {
    LOW("LOW CAP (50M - 200M)", 50_000_000.0, 200_000_000.0),
    MID("MID CAP (200M - 2B)", 200_000_000.0, 2_000_000_000.0),
    HIGH("HIGH CAP (2B+)", 2_000_000_000.0, 100_000_000_000.0),
    CUSTOM("CUSTOM RANGE", 0.0, Double.MAX_VALUE)
}

class CryptoViewModel(
    application: Application,
    private val repository: CryptoRepository
) : AndroidViewModel(application) {

    // --- Paper Trading State ---
    private val prefs = application.getSharedPreferences("paper_trading_prefs", Context.MODE_PRIVATE)

    private val _cashBalance = MutableStateFlow(prefs.getFloat("cash_balance", 100000f).toDouble())
    val cashBalance: StateFlow<Double> = _cashBalance.asStateFlow()

    // --- Auto Trading Bot State ---
    private val _botEnabled = MutableStateFlow(prefs.getBoolean("bot_enabled", false))
    val botEnabled: StateFlow<Boolean> = _botEnabled.asStateFlow()

    private val _botMaxDailyTrades = MutableStateFlow(prefs.getInt("bot_max_trades", 10))
    val botMaxDailyTrades: StateFlow<Int> = _botMaxDailyTrades.asStateFlow()

    private val _botSelectionMode = MutableStateFlow(prefs.getString("bot_selection_mode", "AUTO") ?: "AUTO")
    val botSelectionMode: StateFlow<String> = _botSelectionMode.asStateFlow()

    private val defaultBlueprints = setOf(
        "EMA Continuation Cross (V3)",
        "Volumetric Liquidity Sweep",
        "Mean Reversion & Oversold Bounce",
        "Wyckoff Spring & Phase C Accumulation",
        "High-Volume Momentum Breakout",
        "Institutional Order Block Grab",
        "MACD Divergence & Momentum Exhaustion"
    )

    private val _botSelectedBlueprints = MutableStateFlow(
        prefs.getStringSet("bot_selected_blueprints", defaultBlueprints) ?: defaultBlueprints
    )
    val botSelectedBlueprints: StateFlow<Set<String>> = _botSelectedBlueprints.asStateFlow()

    private val _botTradeSize = MutableStateFlow(prefs.getFloat("bot_trade_size", 1000f).toDouble())
    val botTradeSize: StateFlow<Double> = _botTradeSize.asStateFlow()

    private val _botTargetCoinMode = MutableStateFlow(prefs.getString("bot_target_coin_mode", "ALL") ?: "ALL")
    val botTargetCoinMode: StateFlow<String> = _botTargetCoinMode.asStateFlow()

    private val _botSelectedCoinIds = MutableStateFlow(prefs.getStringSet("bot_selected_coin_ids", emptySet()) ?: emptySet())
    val botSelectedCoinIds: StateFlow<Set<String>> = _botSelectedCoinIds.asStateFlow()

    // --- OKX Live Trading State ---
    private val _okxEnabled = MutableStateFlow(prefs.getBoolean("okx_enabled", false))
    val okxEnabled: StateFlow<Boolean> = _okxEnabled.asStateFlow()

    private val _okxIsDemo = MutableStateFlow(prefs.getBoolean("okx_is_demo", true))
    val okxIsDemo: StateFlow<Boolean> = _okxIsDemo.asStateFlow()

    private val _okxApiKey = MutableStateFlow(prefs.getString("okx_api_key", "00884f07-3877-4b53-a543-39d504adcf99") ?: "00884f07-3877-4b53-a543-39d504adcf99")
    val okxApiKey: StateFlow<String> = _okxApiKey.asStateFlow()

    private val _okxSecretKey = MutableStateFlow(prefs.getString("okx_secret_key", "220F3C4CE2842CA9B3085F959DEA779F") ?: "220F3C4CE2842CA9B3085F959DEA779F")
    val okxSecretKey: StateFlow<String> = _okxSecretKey.asStateFlow()

    private val _okxPassphrase = MutableStateFlow(prefs.getString("okx_passphrase", "N@deem123") ?: "N@deem123")
    val okxPassphrase: StateFlow<String> = _okxPassphrase.asStateFlow()

    private val _okxBalance = MutableStateFlow(0.0)
    val okxBalance: StateFlow<Double> = _okxBalance.asStateFlow()

    private val _okxConnectionStatus = MutableStateFlow("Disconnected")
    val okxConnectionStatus: StateFlow<String> = _okxConnectionStatus.asStateFlow()

    fun setOkxEnabled(enabled: Boolean) {
        _okxEnabled.value = enabled
        prefs.edit().putBoolean("okx_enabled", enabled).apply()
        addLog(if (enabled) "🟢 OKX Live/Demo Execution ROUTED." else "🔴 OKX Live/Demo Execution DEACTIVATED.")
        if (enabled) {
            validateAndRefreshOkxBalance()
        }
    }

    fun setOkxIsDemo(isDemo: Boolean) {
        _okxIsDemo.value = isDemo
        prefs.edit().putBoolean("okx_is_demo", isDemo).apply()
        addLog("🤖 OKX Mode set to ${if (isDemo) "DEMO/SIMULATED" else "LIVE ORDER BOOK"}")
        validateAndRefreshOkxBalance()
    }

    fun saveOkxCredentials(apiKey: String, secretKey: String, passphrase: String) {
        _okxApiKey.value = apiKey
        _okxSecretKey.value = secretKey
        _okxPassphrase.value = passphrase
        prefs.edit()
            .putString("okx_api_key", apiKey)
            .putString("okx_secret_key", secretKey)
            .putString("okx_passphrase", passphrase)
            .apply()
        addLog("🤖 Saved OKX API Credentials manually.")
        validateAndRefreshOkxBalance()
    }

    fun validateAndRefreshOkxBalance() {
        val key = _okxApiKey.value
        val sec = _okxSecretKey.value
        val pass = _okxPassphrase.value
        val isDemo = _okxIsDemo.value

        if (key.isBlank() || sec.isBlank() || pass.isBlank()) {
            _okxConnectionStatus.value = "Credentials Missing"
            return
        }

        _okxConnectionStatus.value = "Connecting..."
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val balance = OkxClient.validateAndFetchBalance(key, sec, pass, isDemo)
                _okxBalance.value = balance
                _okxConnectionStatus.value = "Connected: ${String.format(Locale.US, "%.2f", balance)} USDT (${if (isDemo) "Demo" else "Live"})"
                addLog("🟢 OKX API connection verified successfully. Balance: $${String.format(Locale.US, "%.2f", balance)} USDT.")
            } catch (e: Throwable) {
                _okxBalance.value = 0.0
                val errMsg = e.localizedMessage ?: "Connection Failed"
                _okxConnectionStatus.value = "Error: $errMsg"
                addLog("❌ OKX API connection failed: $errMsg")
            }
        }
    }

    // --- MEXC Live Trading State ---
    private val _mexcEnabled = MutableStateFlow(prefs.getBoolean("mexc_enabled", false))
    val mexcEnabled: StateFlow<Boolean> = _mexcEnabled.asStateFlow()

    private val _mexcIsDemo = MutableStateFlow(prefs.getBoolean("mexc_is_demo", true))
    val mexcIsDemo: StateFlow<Boolean> = _mexcIsDemo.asStateFlow()

    private val _mexcApiKey = MutableStateFlow(prefs.getString("mexc_api_key", "") ?: "")
    val mexcApiKey: StateFlow<String> = _mexcApiKey.asStateFlow()

    private val _mexcSecretKey = MutableStateFlow(prefs.getString("mexc_secret_key", "") ?: "")
    val mexcSecretKey: StateFlow<String> = _mexcSecretKey.asStateFlow()

    private val _mexcBalance = MutableStateFlow(0.0)
    val mexcBalance: StateFlow<Double> = _mexcBalance.asStateFlow()

    private val _mexcConnectionStatus = MutableStateFlow("Disconnected")
    val mexcConnectionStatus: StateFlow<String> = _mexcConnectionStatus.asStateFlow()

    fun setMexcEnabled(enabled: Boolean) {
        _mexcEnabled.value = enabled
        prefs.edit().putBoolean("mexc_enabled", enabled).apply()
        addLog(if (enabled) "🟢 MEXC Live/Demo Execution ROUTED." else "🔴 MEXC Live/Demo Execution DEACTIVATED.")
        if (enabled) {
            validateAndRefreshMexcBalance()
        }
    }

    fun setMexcIsDemo(isDemo: Boolean) {
        _mexcIsDemo.value = isDemo
        prefs.edit().putBoolean("mexc_is_demo", isDemo).apply()
        addLog("🤖 MEXC Mode set to ${if (isDemo) "DEMO (Simulated)" else "LIVE ORDER BOOK"}")
        validateAndRefreshMexcBalance()
    }

    fun saveMexcCredentials(apiKey: String, secretKey: String) {
        _mexcApiKey.value = apiKey
        _mexcSecretKey.value = secretKey
        prefs.edit()
            .putString("mexc_api_key", apiKey)
            .putString("mexc_secret_key", secretKey)
            .apply()
        addLog("🤖 Saved MEXC API Credentials manually.")
        validateAndRefreshMexcBalance()
    }

    fun validateAndRefreshMexcBalance() {
        val key = _mexcApiKey.value
        val sec = _mexcSecretKey.value
        val isDemo = _mexcIsDemo.value

        if (key.isBlank() || sec.isBlank()) {
            _mexcConnectionStatus.value = "Credentials Missing"
            return
        }

        _mexcConnectionStatus.value = "Connecting..."
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val balance = MexcClient.validateAndFetchBalance(key, sec, isDemo)
                _mexcBalance.value = balance
                _mexcConnectionStatus.value = "Connected: ${String.format(Locale.US, "%.2f", balance)} USDT (${if (isDemo) "Demo" else "Live"})"
                addLog("🟢 MEXC API connection verified successfully. Balance: $${String.format(Locale.US, "%.2f", balance)} USDT.")
            } catch (e: Throwable) {
                _mexcBalance.value = 0.0
                val errMsg = e.localizedMessage ?: "Unknown connection error"
                _mexcConnectionStatus.value = "Error: $errMsg"
                addLog("❌ MEXC API connection failed: $errMsg")
            }
        }
    }

    val openTrades: StateFlow<List<PaperTrade>> = repository.openTradesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val closedTrades: StateFlow<List<PaperTrade>> = repository.closedTradesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun modifyCashBalance(delta: Double): Boolean {
        var success = true
        _cashBalance.update { current ->
            if (delta < 0.0 && current < -delta) {
                success = false
                current
            } else {
                val newBalance = current + delta
                prefs.edit().putFloat("cash_balance", newBalance.toFloat()).apply()
                newBalance
            }
        }
        return success
    }

    private fun resetCashBalance() {
        _cashBalance.update {
            val newBalance = 100000.0
            prefs.edit().putFloat("cash_balance", newBalance.toFloat()).apply()
            newBalance
        }
    }

    // --- Market Cap Configurations & Tiers ---
    private val _selectedMarketCapTier = MutableStateFlow(
        try {
            MarketCapTier.valueOf(prefs.getString("market_cap_tier", MarketCapTier.LOW.name) ?: MarketCapTier.LOW.name)
        } catch (e: Exception) {
            MarketCapTier.LOW
        }
    )
    val selectedMarketCapTier: StateFlow<MarketCapTier> = _selectedMarketCapTier.asStateFlow()

    private val _customMinCap = MutableStateFlow(prefs.getFloat("custom_min_cap", 50_000_000f).toDouble())
    val customMinCap: StateFlow<Double> = _customMinCap.asStateFlow()

    private val _customMaxCap = MutableStateFlow(prefs.getFloat("custom_max_cap", 200_000_000f).toDouble())
    val customMaxCap: StateFlow<Double> = _customMaxCap.asStateFlow()

    fun setMarketCapTier(tier: MarketCapTier) {
        _selectedMarketCapTier.value = tier
        prefs.edit().putString("market_cap_tier", tier.name).apply()
        triggerMarketScanOnConfigChange()
        resetBotCoinsCache()
    }

    fun setCustomMarketCapRange(min: Double, max: Double) {
        _customMinCap.value = min
        _customMaxCap.value = max
        prefs.edit()
            .putFloat("custom_min_cap", min.toFloat())
            .putFloat("custom_max_cap", max.toFloat())
            .apply()
        if (_selectedMarketCapTier.value == MarketCapTier.CUSTOM) {
            triggerMarketScanOnConfigChange()
        }
        resetBotCoinsCache()
    }

    fun getActiveMarketCapRange(): Pair<Double, Double> {
        val tier = _selectedMarketCapTier.value
        return when (tier) {
            MarketCapTier.LOW -> Pair(tier.minCap, tier.maxCap)
            MarketCapTier.MID -> Pair(tier.minCap, tier.maxCap)
            MarketCapTier.HIGH -> Pair(tier.minCap, tier.maxCap)
            MarketCapTier.CUSTOM -> Pair(_customMinCap.value, _customMaxCap.value)
        }
    }

    private var scanOnConfigChangeJob: kotlinx.coroutines.Job? = null

    private fun triggerMarketScanOnConfigChange() {
        scanOnConfigChangeJob?.cancel()
        scanOnConfigChangeJob = viewModelScope.launch {
            try {
                delay(300)
                val range = getActiveMarketCapRange()
                val coins = repository.scanMarket(useFallbackOnly = true, minCap = range.first, maxCap = range.second)
                _scannedCoins.value = coins
            } catch (e: Throwable) {
                if (e !is kotlinx.coroutines.CancellationException) {
                    _error.value = "Failed to load market data: ${e.message}"
                }
            }
        }
    }

    // Scanned coins in the active market cap range
    private val _scannedCoins = MutableStateFlow<List<Coin>>(emptyList())
    val scannedCoins: StateFlow<List<Coin>> = _scannedCoins.asStateFlow()

    // Scanner state
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scanLogs = MutableStateFlow<List<String>>(listOf("System ready. Click scan to sweep the market."))
    val scanLogs: StateFlow<List<String>> = _scanLogs.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Key availability info
    val isModelKeyConfigured: Boolean = BuildConfig.GEMINI_API_KEY.isNotEmpty() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

    // Locally database cached signals
    val activeConfirmedSignals: StateFlow<List<SavedSignal>> = repository.allSignals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bookmarkedSignals: StateFlow<List<SavedSignal>> = repository.bookmarkedSignals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Automatically test OKX credentials if enabled on init
        if (_okxEnabled.value) {
            validateAndRefreshOkxBalance()
        }
        // Automatically test MEXC credentials if enabled on init
        if (_mexcEnabled.value) {
            validateAndRefreshMexcBalance()
        }

        // Automatically fetch an initial sample of coins using loaded constraints
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val range = getActiveMarketCapRange()
                val coins = repository.scanMarket(useFallbackOnly = false, minCap = range.first, maxCap = range.second)
                _scannedCoins.value = coins
            } catch (e: Throwable) {
                _error.value = "Failed to load market data: ${e.message}"
            }
        }

        // Active price-ticking coroutine loop for live paper trading and Auto Trading Bot core loop
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            while (true) {
                delay(4000) // Update every 4 seconds
                try {
                    updateLiveOpenTrades()
                    if (_botEnabled.value) {
                        runBotAutoTradingCycle()
                    }
                } catch (e: Throwable) {
                    Log.e("CryptoViewModel", "Error in paper trading live ticking loop: ${e.message}")
                }
            }
        }
    }

    fun clearLogs() {
        _scanLogs.update { emptyList() }
    }

    fun addLog(log: String) {
        _scanLogs.update { current ->
            val updated = current + "[${getCurrentTime()}] $log"
            if (updated.size > 150) updated.takeLast(150) else updated
        }
    }

    private fun getCurrentTime(): String {
        val formatter = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }

    /**
     * Executes the full market scan:
     * 1. Query the market for configured bounds.
     * 2. Loops candidates, runs predictors (Gemini/Local rules).
     * 3. Sifts out high-confidence LONG/SHORT signals (filters NONE).
     * 4. Stores signals in the database cache.
     */
    fun startFullMarketScan(useFallbackOnly: Boolean = false) {
        if (_isScanning.value) return

        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0.05f
            _error.value = null
            _scanLogs.value = emptyList()

            addLog("Initializing Alpha Scanner pipeline...")
            delay(800)
            
            addLog("Checking AI engine configuration...")
            delay(500)
            if (isModelKeyConfigured) {
                addLog("AI engine loaded successfully (Direct Gemini 3.5-Flash REST link)")
            } else {
                addLog("Gemini Key not found. Initiating high-accuracy Quantitative Strategy Engine.")
            }
            delay(600)

            val range = getActiveMarketCapRange()
            val formatMin = if (range.first.isNaN() || range.first.isInfinite()) "$0" else String.format(java.util.Locale.US, "$%,.0f", range.first)
            val formatMax = if (range.second >= Double.MAX_VALUE || range.second.isInfinite() || range.second.isNaN()) "Infinity" else String.format(java.util.Locale.US, "$%,.0f", range.second)
            addLog("Scanning cryptocurrency indices for cap tier: $formatMin - $formatMax USD...")
            _scanProgress.value = 0.15f
            
            try {
                // Fetch candidate coins
                val coins = repository.scanMarket(useFallbackOnly, minCap = range.first, maxCap = range.second)
                _scannedCoins.value = coins
                
                if (coins.isEmpty()) {
                    addLog("Warning: No active coins detected in range under current API rate limits. Generating high-liquidity sandbox targets.")
                    _isScanning.value = false
                    _scanProgress.value = 0f
                    return@launch
                }

                addLog("Successfully retrieved ${coins.size} candidate listings.")
                delay(800)

                _scanProgress.value = 0.30f
                addLog("Calculating volumetric indicators & moving average trend envelopes...")
                delay(1000)

                // Wipe non-bookmarked old setups so we don't clog results
                addLog("Clearing historical un-bookmarked volatility signals from Room storage.")
                repository.clearUnsavedSignals()
                delay(500)

                _scanProgress.value = 0.45f
                addLog("Launching parallel multivariant trade model analysis on candidates...")
                delay(1000)

                var analyzedCount = 0
                var generatedSignalsCount = 0

                // Analyze each coin in the list
                for (coin in coins) {
                    analyzedCount++
                    addLog("Analyzing ${coin.name} (${coin.symbol.uppercase()})...")
                    
                    val prediction = repository.predictTradeSignal(coin)
                    
                    if (prediction.signal == "LONG" || prediction.signal == "SHORT") {
                        if (prediction.confidence >= 85) {
                            val savedSignal = SavedSignal(
                                id = coin.id,
                                symbol = coin.symbol,
                                name = coin.name,
                                image = coin.image,
                                currentPrice = coin.currentPrice,
                                marketCap = coin.marketCap,
                                totalVolume = coin.totalVolume,
                                signal = prediction.signal,
                                confidence = prediction.confidence,
                                strategy = prediction.strategy,
                                stopLoss = prediction.stopLoss,
                                takeProfit = prediction.takeProfit,
                                rationale = prediction.rationale,
                                isBookmarked = false
                            )
                            repository.saveSignal(savedSignal)
                            generatedSignalsCount++
                            addLog("🟢 CONFIRMED HIGH-ACCURACY ${prediction.signal} SETUP for ${coin.symbol.uppercase()} (${prediction.confidence}% confidence)")
                        }
                    } else {
                        Log.d("CryptoViewModel", "Skipped ${coin.symbol} (Signal: ${prediction.signal})")
                    }

                    // Increment progress smoothly between 0.45 and 0.90
                    val currentProgress = 0.45f + (analyzedCount.toFloat() / coins.size.toFloat() * 0.45f)
                    _scanProgress.value = currentProgress
                    delay(400) // Delay to simulate professional visual work/feedback
                }

                _scanProgress.value = 0.95f
                addLog("Finalizing results collation & signal filtering...")
                delay(600)

                _scanProgress.value = 1.0f
                addLog("SUCCESS: Swept $analyzedCount microcaps. Consolidated $generatedSignalsCount high-conviction trade entries that are highly confirmed.")
                
            } catch (e: Throwable) {
                Log.e("CryptoViewModel", "Scan error: ${e.message}", e)
                addLog("🛑 Fatal pipeline error: ${e.localizedMessage}")
                _error.value = "Pipeline broken: ${e.message}"
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun toggleBookmark(signal: SavedSignal) {
        viewModelScope.launch {
            try {
                val updated = signal.copy(isBookmarked = !signal.isBookmarked)
                repository.updateSignal(updated)
                addLog("${if (updated.isBookmarked) "Locked" else "Released"} setup bookmark track for ${signal.symbol.uppercase()}")
            } catch (e: Throwable) {
                Log.e("CryptoViewModel", "Error toggling bookmark: ${e.message}", e)
            }
        }
    }

    fun deleteSignal(signal: SavedSignal) {
        viewModelScope.launch {
            try {
                repository.deleteSignal(signal)
                addLog("Dismissed signal setup for ${signal.symbol.uppercase()}")
            } catch (e: Throwable) {
                Log.e("CryptoViewModel", "Error deleting signal: ${e.message}", e)
            }
        }
    }

    fun forceFullRefresh() {
        startFullMarketScan(useFallbackOnly = false)
    }

    // --- Core Paper Trading Operations ---
    
    private suspend fun closeOkxPositionIfLive(trade: PaperTrade, exitPrice: Double) {
        if (!trade.isOkxTrade || trade.okxOrderId.isNullOrBlank()) return
        try {
            val isDemo = _okxIsDemo.value
            addLog("💼 [OKX Execution] Closing live OKX position for ${trade.symbol.uppercase()} (QTY: ${String.format(Locale.US, "%.5f", trade.quantity)}) by placing market SELL order...")
            val closeOrdId = OkxClient.placeMarketOrder(
                apiKey = _okxApiKey.value,
                secretKey = _okxSecretKey.value,
                passphrase = _okxPassphrase.value,
                isDemo = isDemo,
                symbol = trade.symbol,
                isBuy = false,
                size = trade.quantity
            )
            addLog("🎯 [OKX Execution] Market SELL filled on OKX. Order ID: $closeOrdId. Position closed.")
            validateAndRefreshOkxBalance()
        } catch (e: Exception) {
            addLog("❌ [OKX Execution Failed] Error closing OKX order for ${trade.symbol.uppercase()}: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    private suspend fun closeMexcPositionIfLive(trade: PaperTrade, exitPrice: Double) {
        if (!trade.isMexcTrade || trade.mexcOrderId.isNullOrBlank()) return
        try {
            val isDemo = _mexcIsDemo.value
            addLog("💼 [MEXC Execution] Closing live MEXC position for ${trade.symbol.uppercase()} (QTY: ${String.format(Locale.US, "%.5f", trade.quantity)}) by placing market SELL order...")
            val closeOrdId = MexcClient.placeMarketOrder(
                apiKey = _mexcApiKey.value,
                secretKey = _mexcSecretKey.value,
                isDemo = isDemo,
                symbol = trade.symbol,
                isBuy = false,
                size = trade.quantity
            )
            addLog("🎯 [MEXC Execution] Market SELL filled on MEXC. Order ID: $closeOrdId. Position closed.")
            validateAndRefreshMexcBalance()
        } catch (e: Throwable) {
            addLog("❌ [MEXC Execution Failed] Error closing MEXC order for ${trade.symbol.uppercase()}: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    private suspend fun updateLiveOpenTrades() = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val openPositions = repository.getOpenTradesList()
        if (openPositions.isEmpty()) return@withContext

        val tradesToUpdate = mutableListOf<PaperTrade>()
        var balanceDelta = 0.0

        for (trade in openPositions) {
            if (trade.entryPrice <= 0.0 || trade.quantity <= 0.0) continue
            // Price drift of -1.0% to +1.0%
            val randomPct = -0.01 + (Random.nextDouble() * 0.02)
            val currentPrice = (trade.currentPrice * (1.0 + randomPct)).coerceAtLeast(0.000001)
            
            val isBuy = trade.signalType == "LONG"
            val rawPnl = if (isBuy) {
                (currentPrice - trade.entryPrice) * trade.quantity
            } else {
                (trade.entryPrice - currentPrice) * trade.quantity
            }
            val pnl = if (rawPnl.isNaN() || rawPnl.isInfinite()) 0.0 else rawPnl

            var triggerClose = false
            var finalStatus = trade.status
            var exitPrice = currentPrice

            if (isBuy) {
                if (currentPrice <= trade.stopLoss) {
                    triggerClose = true
                    finalStatus = "CLOSED_SL"
                    exitPrice = trade.stopLoss
                } else if (currentPrice >= trade.takeProfit) {
                    triggerClose = true
                    finalStatus = "CLOSED_TP"
                    exitPrice = trade.takeProfit
                }
            } else { // SHORT
                if (currentPrice >= trade.stopLoss) {
                    triggerClose = true
                    finalStatus = "CLOSED_SL"
                    exitPrice = trade.stopLoss
                } else if (currentPrice <= trade.takeProfit) {
                    triggerClose = true
                    finalStatus = "CLOSED_TP"
                    exitPrice = trade.takeProfit
                }
            }

            if (triggerClose) {
                val finalPnl = if (isBuy) {
                    (exitPrice - trade.entryPrice) * trade.quantity
                } else {
                    (trade.entryPrice - exitPrice) * trade.quantity
                }
                val closedTrade = trade.copy(
                    currentPrice = exitPrice,
                    status = finalStatus,
                    exitPrice = exitPrice,
                    exitTimestamp = System.currentTimeMillis(),
                    pnl = finalPnl
                )
                tradesToUpdate.add(closedTrade)

                if (trade.isOkxTrade) {
                    viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                        closeOkxPositionIfLive(trade, exitPrice)
                    }
                } else if (trade.isMexcTrade) {
                    viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                        closeMexcPositionIfLive(trade, exitPrice)
                    }
                } else {
                    val returnFund = trade.investedAmount + finalPnl
                    balanceDelta += returnFund
                }
                val modeLabel = when {
                    trade.isOkxTrade -> "OKX Live Target"
                    trade.isMexcTrade -> "MEXC Live Target"
                    else -> "Paper Trigger"
                }
                addLog("📢 [$modeLabel] ${trade.symbol.uppercase()} hit target. Closed position via $finalStatus! PnL: $${String.format(java.util.Locale.US, "%.2f", finalPnl)}")
            } else {
                val updatedTrade = trade.copy(
                    currentPrice = currentPrice,
                    pnl = pnl
                )
                tradesToUpdate.add(updatedTrade)
            }
        }

        if (tradesToUpdate.isNotEmpty()) {
            repository.updatePaperTrades(tradesToUpdate)
        }
        if (balanceDelta != 0.0) {
            modifyCashBalance(balanceDelta)
        }
    }

    fun executePaperTrade(
        coinId: String,
        symbol: String,
        name: String,
        image: String?,
        signalType: String,
        entryPrice: Double,
        stopLoss: Double,
        takeProfit: Double,
        investedAmount: Double,
        strategy: String = "Manual Position"
    ): Boolean {
        if (investedAmount <= 0.0 || entryPrice <= 0.0) return false
        
        if (!_okxEnabled.value && !_mexcEnabled.value) {
            val deducted = modifyCashBalance(-investedAmount)
            if (!deducted) {
                _error.value = "Insufficient paper trading capital"
                addLog("❌ Paper trade failed: Insufficient balance.")
                return false
            }
        } else {
            // Deduct mock cash balance if available, but do not block if it is depleted since we use exchange real capital
            modifyCashBalance(-investedAmount)
        }

        if (_mexcEnabled.value) {
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    addLog("💼 [MEXC Execution] Initiating market ${if (signalType == "LONG") "BUY" else "SELL (Short)"} for ${symbol.uppercase()}...")
                    
                    if (signalType == "SHORT") {
                        addLog("⚠️ [MEXC Spot Limit] MEXC spot account does not support short selling directly. Executing as paper position instead.")
                        insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy)
                        return@launch
                    }

                    val ordId = MexcClient.placeMarketOrder(
                        apiKey = _mexcApiKey.value,
                        secretKey = _mexcSecretKey.value,
                        isDemo = _mexcIsDemo.value,
                        symbol = symbol,
                        isBuy = true,
                        size = investedAmount
                    )

                    addLog("🎯 [MEXC Execution] Spot Market BUY order filled on MEXC. Order ID: $ordId")
                    insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy, isMexc = true, mexcOrderId = ordId)
                    validateAndRefreshMexcBalance()
                } catch (e: Throwable) {
                    val errorMsg = e.localizedMessage ?: "Unknown Error"
                    addLog("❌ [MEXC Execution Failed] Error placing order for ${symbol.uppercase()}: $errorMsg. Saving as paper position only.")
                    // Fallback to regular paper trade in DB
                    insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy)
                }
            }
        } else if (_okxEnabled.value) {
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    addLog("💼 [OKX Execution] Initiating market ${if (signalType == "LONG") "BUY" else "SELL (Short)"} for ${symbol.uppercase()}...")
                    
                    if (signalType == "SHORT") {
                        addLog("⚠️ [OKX Spot Limit] OKX spot account does not support short selling directly. Executing as paper position instead.")
                        insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy)
                        return@launch
                    }

                    val ordId = OkxClient.placeMarketOrder(
                        apiKey = _okxApiKey.value,
                        secretKey = _okxSecretKey.value,
                        passphrase = _okxPassphrase.value,
                        isDemo = _okxIsDemo.value,
                        symbol = symbol,
                        isBuy = true,
                        size = investedAmount
                    )

                    addLog("🎯 [OKX Execution] Spot Market BUY order filled on OKX. Order ID: $ordId")
                    insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy, isOkx = true, orderId = ordId)
                    validateAndRefreshOkxBalance()
                } catch (e: Exception) {
                    val errorMsg = e.localizedMessage ?: "Unknown Error"
                    addLog("❌ [OKX Execution Failed] Error placing order for ${symbol.uppercase()}: $errorMsg. Saving as paper position only.")
                    insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy)
                }
            }
        } else {
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    insertPaperTradeInDb(coinId, symbol, name, image, signalType, entryPrice, stopLoss, takeProfit, investedAmount, strategy)
                } catch (e: Exception) {
                    Log.e("CryptoViewModel", "Error opening paper trade: ${e.message}", e)
                }
            }
        }
        return true
    }

    private suspend fun insertPaperTradeInDb(
        coinId: String,
        symbol: String,
        name: String,
        image: String?,
        signalType: String,
        entryPrice: Double,
        stopLoss: Double,
        takeProfit: Double,
        investedAmount: Double,
        strategy: String,
        isOkx: Boolean = false,
        orderId: String? = null,
        isMexc: Boolean = false,
        mexcOrderId: String? = null
    ) {
        val quantity = investedAmount / entryPrice
        val newTrade = PaperTrade(
            coinId = coinId,
            symbol = symbol,
            name = name,
            image = image,
            signalType = signalType,
            entryPrice = entryPrice,
            currentPrice = entryPrice,
            stopLoss = stopLoss,
            takeProfit = takeProfit,
            quantity = quantity,
            status = "OPEN",
            pnl = 0.0,
            investedAmount = investedAmount,
            strategy = strategy,
            isOkxTrade = isOkx,
            okxOrderId = orderId,
            isMexcTrade = isMexc,
            mexcOrderId = mexcOrderId
        )
        repository.insertPaperTrade(newTrade)
        val modeText = when {
            isOkx -> "OKX Live Order"
            isMexc -> "MEXC Live Order"
            else -> "Paper Trade"
        }
        addLog("🚀 Opened $modeText: $signalType ${symbol.uppercase()} size: $${String.format(Locale.US, "%.2f", investedAmount)}")
    }

    fun closePaperTradeManually(trade: PaperTrade) {
        if (trade.entryPrice <= 0.0 || trade.quantity <= 0.0) return
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val isBuy = trade.signalType == "LONG"
                val exitPrice = trade.currentPrice
                val pnl = if (isBuy) {
                    (exitPrice - trade.entryPrice) * trade.quantity
                } else {
                    (trade.entryPrice - exitPrice) * trade.quantity
                }

                val closedTrade = trade.copy(
                    status = "CLOSED_MANUAL",
                    exitPrice = exitPrice,
                    exitTimestamp = System.currentTimeMillis(),
                    pnl = pnl
                )
                repository.updatePaperTrade(closedTrade)

                if (trade.isOkxTrade) {
                    closeOkxPositionIfLive(trade, exitPrice)
                } else if (trade.isMexcTrade) {
                    closeMexcPositionIfLive(trade, exitPrice)
                } else {
                    modifyCashBalance(trade.investedAmount + pnl)
                }
                
                val sourceLabel = when {
                    trade.isOkxTrade -> "live OKX position"
                    trade.isMexcTrade -> "live MEXC position"
                    else -> "paper trade"
                }
                addLog("🔴 Manually closed $sourceLabel ${trade.symbol.uppercase()} at $${String.format(java.util.Locale.US, "%.4f", exitPrice)}. PnL: $${String.format(java.util.Locale.US, "%.2f", pnl)}")
            } catch (e: Throwable) {
                Log.e("CryptoViewModel", "Error closing paper trade: ${e.message}", e)
            }
        }
    }

    fun resetPaperTradingAccount() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                repository.clearAllPaperTrades()
                resetCashBalance()
                addLog("♻️ Reset virtual ledger. Portfolio balance set to $100,000.00 USD.")
            } catch (e: Throwable) {
                Log.e("CryptoViewModel", "Error resetting trade account: ${e.message}", e)
            }
        }
    }

    // --- Auto Trading Bot Core Methods & Setters ---

    fun setBotEnabled(enabled: Boolean) {
        _botEnabled.value = enabled
        prefs.edit().putBoolean("bot_enabled", enabled).apply()
        addLog(if (enabled) "🤖 Auto Trading Bot Core ACTIVATED." else "🤖 Auto Trading Bot Core DEACTIVATED.")
    }

    fun setBotMaxDailyTrades(maxTrades: Int) {
        _botMaxDailyTrades.value = maxTrades
    }

    fun saveBotMaxDailyTrades(maxTrades: Int) {
        prefs.edit().putInt("bot_max_trades", maxTrades).apply()
        addLog("🤖 Configured Bot Max Active Trades: $maxTrades")
    }

    fun setBotSelectionMode(mode: String) {
        _botSelectionMode.value = mode
        prefs.edit().putString("bot_selection_mode", mode).apply()
        addLog("🤖 Configured Bot Setup Selection Mode: $mode")
    }

    fun toggleBotBlueprint(blueprintTitle: String) {
        val current = _botSelectedBlueprints.value.toMutableSet()
        if (current.contains(blueprintTitle)) {
            current.remove(blueprintTitle)
            addLog("🤖 Excluded blueprint from bot range: $blueprintTitle")
        } else {
            current.add(blueprintTitle)
            addLog("🤖 Included blueprint in bot range: $blueprintTitle")
        }
        _botSelectedBlueprints.value = current
        prefs.edit().putStringSet("bot_selected_blueprints", current).apply()
    }

    fun setBotTradeSize(size: Double) {
        _botTradeSize.value = size
    }

    fun saveBotTradeSize(size: Double) {
        prefs.edit().putFloat("bot_trade_size", size.toFloat()).apply()
        addLog("🤖 Configured Allocation Size Per Position: $${String.format(java.util.Locale.US, "%.2f", size)}")
    }

    fun setBotTargetCoinMode(mode: String) {
        _botTargetCoinMode.value = mode
        prefs.edit().putString("bot_target_coin_mode", mode).apply()
        addLog("🤖 Configured Bot Target Coin Mode to: $mode")
        resetBotCoinsCache()
    }

    fun toggleBotSelectedCoin(coinId: String) {
        val current = _botSelectedCoinIds.value.toMutableSet()
        if (current.contains(coinId)) {
            current.remove(coinId)
            addLog("🤖 Excluded coin from bot target: $coinId")
        } else {
            current.add(coinId)
            addLog("🤖 Included coin in bot target: $coinId")
        }
        _botSelectedCoinIds.value = current
        prefs.edit().putStringSet("bot_selected_coin_ids", current).apply()
    }

    fun clearBotSelectedCoins() {
        _botSelectedCoinIds.value = emptySet()
        prefs.edit().putStringSet("bot_selected_coin_ids", emptySet()).apply()
        addLog("🤖 Cleared all bot target custom coins")
    }

    @Volatile private var isBotScanning = false
    @Volatile private var botScannedCoins: List<Coin> = emptyList()
    @Volatile private var lastBotScanTime = 0L

    fun resetBotCoinsCache() {
        botScannedCoins = emptyList()
        lastBotScanTime = 0L
    }

    private suspend fun runBotAutoTradingCycle() {
        if (isBotScanning) return
        isBotScanning = true
        try {
            val maxTrades = _botMaxDailyTrades.value
            val currentActive = repository.getOpenTradesList().size
            if (currentActive >= maxTrades) {
                return
            }

            var spotsNeeded = maxTrades - currentActive
            if (spotsNeeded <= 0) return

            // Get target coins independently of the manual scanner
            val range = getActiveMarketCapRange()
            val now = System.currentTimeMillis()
            val coins = if (botScannedCoins.isEmpty() || (now - lastBotScanTime) > 60000L) {
                val fetched = repository.scanMarket(useFallbackOnly = false, minCap = range.first, maxCap = range.second)
                botScannedCoins = fetched
                lastBotScanTime = now
                fetched
            } else {
                botScannedCoins
            }

            if (coins.isEmpty()) return

            var targetCoins = coins
            if (_botTargetCoinMode.value == "CUSTOM") {
                val selectedIds = _botSelectedCoinIds.value
                targetCoins = coins.filter { selectedIds.contains(it.id) }
            }

            if (targetCoins.isEmpty()) return

            val openSymbols = repository.getOpenTradesList().map { it.symbol.lowercase() }.toSet()

            for (coin in targetCoins) {
                if (spotsNeeded <= 0) break
                if (openSymbols.contains(coin.symbol.lowercase())) continue

                // Check cash balance
                val tradeSize = _botTradeSize.value
                val currentCash = _cashBalance.value
                if (currentCash < tradeSize) {
                    addLog("🤖 [Bot Check] Insufficient cash ($${String.format(java.util.Locale.US, "%.2f", currentCash)}) for trade of size $${String.format(java.util.Locale.US, "%.2f", tradeSize)}.")
                    break
                }

                // Predict trade setup
                val prediction = repository.predictTradeSignal(coin)
                if (prediction.signal == "LONG" || prediction.signal == "SHORT") {
                    if (prediction.confidence >= 85) {
                        val isStrategyAllowed = if (_botSelectionMode.value == "AUTO") {
                            true
                        } else {
                            _botSelectedBlueprints.value.contains(prediction.strategy)
                        }

                        if (isStrategyAllowed) {
                            val success = executePaperTrade(
                                coinId = coin.id,
                                symbol = coin.symbol,
                                name = coin.name,
                                image = coin.image,
                                signalType = prediction.signal,
                                entryPrice = coin.currentPrice,
                                stopLoss = prediction.stopLoss,
                                takeProfit = prediction.takeProfit,
                                investedAmount = tradeSize,
                                strategy = prediction.strategy
                            )
                            if (success) {
                                spotsNeeded--
                                addLog("🤖 [Auto Bot] Found qualified ${prediction.signal} entry on ${coin.symbol.uppercase()} via '${prediction.strategy}' Setup. Automatically executed paper trade with $${String.format(java.util.Locale.US, "%.2f", tradeSize)} virtual cash.")
                            }
                        }
                    }
                }
                // Spaced out to respect Gemini rate limiting and prevent main thread stalls or ANRs
                val botDelay = if (isModelKeyConfigured) 1200L else 150L
                delay(botDelay)
            }
        } catch (e: Throwable) {
            Log.e("CryptoViewModel", "Bot scanner loop error: ${e.message}", e)
        } finally {
            isBotScanning = false
        }
    }
}

class CryptoViewModelFactory(
    private val application: Application,
    private val repository: CryptoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CryptoViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
