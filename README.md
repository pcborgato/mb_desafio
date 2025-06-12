# Desafio MB - App de Cotação de Exchanges

Este projeto é uma aplicação Android desenvolvida como parte de um desafio técnico. O objetivo principal do aplicativo é consumir a API da [CoinAPI.io](https://www.coinapi.io/) para buscar e exibir uma lista de exchanges de criptomoedas, permitindo ao usuário visualizar detalhes de cada uma.

## Funcionalidades

*   **Listagem de Exchanges:** Exibe uma lista de exchanges de criptomoedas obtidas da API, mostrando informações como nome, ID e volume de negociação (se disponível na listagem).
*   **Detalhes da Exchange:** Ao selecionar uma exchange da lista, o usuário é direcionado para uma tela de detalhes que mostra informações mais completas sobre a exchange (como site, data de início, etc. ).
*   **Tratamento de Estados:** A interface do usuário reflete os estados de carregamento, sucesso e erro durante as chamadas de API.
*   **Interface Reativa:** Construído utilizando Jetpack Compose para uma UI moderna e declarativa.
*   **Navegação:** Utiliza o Navigation Compose para gerenciar a navegação entre as telas.

## Arquitetura e Tecnologias Utilizadas

O projeto adota uma arquitetura inspirada no MVVM (Model-View-ViewModel) com elementos que visam uma boa separação de responsabilidades.

*   **Linguagem:** Kotlin
*   **UI Toolkit:** Jetpack Compose
*   **Gerenciamento de Estado da UI:** ViewModel com `LiveData` e `ExchangeUiState` para representar os diferentes estados da tela.
*   **Navegação:** Navigation Compose
*   **Networking:**
    *   Retrofit: Para realizar chamadas HTTP à API da CoinAPI.
    *   Gson: Para serialização/desserialização de JSON.
    *   OkHttp Logging Interceptor: Para visualização dos logs de chamadas de rede durante o desenvolvimento.
*   **Coroutines:** Para gerenciamento de operações assíncronas (chamadas de API).
*   **Testes:**
    *   JUnit 4: Para testes unitários.
    *   Mockito (ou MockK): Para mocking de dependências em testes.
    *   Testes de UI com Compose (`createComposeRule`).
    *   `kotlinx-coroutines-test`: Para testar coroutines.

### Estrutura de Camadas (Simplificada)

1.  **UI (Apresentação):**
    *   Composable Screens (`ExchangeListScreen.kt`, `ExchangeDetailScreen.kt`)
    *   ViewModel (`ExchangeListViewModel.kt`) - Prepara e gerencia os dados para a UI.
2.  **Data:**
    *   Repository (`ExchangeRepository.kt`, `ExchangeRepositoryImpl.kt`) - Abstrai a fonte de dados e gerencia a lógica de obtenção e cache (se houver).
    *   Network (`CoinApiService.kt`, `RetrofitInstance.kt`) - Responsável pela comunicação com a API.
    *   Models (`Exchange.kt`) - Representação dos dados da API.

## Configuração do Projeto

### Pré-requisitos

*   Android Studio (versão mais recente recomendada)
*   JDK 11 ou superior
*   Conta na [CoinAPI.io](https://www.coinapi.io/) para obter uma chave de API.

## Observação

Como não houve permissão para acessar o serviço, optei por escrever um mock

{
"title": "Forbidden",
"status": 403,
"detail": "Quota exceeded: Insufficient Usage Credits or Subscription.",
"error": "Forbidden (Quota exceeded: Insufficient Usage Credits or Subscription.)",
"QuotaKey": "BA",
"QuotaName": "Insufficient Usage Credits or Subscription",
"QuotaType": "Organization Limit",
"QuotaValueCurrentUsage": 0,
"QuotaValue": 0,
"QuotaValueUnit": "$",
"QuotaValueAdjustable": "Yes, acquire or upgrade subscription, add service credits manually or setup auto-recharge."
}

## Usando o mockado

Use `@RealRepository` para acessar o serviço via Retrofit ou use `@MockRepository` para usar dados mockados

```kotlin
class GetExchangesUseCaseImpl @Inject constructor(
   // @RealRepository private val repository: ExchangeRepository
    @MockRepository private val repository: ExchangeRepository
) : SelectExchangeUseCase {

    override suspend fun invoke(): Result<List<Exchange>> {
        return repository.getExchanges()
    }
}
