package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String?,
    @Json(name = "current_price") val currentPrice: Double,
    @Json(name = "market_cap") val marketCap: Double,
    @Json(name = "market_cap_rank") val marketCapRank: Int?,
    @Json(name = "total_volume") val totalVolume: Double?,
    @Json(name = "high_24h") val high24h: Double?,
    @Json(name = "low_24h") val low24h: Double?,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @Json(name = "total_supply") val totalSupply: Double? = null,
    @Json(name = "circulating_supply") val circulatingSupply: Double? = null,
    @Json(name = "max_supply") val maxSupply: Double? = null
)
