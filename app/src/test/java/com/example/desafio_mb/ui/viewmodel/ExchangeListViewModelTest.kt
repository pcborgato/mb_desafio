package com.example.desafio_mb.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.desafio_mb.data.model.Exchange

import com.example.desafio_mb.domain.usecase.SelectExchangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ExchangeListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Para LiveData

    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler()) // Ou StandardTestDispatcher

    private lateinit var viewModel: ExchangeListViewModel
    private lateinit var mockGetExchangesUseCase: SelectExchangeUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockGetExchangesUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchExchanges com sucesso atualiza uiState para Success`() = runTest(testDispatcher) {
        val fakeExchanges = listOf(
            Exchange(exchangeId = "BITSTAMP", name = "Bitstamp", volume1dayUsd = 12345.67),
            Exchange(exchangeId = "COINBASE", name = "Coinbase Pro", volume1dayUsd = 78901.23)
        )
        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.success(fakeExchanges))

        viewModel = ExchangeListViewModel(mockGetExchangesUseCase)

        val uiState = viewModel.uiState.value
        assertTrue("O estado da UI deveria ser Success, mas foi $uiState", uiState is ExchangeUiState.Success)
        assertEquals(fakeExchanges, (uiState as ExchangeUiState.Success).exchanges)
    }

    @Test
    fun `fetchExchanges com falha atualiza uiState para Error`() = runTest(testDispatcher) {
        val errorMessage = "Network error"

        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.failure(Exception(errorMessage)))

        viewModel = ExchangeListViewModel(mockGetExchangesUseCase)

        val uiState = viewModel.uiState.value
        assertTrue("O estado da UI deveria ser Error, mas foi $uiState", uiState is ExchangeUiState.Error)
        assertEquals(errorMessage, (uiState as ExchangeUiState.Error).message)
    }

    @Test
    fun `estado inicial eh Loading entao Success após fetch (se fetch é no init)`() = runTest(testDispatcher) {
        val fakeExchanges = listOf(Exchange(exchangeId = "ID1", name = "Name1", volume1dayUsd = 100.0))
        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.success(fakeExchanges))

        viewModel = ExchangeListViewModel(mockGetExchangesUseCase)

        val uiState = viewModel.uiState.value
        assertTrue("O estado da UI deveria ser Success, mas foi $uiState", uiState is ExchangeUiState.Success)
        assertEquals(fakeExchanges, (uiState as ExchangeUiState.Success).exchanges)

    }
}