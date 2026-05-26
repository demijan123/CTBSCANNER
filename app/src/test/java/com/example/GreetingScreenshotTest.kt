package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent { MyApplicationTheme { androidx.compose.material3.Text("Crypto Signal Dashboard") } }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }

  @Test
  fun dashboard_rendering_test() {
    val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>() as android.app.Application
    val db = com.example.data.local.AppDatabase.getDatabase(context)
    val repository = com.example.data.repository.CryptoRepository(db.coinDao(), db.paperTradeDao())
    val viewModel = com.example.ui.CryptoViewModel(context, repository)

    composeTestRule.setContent {
      MyApplicationTheme {
        MainDesktopDashboard(viewModel = viewModel)
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/dashboard_test.png")
  }

  @Test
  fun test_backtest_simulation_renders_and_runs() {
    val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>() as android.app.Application
    val db = com.example.data.local.AppDatabase.getDatabase(context)
    val repository = com.example.data.repository.CryptoRepository(db.coinDao(), db.paperTradeDao())
    val viewModel = com.example.ui.CryptoViewModel(context, repository)
    val latestBacktestResults = androidx.compose.runtime.mutableStateMapOf<String, com.example.SimulationResult>()

    composeTestRule.setContent {
      MyApplicationTheme {
        StrategyBlueprintsTab(viewModel = viewModel, latestBacktestResults = latestBacktestResults)
      }
    }

    // Capture the initial blueprints list screenshot
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/blueprints_initial.png")

    // Find and click the first blueprint's RUN BACKTEST SIMULATION button
    composeTestRule.onNodeWithTag("run_backtest_btn_ema_continuation_cross_(v3)").performClick()

    // Select a particular focus coin (e.g., BRETT)
    composeTestRule.onNodeWithTag("coin_asset_chip_brett").performClick()

    // Enter manual initial capital (e.g., $15000)
    composeTestRule.onNodeWithTag("custom_capital_input").performTextReplacement("15000")

    // Capture the simulator input screen screenshot
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/backtest_inputs.png")

    // Find and click the "RUN SIMULATED HISTORICAL RANGE" button
    composeTestRule.onNodeWithText("RUN SIMULATED HISTORICAL RANGE").performClick()

    // Since delay is automatically managed and advanced under Robolectric, 
    // the simulation and results rendering will execute synchronously.
    
    // Capture the completed results screen screenshot!
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/backtest_results.png")
  }

  @Test
  fun test_mexc_console_tab_rendering() {
    val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>() as android.app.Application
    val db = com.example.data.local.AppDatabase.getDatabase(context)
    val repository = com.example.data.repository.CryptoRepository(db.coinDao(), db.paperTradeDao())
    val viewModel = com.example.ui.CryptoViewModel(context, repository)

    composeTestRule.setContent {
      MyApplicationTheme {
        MexcTradingConsoleTab(viewModel = viewModel)
      }
    }
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/mexc_console_rendered.png")
  }

  @Test
  fun test_mexc_demo_trades_tab_rendering() {
    val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>() as android.app.Application
    val db = com.example.data.local.AppDatabase.getDatabase(context)
    val repository = com.example.data.repository.CryptoRepository(db.coinDao(), db.paperTradeDao())
    val viewModel = com.example.ui.CryptoViewModel(context, repository)

    composeTestRule.setContent {
      MyApplicationTheme {
        MexcTradesTab(viewModel = viewModel, isDemo = true)
      }
    }
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/mexc_demo_trades_rendered.png")
  }

  @Test
  fun test_mexc_live_trades_tab_rendering() {
    val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>() as android.app.Application
    val db = com.example.data.local.AppDatabase.getDatabase(context)
    val repository = com.example.data.repository.CryptoRepository(db.coinDao(), db.paperTradeDao())
    val viewModel = com.example.ui.CryptoViewModel(context, repository)

    composeTestRule.setContent {
      MyApplicationTheme {
        MexcTradesTab(viewModel = viewModel, isDemo = false)
      }
    }
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/mexc_live_trades_rendered.png")
  }
}
