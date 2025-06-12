package com.example.desafio_mb.data.network

import com.example.desafio_mb.data.model.Exchange
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApiService {
    @GET("v1/exchanges")
    suspend fun getExchanges(
        @Query("apikey") apiKey: String
    ): Response<List<Exchange>>
}