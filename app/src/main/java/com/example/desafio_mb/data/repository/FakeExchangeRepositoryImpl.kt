package com.example.desafio_mb.data.repository

import com.example.desafio_mb.data.model.Exchange
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeExchangeRepositoryImpl @Inject constructor() : ExchangeRepository {

    private val mockExchanges = listOf(
        Exchange("FAKE_EX1", "Fake Exchange Alpha", 10000.0, "http://fakealpha.com", "2023-01-01"),
        Exchange("FAKE_EX2", "Fake Exchange Beta (Slow)", 25000.50, "http://fakebeta.com", "2022-05-10"),
        Exchange("FAKE_EX3", "Empty Exchange Gamma", 0.0, "http://fakegamma.com", "2024-01-01")
    )

    override suspend fun getExchanges(): Result<List<Exchange>> {
        delay(if (mockExchanges.any { it.name.contains("Slow") }) 2000L else 500L)

        val scenario = (1..4).random()

        return when (scenario) {
            1 -> Result.success(mockExchanges)
            2 -> Result.success(emptyList())
            3 -> Result.failure(Exception("Simulated Network Error: Could not connect")) // Erro
            else -> Result.success(mockExchanges.shuffled().take( (0..mockExchanges.size).random() )) // Sucesso com dados aleatórios
        }
    }

     override suspend fun getExchangeDetails(exchangeId: String): Result<Exchange> {
         delay(300)
         val found = mockExchanges.find { it.exchangeId == exchangeId }
         return if (found != null) Result.success(found) else Result.failure(Exception("Fake Exchange not found"))
     }
}
