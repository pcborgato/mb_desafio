package com.example.desafio_mb.domain.usecase

import com.example.desafio_mb.data.model.Exchange

interface SelectExchangeUseCase {
     suspend operator fun invoke(): Result<List<Exchange>>
 }
