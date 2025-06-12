package com.example.desafio_mb.di

import com.example.desafio_mb.data.network.CoinApiService
import com.example.desafio_mb.data.network.RetrofitInstance
import com.example.desafio_mb.data.repository.ExchangeRepository
import com.example.desafio_mb.data.repository.ExchangeRepositoryImpl
import com.example.desafio_mb.data.repository.FakeExchangeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Scopes the bindings to the application lifecycle
object AppModule {

    @Provides
    @Singleton // Ensures a single instance of CoinApiService
    fun provideCoinApiService(): CoinApiService {
        return RetrofitInstance.api // Assuming RetrofitInstance.api creates and returns the service
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    @RealRepository
    abstract fun bindRealExchangeRepository(
        exchangeRepositoryImpl: ExchangeRepositoryImpl
    ): ExchangeRepository

    @Binds
    @Singleton
    @MockRepository
    abstract fun bindMockExchangeRepository(
        mockExchangeRepositoryImpl: FakeExchangeRepositoryImpl
    ): ExchangeRepository
}