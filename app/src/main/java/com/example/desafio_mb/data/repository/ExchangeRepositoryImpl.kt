package com.example.desafio_mb.data.repository

import com.example.desafio_mb.data.model.Exchange
import com.example.desafio_mb.data.network.CoinApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val apiService: CoinApiService
) : ExchangeRepository {
    private val response: List<Exchange> = listOf()
    override suspend fun getExchanges(): Result<List<Exchange>> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.getExchanges(apiKey = "63a5f21f-236e-42b1-a396-dcfdb37f5d86")
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Response body is null"))
                } else {
                    Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getExchangeDetails(exchangeId: String): Result<Exchange> {
        delay(300)
        val found = response.find { it.exchangeId == exchangeId }
        return if (found != null) Result.success(found) else Result.failure(Exception("Fake Exchange not found"))
    }
}