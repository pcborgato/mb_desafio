package com.example.desafio_mb.data.repository

import com.example.desafio_mb.data.model.Exchange

interface ExchangeRepository {
    suspend fun getExchanges(): Result<List<Exchange>>
    suspend fun getExchangeDetails(exchangeId: String): Result<Exchange>
}

