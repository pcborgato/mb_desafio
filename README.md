# Desafio MB - Corretoras de Criptomoedas

Este é um aplicativo Android de exemplo que exibe uma lista de corretoras de criptomoedas (Exchanges) obtidas através de uma API. O projeto demonstra o uso de tecnologias modernas de desenvolvimento Android, incluindo Jetpack Compose, ViewModel, Coroutines, Retrofit, Hilt e uma arquitetura baseada em Use Cases (Casos de Uso).

## Funcionalidades

*   Exibe uma lista de corretoras de criptomoedas.
*   Mostra detalhes de cada corretora, como nome, ID e volume de negociação nas últimas 24 horas (USD).
*   Permite visualizar detalhes de uma corretora selecionada (funcionalidade de navegação pendente ou a ser especificada).
*   Tratamento de estados de carregamento (loading), sucesso e erro.
*   Botão para tentar novamente em caso de falha na busca de dados.

## Arquitetura e Tecnologias Utilizadas

O projeto segue uma arquitetura inspirada em Clean Architecture, separando as responsabilidades em diferentes camadas:

*   **UI (Interface do Usuário):** Construída com Jetpack Compose.
    *   `ExchangeListScreen`: Composable principal que exibe a lista de corretoras e interage com o ViewModel.
    *   `ExchangeListViewModel`: Gerencia o estado da UI e delega a lógica de negócios aos Use Cases.
*   **Domain (Domínio):** Contém a lógica de negócios central e as definições dos Use Cases.
    *   `GetExchangesUseCase`: Caso de Uso responsável por obter a lista de corretoras. Encapsula a lógica de interação com o repositório.
*   **Data (Dados):** Responsável por fornecer os dados para o aplicativo, seja de uma API remota ou de um cache local (não implementado neste exemplo).
    *   `ExchangeRepository`: Interface que define o contrato para obtenção dos dados das corretoras.
    *   `ExchangeRepositoryImpl`: Implementação concreta do repositório, utilizando Retrofit para chamadas de API.
    *   `CoinApiService`: Interface Retrofit para definir os endpoints da API.
    *   `Exchange`: Modelo de dados (POKO/data class) representando uma corretora.

**Principais Tecnologias e Bibliotecas:**

*   **Kotlin:** Linguagem de programação principal.
*   **Jetpack Compose:** Toolkit moderno para construção de UIs nativas do Android.
*   **ViewModel (Jetpack):** Para gerenciar dados relacionados à UI de forma consciente ao ciclo de vida.
*   **LiveData (Jetpack):** Para observar mudanças de dados (embora o projeto possa evoluir para usar StateFlow).
*   **Coroutines:** Para gerenciamento de concorrência e operações assíncronas.
*   **Retrofit:** Para realizar chamadas de rede HTTP de forma type-safe.
*   **Hilt (Dagger):** Para injeção de dependência, facilitando o gerenciamento de dependências e a testabilidade.
*   **Mockito-Kotlin:** Para mocking em testes unitários e de UI.
*   **JUnit 4 & AndroidX Test:** Para testes unitários e de instrumentação.

## Injeção de Dependência com Hilt

Este projeto utiliza Hilt para gerenciar a injeção de dependência.

*   `MbApplication.kt`: Classe Application anotada com `@HiltAndroidApp` para habilitar a geração de código do Hilt.
*   **Módulos Hilt:**
    *   `AppModule.kt` (ou similar): Pode conter bindings para dependências de nível de aplicativo, como instâncias do Retrofit, OkHttpClient, etc.
    *   `RepositoryModule.kt`: Fornece a implementação concreta do `ExchangeRepository`.
    *   `UseCaseModule.kt`: Fornece a implementação concreta do `GetExchangesUseCase` (e outros Use Cases, se houver).
*   **Injeção em Componentes Android:**
    *   ViewModels (como `ExchangeListViewModel`) são anotados com `@HiltViewModel` e suas dependências (como `GetExchangesUseCase`) são injetadas no construtor com `@Inject`.

## Use Cases (Casos de Uso)

A camada de domínio introduz Use Cases para encapsular a lógica de negócios específica.

*   `GetExchangesUseCase`:
    *   Interface: `domain.usecase.GetExchangesUseCase`
    *   Implementação: `domain.usecase.GetExchangesUseCaseImpl`
    *   Responsabilidade: Orquestrar a busca da lista de corretoras, interagindo com o `ExchangeRepository`.
    *   Benefícios: Separação de responsabilidades, melhor testabilidade e clareza da lógica de negócios.

## Testes

O projeto inclui:

*   **Testes Unitários:** Para ViewModels e Use Cases, localizados em `src/test`.
    *   `ExchangeListViewModelTest.kt`: Testa a lógica do ViewModel, zombando do `GetExchangesUseCase`.
*   **Testes de UI (Instrumentação):** Para a tela `ExchangeListScreen`, localizados em `src/androidTest`.
    *   `ExchangeListScreenTest.kt`: Testa as interações da UI e a exibição de diferentes estados, usando Hilt para fornecer um `GetExchangesUseCase` mockado através de `@BindValue`.

Para executar os testes:

*   No Android Studio, clique com o botão direito na pasta `test` ou `androidTest` (ou em um arquivo de teste específico) e selecione "Run tests".
*   Ou use os comandos Gradle:

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

## Estrutura de Pacotes (Sugestão)
```
com.example.desafio_mb
├── data
│   ├── model
│   │   └── Exchange
│   ├── network
│   │   ├── CoinApiService
│   │   └── RetrofitInstance
│   └── repository
│       ├── ExchangeRepository
│       ├── ExchangeRepositoryImpl
│       └── FakeExchangeRepositoryImpl
├── domain
│   ├── model 
│   └── usecase
│       ├── SelectExchangeUseCase.kt (interface)
│       └── GetExchangesUseCaseImpl.kt (implementação)
├── ui
│   ├── view
│   │   ├── MainActivity
│   │   ├── ExchangeDetailScreen
│   |   └── ExchangeListScreen
│   └── viewmodel
│       └── ExchangeListViewModel
├── navigation
│   └── AppNavigation
└── di
    ├── AppModule
    ├── Qualifiers
    └── UseCaseModule
