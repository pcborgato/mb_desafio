package com.example.desafio_mb.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockRepository