package com.example.desafio_mb.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.desafio_mb.data.model.Exchange
import com.example.desafio_mb.ui.viewmodel.ExchangeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailScreen(
    exchangeId: String,
    viewModel: ExchangeListViewModel,
    onNavigateUp: () -> Unit
) {
    val selectedExchange by viewModel.selectedExchange.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedExchange?.name ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            selectedExchange?.let { exchange ->
                ExchangeDetailContent(exchange = exchange)
            } ?: run {
                Text(
                    "Detalhes não disponíveis. Tente voltar e selecionar novamente.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ExchangeDetailContent(exchange: Exchange) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        DetailItem("Nome:", exchange.name)
        DetailItem("ID da Exchange:", exchange.exchangeId)
        DetailItem("Volume (1 dia USD):", "\$${"%,.2f".format(exchange.volume1dayUsd)}")
        exchange.website?.let { DetailItem("Website:", it) }
        exchange.dataStart?.let { DetailItem("Data de Início:", it) }
        exchange.dataEnd?.let { DetailItem("Data de Fim (se aplicável):", it) }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
