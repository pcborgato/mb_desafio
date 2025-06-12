package com.example.desafio_mb.domain.usecase

import com.example.desafio_mb.data.model.Exchange
import com.example.desafio_mb.data.repository.ExchangeRepository
import com.example.desafio_mb.di.MockRepository
import com.example.desafio_mb.di.RealRepository
import javax.inject.Inject

class GetExchangesUseCaseImpl @Inject constructor(
   // @RealRepository private val repository: ExchangeRepository
    @MockRepository private val repository: ExchangeRepository
) : SelectExchangeUseCase {

    override suspend fun invoke(): Result<List<Exchange>> {
        return repository.getExchanges()
    }
}
