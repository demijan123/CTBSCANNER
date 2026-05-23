package com.example.data.network

import com.example.data.model.Coin
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("api/v3/coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 250,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<Coin>
}
