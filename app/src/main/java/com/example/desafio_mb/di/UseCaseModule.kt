package com.example.desafio_mb.di

import com.example.desafio_mb.domain.usecase.GetExchangesUseCaseImpl
import com.example.desafio_mb.domain.usecase.SelectExchangeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGetExchangesUseCase(
        getExchangesUseCaseImpl: GetExchangesUseCaseImpl
    ): SelectExchangeUseCase
}
