package com.example.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object MexcClient {
    private const val TAG = "MexcClient"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789abcdef"
        val result = java.lang.StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val i = b.toInt() and 0xFF
            result.append(hexChars[i shr 4])
            result.append(hexChars[i and 0x0F])
        }
        return result.toString()
    }

    private fun hmacSha256Hex(data: String, secretKey: String): String {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val hmacBytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return bytesToHex(hmacBytes)
    }

    /**
     * Validates the credentials by fetching the available USDT balance.
     * Returns the USDT available balance if successful.
     */
    suspend fun validateAndFetchBalance(
        apiKey: String,
        secretKey: String,
        isDemo: Boolean
    ): Double = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || secretKey.isBlank()) {
            throw IllegalArgumentException("API Key and Secret must not be empty.")
        }

        if (isDemo) {
            // Simulated Demo/Sandbox environment for MEXC as MEXC has no public testnet
            return@withContext 100000.0
        }

        val baseUrl = "https://api.mexc.com"
        val path = "/api/v3/account"
        val timestamp = System.currentTimeMillis().toString()
        val queryString = "timestamp=$timestamp&recvWindow=6000"
        val signature = hmacSha256Hex(queryString, secretKey)
        val url = "$baseUrl$path?$queryString&signature=$signature"

        val request = Request.Builder()
            .url(url)
            .addHeader("X-MEXC-APIKEY", apiKey)
            .addHeader("Accept", "application/json")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyText = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    throw Exception("HTTP Error ${response.code}: $bodyText")
                }

                val json = JSONObject(bodyText)
                val balancesArray = json.optJSONArray("balances")
                if (balancesArray != null) {
                    for (i in 0 until balancesArray.length()) {
                        val balObj = balancesArray.getJSONObject(i)
                        if (balObj.optString("asset").uppercase() == "USDT") {
                            val free = balObj.optDouble("free", 0.0)
                            return@withContext free
                        }
                    }
                }
                return@withContext 0.0
            }
        } catch (e: Exception) {
            Log.e(TAG, "validateAndFetchBalance failed", e)
            throw e
        }
    }

    /**
     * Places a market order on MEXC.
     * Returns the order ID if successful.
     */
    suspend fun placeMarketOrder(
        apiKey: String,
        secretKey: String,
        isDemo: Boolean,
        symbol: String,  // e.g. "BTC" or "ETH"
        isBuy: Boolean,
        size: Double     // For buy: USDT amount to spend. For sell: Coin quantity to sell.
    ): String = withContext(Dispatchers.IO) {
        if (isDemo) {
            // Simulated order ID for MEXC Demo
            val randomId = (10000000..99999999).random().toString()
            return@withContext "MEXC-DEMO-$randomId"
        }

        val baseUrl = "https://api.mexc.com"
        val path = "/api/v3/order"
        val timestamp = System.currentTimeMillis().toString()

        val symbolUpper = if (symbol.uppercase().endsWith("USDT")) {
            symbol.uppercase()
        } else {
            "${symbol.uppercase()}USDT"
        }

        val queryMap = mutableMapOf<String, String>().apply {
            put("symbol", symbolUpper)
            put("side", if (isBuy) "BUY" else "SELL")
            put("type", "MARKET")
            if (isBuy) {
                put("quoteOrderQty", String.format(Locale.US, "%.5f", size))
            } else {
                put("quantity", String.format(Locale.US, "%.5f", size))
            }
            put("timestamp", timestamp)
            put("recvWindow", "6000")
        }

        val queryString = queryMap.map { "${it.key}=${it.value}" }.joinToString("&")
        val signature = hmacSha256Hex(queryString, secretKey)
        val url = "$baseUrl$path?$queryString&signature=$signature"

        val mediaType = "application/x-www-form-urlencoded".toMediaType()
        val requestBody = "".toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("X-MEXC-APIKEY", apiKey)
            .addHeader("Accept", "application/json")
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyText = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    throw Exception("HTTP Error ${response.code}: $bodyText")
                }

                val json = JSONObject(bodyText)
                val orderId = json.optString("orderId", "")
                if (orderId.isNotBlank()) {
                    return@withContext orderId
                }
                throw Exception("MEXC empty orderId response: $bodyText")
            }
        } catch (e: Exception) {
            Log.e(TAG, "placeMarketOrder error: ${e.message}", e)
            throw e
        }
    }
}
