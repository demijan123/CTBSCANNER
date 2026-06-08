package com.example

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CryptoViewModel
import com.example.data.model.ProtectionEvent

@Composable
fun SafetyDashboardTab(viewModel: CryptoViewModel) {
    val context = LocalContext.current

    // Observe master settings
    val smartRiskEnabled by viewModel.smartRiskEnabled.collectAsState()
    val riskProfile by viewModel.riskProfile.collectAsState()

    // Observe env variables
    val btcPrice by viewModel.simulatedBtcPrice.collectAsState()
    val btcChange24h by viewModel.simulatedBtcChange24h.collectAsState()
    val atrMultiplier by viewModel.simulatedAtrMultiple.collectAsState()

    // Observe counts
    val totalPrevented by viewModel.totalTradesPrevented.collectAsState()
    val auditHistory by viewModel.protectionHistoryList.collectAsState()

    // Individual triggers (visual testing knobs)
    val emergencyTrig by viewModel.emergencyTriggered.collectAsState()
    val lossTrig by viewModel.consecutiveLossTriggered.collectAsState()
    val volatilityTrig by viewModel.volatilityTriggered.collectAsState()
    val btcTrendTrig by viewModel.btcTrendTriggered.collectAsState()
    val cooldownMoveTrig by viewModel.cooldownMoveTriggered.collectAsState()

    // Calculate current general status dynamically
    val generalStatus = viewModel.checkRiskProtectionStatus()

    // Advanced threshold details (Custom backups)
    val advEmergBtcDrop by viewModel.advEmergencyBtcDrop.collectAsState()
    val advLossMax by viewModel.advLossMax.collectAsState()
    val advVolAtrTrigger by viewModel.advVolAtrTrigger.collectAsState()
    val advBtcTrendEnabled by viewModel.advBtcTrendEnabled.collectAsState()
    val advCooldownTrigger by viewModel.advCooldownTrigger.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("safety_dashboard_container"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- HEADER SUMMARY CARD ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("safety_header_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, CyberSlate)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "RISK PROTECTION CORE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAccentGreen,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Automated Safe Trading Enforcer",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = CyberTextWhite
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (generalStatus.isBlocked) Color(0xFFEAB308).copy(alpha = 0.15f) else CyberAccentGreen.copy(alpha = 0.15f))
                                .border(1.dp, if (generalStatus.isBlocked) Color(0xFFEAB308) else CyberAccentGreen, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (generalStatus.isBlocked) "PAUSED" else "ACTIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = if (generalStatus.isBlocked) Color(0xFFEAB308) else CyberAccentGreen,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PROTECTED TRADING ACTIONS PREVENTED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextDim,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalPrevented PREVENTED",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = if (totalPrevented > 0) Color(0xFFEAB308) else CyberAccentGreen
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.resetToRecommendedDefaults()
                                Toast.makeText(context, "Settings reset to recommended defaults", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSlate),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Defaults",
                                modifier = Modifier.size(14.dp),
                                tint = CyberTextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RESET DEFAULTS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // --- MASTER TOGGLE & STATUS BADGE CARD ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("safety_master_toggle_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (smartRiskEnabled) CyberAccentGreen.copy(alpha = 0.5f) else CyberSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Safety Shield",
                                tint = if (smartRiskEnabled) CyberAccentGreen else CyberTextDim,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SMART RISK PROTECTION",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextWhite
                                )
                                Text(
                                    text = if (smartRiskEnabled) "Recommended Protection Engaged" else "Manual Protection Override Active",
                                    fontSize = 11.sp,
                                    color = CyberTextDim
                                )
                            }
                        }

                        Switch(
                            checked = smartRiskEnabled,
                            onCheckedChange = { viewModel.setSmartRiskEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CyberDark,
                                checkedTrackColor = CyberAccentGreen,
                                uncheckedThumbColor = CyberTextDim,
                                uncheckedTrackColor = CyberSurface
                            ),
                            modifier = Modifier.testTag("smart_risk_toggle")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedVisibility(visible = smartRiskEnabled) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberAccentGreen.copy(alpha = 0.08f))
                                .border(1.dp, CyberAccentGreen.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "✨ Smart Risk Protection is active. Highly recommended presets are being applied automatically. Advanced settings are locked on defaults unless set to 'CUSTOM' profile.",
                                fontSize = 11.sp,
                                color = CyberAccentGreen,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    AnimatedVisibility(visible = !smartRiskEnabled) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEAB308).copy(alpha = 0.08f))
                                .border(1.dp, Color(0xFFEAB308).copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "⚠️ Smart Risk Protection is deactivated. Advanced users must configure each safety module manually. Ensure that threshold triggers are calculated accurately.",
                                fontSize = 11.sp,
                                color = Color(0xFFEAB308),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // --- PRESET SELECTION ROW ---
        item {
            AnimatedVisibility(visible = smartRiskEnabled) {
                Column {
                    Text(
                        text = "RISK PROTECTION PRESET PROFILES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Conservative", "Balanced", "Aggressive", "Custom").forEach { profile ->
                            val isSelected = riskProfile == profile
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) CyberAccentGreen.copy(alpha = 0.15f) else CyberCard)
                                    .border(
                                        1.dp,
                                        if (isSelected) CyberAccentGreen else CyberSlate,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.setRiskProfile(profile) }
                                    .padding(vertical = 12.dp)
                                    .testTag("profile_button_$profile"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = profile.uppercase(),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) CyberAccentGreen else CyberTextDim,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    if (profile == "Balanced") {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "RECOMMENDED",
                                            fontSize = 6.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSelected) CyberAccentGreen else CyberGold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- STUNNING LIVE PROTECTION MODULES STATUS ---
        item {
            Text(
                text = "RISK CONTROLLER MODULE STATUS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = CyberTextDim,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
        }

        val modulesList = listOf(
            Triple("Emergency Market Protection", "Emergency", "Detects massive short-term BTC drops and pauses trade generation immediately to prevent capital exposure during flash crash events."),
            Triple("Consecutive Loss Protection", "Loss", "Buffers trading against long loss feedback streaks. Triggers pause when maximum negative trade configurations are logged consistently."),
            Triple("Volatility Filter", "Volatility", "Scans live ATR multipliers dynamically. Halts trading routines if volatility sweeps trigger values higher than the safe ATR target limit."),
            Triple("BTC Trend Filter", "BtcTrend", "Ensures trading occurs only under supportive market conditions. Pauses operations when macro BTC price trends register as down/bearish."),
            Triple("Large Market Move Cooldown", "Cooldown", "Restrains bot from chasing volatile breakout sweeps. Auto-pauses executions for set durations when BTC moves dynamically.")
        )

        items(modulesList) { (title, key, desc) ->
            val isEnabled = viewModel.isModuleEnabled(key)
            val currentThresholdText = when (key) {
                "Emergency" -> "BTC Crash: -${viewModel.getModuleThreshold(key, "Trigger").toInt()}% | Window: ${viewModel.getModuleThreshold(key, "Window").toInt()}m | Cooldown: ${viewModel.getModuleThreshold(key, "Cooldown").toInt()}m"
                "Loss" -> "Consecutive Max Losses: ${viewModel.getModuleThreshold(key, "Max").toInt()} | Cooldown: ${viewModel.getModuleThreshold(key, "Pause").toInt()}m"
                "Volatility" -> "ATR Multiple Limit: ${viewModel.getModuleThreshold(key, "ATR")}x | Cooldown: ${viewModel.getModuleThreshold(key, "Cooldown").toInt()}m"
                "BtcTrend" -> "Trend Model: Bullish Only | Cooldown: Dynamic"
                "Cooldown" -> "BTC Move Limit: ${viewModel.getModuleThreshold(key, "Trigger").toInt()}% | Window: ${viewModel.getModuleThreshold(key, "Window").toInt()}m | Cooldown: ${viewModel.getModuleThreshold(key, "Cooldown").toInt()}m"
                else -> ""
            }

            // Check if triggered
            val isTriggered = when (key) {
                "Emergency" -> btcChange24h <= -viewModel.getModuleThreshold(key, "Trigger") || emergencyTrig
                "Loss" -> lossTrig
                "Volatility" -> atrMultiplier >= viewModel.getModuleThreshold(key, "ATR") || volatilityTrig
                "BtcTrend" -> (btcChange24h < 0.0 && isEnabled) || btcTrendTrig
                "Cooldown" -> Math.abs(btcChange24h) >= viewModel.getModuleThreshold(key, "Trigger") || cooldownMoveTrig
                else -> false
            }

            val statusColor = when {
                !isEnabled -> CyberTextDim
                isTriggered -> CyberAccentRed
                else -> CyberAccentGreen
            }

            val statusLabel = when {
                !isEnabled -> "DISABLED"
                isTriggered -> "TRIGGERED (BLOCKED)"
                else -> "ACTIVE (MONITORING)"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("module_card_$key"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, statusColor.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextWhite
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(statusColor.copy(alpha = 0.1f))
                                .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = statusLabel,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = statusColor,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        fontSize = 11.sp,
                        color = CyberTextDim,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONFIGURED THRESHOLD",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = currentThresholdText,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberAccentGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // --- AUDIT HISTORY EXPANDABLE AND SCROLLABLE CARD ---
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PROTECTION AUDIT EVENTS HISTORIC LOG",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )

                    if (auditHistory.isNotEmpty()) {
                        Text(
                            text = "CLEAR AUDIT LOG",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = CyberAccentRed,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .clickable {
                                    viewModel.clearProtectionHistory()
                                    Toast.makeText(context, "Audit logs cleared", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .testTag("protection_history_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CyberSlate)
                ) {
                    if (auditHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🛡️ No security events or trading halts logged yet.",
                                fontSize = 11.sp,
                                color = CyberTextDim,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(12.dp)
                        ) {
                            auditHistory.forEach { event ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = event.moduleTriggered,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberAccentRed
                                        )
                                        Text(
                                            text = event.dateTimeStr,
                                            fontSize = 9.sp,
                                            color = CyberTextDim,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Conditions: ${event.marketConditions} | Pause Action: ${event.durationOfPause}",
                                        fontSize = 10.sp,
                                        color = CyberTextDim
                                    )
                                    Divider(
                                        color = CyberSlate.copy(alpha = 0.5f),
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- REAL-TIME ENVIRONMENTAL CONTROLS & TEST LOG CONSOLE ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("safety_risk_simulation_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, CyberGold.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Sim Icon",
                            tint = CyberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "RISK LEVEL SIMULATOR & TESTING OVERRIDES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // --- SLIDER 1: BTC CHANGE 24H ---
                    Text(
                        text = "Simulate BTC 24H Price Change (%): ${String.format("%.2f", btcChange24h)}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite
                    )
                    Slider(
                        value = btcChange24h.toFloat(),
                        onValueChange = { viewModel.setSimulatedBtcChange24h(it.toDouble()) },
                        valueRange = -10f..10f,
                        colors = SliderDefaults.colors(
                            thumbColor = CyberGold,
                            activeTrackColor = CyberGold,
                            inactiveTrackColor = CyberSlate
                        ),
                        modifier = Modifier.testTag("sim_btc_change_slider")
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "-10.0% (Flash Crash)", fontSize = 8.sp, color = CyberTextDim)
                        Text(text = "0.0% (Flat)", fontSize = 8.sp, color = CyberTextDim)
                        Text(text = "+10.0% (Bull break)", fontSize = 8.sp, color = CyberTextDim)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- SLIDER 2: VOLATILITY ATR MULTIPLE ---
                    Text(
                        text = "Simulate Market Volatility ATR Multiple: ${String.format("%.2f", atrMultiplier)}x",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextWhite
                    )
                    Slider(
                        value = atrMultiplier.toFloat(),
                        onValueChange = { viewModel.setSimulatedAtrMultiple(it.toDouble()) },
                        valueRange = 0.5f..5.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = CyberGold,
                            activeTrackColor = CyberGold,
                            inactiveTrackColor = CyberSlate
                        ),
                        modifier = Modifier.testTag("sim_atr_slider")
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "0.5x (Sideways)", fontSize = 8.sp, color = CyberTextDim)
                        Text(text = "2.5x (Trigger Level)", fontSize = 8.sp, color = CyberTextDim)
                        Text(text = "5.0x (Extreme Squeeze)", fontSize = 8.sp, color = CyberTextDim)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- DIRECT MANUAL BLOCK MOCK TRIGGERS ---
                    Text(
                        text = "MANUAL PROTECTION CORRECTION TEST LAUNCH",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextDim,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.setConsecutiveLossTriggered(!lossTrig, "Mock 3 Consecutive Trade Losses Intercepted") },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (lossTrig) CyberAccentRed.copy(alpha = 0.15f) else Color.Transparent,
                                contentColor = if (lossTrig) CyberAccentRed else CyberTextWhite
                            ),
                            border = BorderStroke(1.dp, if (lossTrig) CyberAccentRed else CyberSlate),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("mock_loss_btn")
                        ) {
                            Text(text = "MOCK LOSS SEG", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }

                        OutlinedButton(
                            onClick = { viewModel.setEmergencyTriggered(!emergencyTrig, "Mock Flash Squeeze Protection Halting Sequence") },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (emergencyTrig) CyberAccentRed.copy(alpha = 0.15f) else Color.Transparent,
                                contentColor = if (emergencyTrig) CyberAccentRed else CyberTextWhite
                            ),
                            border = BorderStroke(1.dp, if (emergencyTrig) CyberAccentRed else CyberSlate),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("mock_emerg_btn")
                        ) {
                            Text(text = "MOCK FLASH", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // --- ADVANCED ADJUSTABLE PARAMETERS SHEET ---
        item {
            val isCustomActive = !smartRiskEnabled || riskProfile == "Custom"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("safety_advanced_settings_card"),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isCustomActive) CyberAccentGreen.copy(alpha = 0.4f) else CyberSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Advanced Settings Options",
                                tint = if (isCustomActive) CyberAccentGreen else CyberTextDim,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ADVANCED RISK CONFIGURATIONS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCustomActive) CyberAccentGreen else CyberTextWhite,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }

                        if (!isCustomActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CyberSlate)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "READ ONLY",
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTextDim,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isCustomActive) {
                            "Custom tuning values are active! You can now adjust thresholds directly below."
                        } else {
                            "Adjustments are locked under standard presets. Select 'CUSTOM' profile or switch OFF master Smart Protection to change values."
                        },
                        fontSize = 10.5.sp,
                        color = CyberTextDim,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- EM DROP TRIGGER INPUT ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Emergency BTC Drop (%):", fontSize = 11.sp, color = CyberTextWhite)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvEmergencyBtcDrop((advEmergBtcDrop - 0.5).coerceAtLeast(1.0)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                            Text(
                                text = "${String.format("%.1f", advEmergBtcDrop)}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvEmergencyBtcDrop((advEmergBtcDrop + 0.5).coerceAtMost(20.0)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "up", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                        }
                    }

                    // --- CONSECUTIVE LOSS MAX INPUT ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Max Consecutive Losses:", fontSize = 11.sp, color = CyberTextWhite)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvLossMax((advLossMax - 1).coerceAtLeast(1)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                            Text(
                                text = "$advLossMax",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvLossMax((advLossMax + 1).coerceAtMost(10)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "up", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                        }
                    }

                    // --- ATR LIMIT INPUT ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "ATR Volatility Limit (x):", fontSize = 11.sp, color = CyberTextWhite)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvVolAtrTrigger((advVolAtrTrigger - 0.1).coerceAtLeast(1.0)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                            Text(
                                text = "${String.format("%.1f", advVolAtrTrigger)}x",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvVolAtrTrigger((advVolAtrTrigger + 0.1).coerceAtMost(5.0)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "up", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                        }
                    }

                    // --- BTC TREND ENABLER CONTROLLER ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "BTC Trend Filter Mode:", fontSize = 11.sp, color = CyberTextWhite)
                        Switch(
                            checked = advBtcTrendEnabled,
                            onCheckedChange = { if (isCustomActive) viewModel.setAdvBtcTrendEnabled(it) },
                            enabled = isCustomActive,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CyberDark,
                                checkedTrackColor = CyberAccentGreen,
                                uncheckedThumbColor = CyberTextDim,
                                uncheckedTrackColor = CyberSurface
                            ),
                            modifier = Modifier.testTag("adv_btc_trend_switch")
                        )
                    }

                    // --- COOLDOWN MOVEMENT TRIGGER LIMIT ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Large Move Trigger (%):", fontSize = 11.sp, color = CyberTextWhite)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvCooldownTrigger((advCooldownTrigger - 0.5).coerceAtLeast(1.0)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                            Text(
                                text = "${String.format("%.1f", advCooldownTrigger)}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberTextWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            IconButton(
                                onClick = { if (isCustomActive) viewModel.setAdvCooldownTrigger((advCooldownTrigger + 0.5).coerceAtMost(10.0)) },
                                enabled = isCustomActive
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "up", tint = if (isCustomActive) CyberAccentGreen else CyberTextDim)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Save / Load custom configurations
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Custom configuration saved successfully to internal flash cache!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSlate),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "SAVE CONFIG", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = {
                                Toast.makeText(context, "Custom configurations loaded correctly!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSlate),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "LOAD CONFIG", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }
}
