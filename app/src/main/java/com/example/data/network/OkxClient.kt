package com.example.data.network

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object OkxClient {
    private const val TAG = "OkxClient"
    private const val BASE_URL = "https://www.okx.com"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private fun getUtcTimestamp(): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        df.timeZone = TimeZone.getTimeZone("UTC")
        return df.format(Date())
    }

    private fun hmacSha256(data: String, secretKey: String): String {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val hmacBytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hmacBytes, Base64.NO_WRAP)
    }

    private fun buildRequest(
        method: String,
        path: String,
        bodyString: String?,
        apiKey: String,
        secretKey: String,
        passphrase: String,
        isDemo: Boolean
    ): Request {
        val timestamp = getUtcTimestamp()
        val signaturePayload = timestamp + method.uppercase() + path + (bodyString ?: "")
        val signature = hmacSha256(signaturePayload, secretKey)

        val builder = Request.Builder()
            .url(BASE_URL + path)
            .addHeader("OK-ACCESS-KEY", apiKey)
            .addHeader("OK-ACCESS-SIGN", signature)
            .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
            .addHeader("OK-ACCESS-PASSPHRASE", passphrase)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")

        if (isDemo) {
            builder.addHeader("x-simulated-id", "1")
            builder.addHeader("OK-ACCESS-SIMULATED", "1")
        }

        if (method.uppercase() == "POST" && bodyString != null) {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            builder.post(bodyString.toRequestBody(mediaType))
        } else {
            builder.get()
        }

        return builder.build()
    }

    /**
     * Validates the credentials by fetching the available USDT balance.
     * Returns the USDT available balance if successful.
     */
    suspend fun validateAndFetchBalance(
        apiKey: String,
        secretKey: String,
        passphrase: String,
        isDemo: Boolean
    ): Double = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || secretKey.isBlank() || passphrase.isBlank()) {
            throw IllegalArgumentException("API Key, Secret, and Passphrase must not be empty.")
        }

        val path = "/api/v5/account/balance?ccy=USDT"
        val request = buildRequest("GET", path, null, apiKey, secretKey, passphrase, isDemo)

        try {
            client.newCall(request).execute().use { response ->
                val bodyText = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    throw Exception("HTTP Error ${response.code}: $bodyText")
                }

                val json = JSONObject(bodyText)
                val code = json.optString("code", "-1")
                val msg = json.optString("msg", "")

                if (code != "0") {
                    throw Exception("OKX Error: $msg (code: $code)")
                }

                val dataArray = json.optJSONArray("data")
                if (dataArray != null && dataArray.length() > 0) {
                    val firstObj = dataArray.getJSONObject(0)
                    val detailsArray = firstObj.optJSONArray("details")
                    if (detailsArray != null) {
                        for (i in 0 until detailsArray.length()) {
                            val detail = detailsArray.getJSONObject(i)
                            if (detail.optString("ccy").uppercase() == "USDT") {
                                val avail = detail.optDouble("availBal", 0.0)
                                return@withContext avail
                            }
                        }
                    }
                    // If USDT detail not found, but data exists, check general availBal
                    val availEq = firstObj.optDouble("availEq", 0.0)
                    return@withContext availEq
                }
                return@withContext 0.0
            }
        } catch (e: Exception) {
            Log.e(TAG, "validateAndFetchBalance failed", e)
            throw e
        }
    }

    /**
     * Places a market order on OKX.
     * Returns the order ID (ordId) if successful.
     */
    suspend fun placeMarketOrder(
        apiKey: String,
        secretKey: String,
        passphrase: String,
        isDemo: Boolean,
        symbol: String,  // e.g. "BTC" or "ETH"
        isBuy: Boolean,
        size: Double     // For buy: USDT amount to spend. For sell: Coin quantity to sell.
    ): String = withContext(Dispatchers.IO) {
        val path = "/api/v5/trade/order"
        val instId = "${symbol.uppercase()}-USDT"

        val bodyJson = JSONObject().apply {
            put("instId", instId)
            put("tdMode", "cash")
            put("side", if (isBuy) "buy" else "sell")
            put("ordType", "market")
            put("sz", String.format(Locale.US, "%.5f", size))
            if (isBuy) {
                // Use quote_ccy so size is interpreted as USDT size.
                put("tgtCcy", "quote_ccy")
            }
        }

        val request = buildRequest("POST", path, bodyJson.toString(), apiKey, secretKey, passphrase, isDemo)

        try {
            client.newCall(request).execute().use { response ->
                val bodyText = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    throw Exception("HTTP Error ${response.code}: $bodyText")
                }

                val json = JSONObject(bodyText)
                val code = json.optString("code", "-1")
                val msg = json.optString("msg", "")

                if (code != "0") {
                    throw Exception("OKX API Order Failed: $msg (code: $code)")
                }

                val dataArray = json.optJSONArray("data")
                if (dataArray != null && dataArray.length() > 0) {
                    val orderResult = dataArray.getJSONObject(0)
                    val sCode = orderResult.optString("sCode", "0")
                    val sMsg = orderResult.optString("sMsg", "")
                    if (sCode != "0" && sCode.isNotBlank()) {
                        throw Exception("OKX Order Error: $sMsg (code: $sCode)")
                    }
                    return@withContext orderResult.optString("ordId", "")
                }
                throw Exception("OKX empty response data")
            }
        } catch (e: Exception) {
            Log.e(TAG, "placeMarketOrder error: ${e.message}", e)
            throw e
        }
    }
}
