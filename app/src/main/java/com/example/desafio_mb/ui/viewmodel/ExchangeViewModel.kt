package com.example.desafio_mb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio_mb.data.model.Exchange
import com.example.desafio_mb.domain.usecase.SelectExchangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ExchangeUiState {
    data object Loading : ExchangeUiState()
    data class Success(val exchanges: List<Exchange>) : ExchangeUiState()
    data class Error(val message: String) : ExchangeUiState()
}

@HiltViewModel
class ExchangeListViewModel @Inject constructor(
    private val getExchangesUseCase: SelectExchangeUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<ExchangeUiState>(ExchangeUiState.Loading)
    val uiState: LiveData<ExchangeUiState> = _uiState

    private val _selectedExchange = MutableLiveData<Exchange?>()
    val selectedExchange: LiveData<Exchange?> = _selectedExchange

    init {
        fetchExchanges()
    }

    fun fetchExchanges() {
        _uiState.postValue(ExchangeUiState.Loading) // MUDANÇA AQUI

        viewModelScope.launch { // Este launch pode estar usando Dispatchers.Default ou IO por padrão
            try {
                // Chame o UseCase como uma função
                val result = getExchangesUseCase()
                result.fold(
                    onSuccess = { exchanges ->
                        // _uiState.value = ExchangeUiState.Success(exchanges) // ERRO se não estiver no Main
                        _uiState.postValue(ExchangeUiState.Success(exchanges)) // MUDANÇA AQUI
                    },
                    onFailure = { error ->
                        // _uiState.value = ExchangeUiState.Error(error.message ?: "Unknown error") // ERRO se não estiver no Main
                        _uiState.postValue(ExchangeUiState.Error(error.message ?: "Unknown error")) // MUDANÇA AQUI
                    }
                )
            } catch (e: Exception) {
                _uiState.postValue(ExchangeUiState.Error(e.message ?: "Erro desconhecido ao buscar")) // MUDANÇA AQUI
            }
        }
    }

    fun selectExchange(exchange: Exchange) {
        _selectedExchange.value = exchange
    }

    fun clearSelectedExchange() {
        _selectedExchange.value = null
    }
}
