package com.example.desafio_mb.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desafio_mb.data.model.Exchange
import com.example.desafio_mb.ui.viewmodel.ExchangeListViewModel
import com.example.desafio_mb.ui.viewmodel.ExchangeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeListScreen(
    viewModel: ExchangeListViewModel,
    onNavigateToDetail: (String) -> Unit // Callback para navegar, passando o exchange_id
) {
    val uiState by viewModel.uiState.observeAsState(initial = ExchangeUiState.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Exchanges")
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ExchangeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ExchangeUiState.Success -> {
                    if (state.exchanges.isEmpty()) {
                        Text(
                            text = "Nenhuma exchange encontrada.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        ExchangeList(
                            exchanges = state.exchanges,
                            onItemClick = { exchange ->
                                viewModel.selectExchange(exchange) // Armazena a exchange selecionada
                                onNavigateToDetail(exchange.exchangeId)
                            }
                        )
                    }
                }

                is ExchangeUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Erro: ${state.message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchExchanges() }) {
                            Text("Tentar Novamente")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExchangeList(
    exchanges: List<Exchange>,
    onItemClick: (Exchange) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(exchanges, key = { it.exchangeId }) { exchange ->
            ExchangeItem(exchange = exchange, onClick = { onItemClick(exchange) })
        }
    }
}

@Composable
fun ExchangeItem(
    exchange: Exchange,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = exchange.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: ${exchange.exchangeId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Volume (24h USD): \$${"%,.2f".format(exchange.volume1dayUsd)}", // Formatação básica
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}