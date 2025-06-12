package com.example.desafio_mb.data.model

import com.google.gson.annotations.SerializedName

data class Exchange(
    @SerializedName("exchange_id")
    val exchangeId: String,
    val name: String,
    @SerializedName("volume_1day_usd")
    val volume1dayUsd: Double,
    @SerializedName("website")
    val website: String? = null,
    @SerializedName("data_start")
    val dataStart: String? = null,
    @SerializedName("data_end")
    val dataEnd: String? = null
)
