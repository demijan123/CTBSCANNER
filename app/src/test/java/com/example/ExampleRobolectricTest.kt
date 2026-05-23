package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.SavedSignal
import com.example.data.local.PaperTrade
import com.example.data.repository.CryptoRepository
import com.example.ui.CryptoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import androidx.test.core.app.ActivityScenario
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Crypto Signal Scanner", appName)
  }

  @Test
  fun `test activity launch`() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      assertNotNull(scenario)
    }
  }

  @Test
  fun `test database and viewmodel initialization`() = runTest {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val db = AppDatabase.getDatabase(context)
    val coinDao = db.coinDao()
    val paperDao = db.paperTradeDao()
    
    val repository = CryptoRepository(coinDao, paperDao)
    
    // Check tables are empty or populated
    assertNotNull(repository)
    
    // Test database inserts
    val sampleTrade = PaperTrade(
        coinId = "bitcoin",
        symbol = "btc",
        name = "Bitcoin",
        image = null,
        signalType = "LONG",
        entryPrice = 60000.0,
        currentPrice = 61000.0,
        stopLoss = 58000.0,
        takeProfit = 65000.0,
        quantity = 0.5,
        status = "OPEN",
        investedAmount = 30000.0
    )
    val tradeId = paperDao.insertTrade(sampleTrade)
    assertEquals(1L, tradeId)
    
    val openTradesList = paperDao.getOpenTrades()
    assertEquals(1, openTradesList.size)
    assertEquals("bitcoin", openTradesList[0].coinId)
    
    val viewModel = CryptoViewModel(context, repository)
    assertNotNull(viewModel)
    
    // Check default cash balance
    assertEquals(100000.0, viewModel.cashBalance.value, 0.001)
  }
}

