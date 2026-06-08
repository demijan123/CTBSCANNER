package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProtectionEvent(
    val id: String,
    val timestamp: Long,
    val dateTimeStr: String,
    val moduleTriggered: String,
    val marketConditions: String,
    val durationOfPause: String,
    val tradesPrevented: Int,
    val engineStatus: String = "Paused"
)
