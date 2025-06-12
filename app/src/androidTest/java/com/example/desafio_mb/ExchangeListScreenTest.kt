package com.example.desafio_mb.ui.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData // Mantenha se o seu ViewModel real ainda usa LiveData internamente
import com.example.desafio_mb.data.model.Exchange
import com.example.desafio_mb.di.RepositoryModule // Importe seu módulo de repositório
import com.example.desafio_mb.di.UseCaseModule // Importe seu módulo de UseCase
import com.example.desafio_mb.domain.usecase.SelectExchangeUseCase
import com.example.desafio_mb.ui.theme.Desafio_mbTheme
import com.example.desafio_mb.ui.viewmodel.ExchangeListViewModel
import com.example.desafio_mb.ui.viewmodel.ExchangeUiState
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.runBlocking // Para chamar suspend functions do mock

@UninstallModules(UseCaseModule::class, RepositoryModule::class) // Desinstala os módulos de produção
@HiltAndroidTest
class ExchangeListScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    // Use @BindValue para fornecer uma instância mock do UseCase.
    // Hilt irá injetar este mock no ExchangeListViewModel real.
    @BindValue
    @JvmField // Necessário para campos @BindValue em Kotlin
    val mockGetExchangesUseCase: SelectExchangeUseCase = mock()

    // O ViewModel será instanciado pelo Hilt com o mockGetExchangesUseCase injetado
    // Não precisamos mais zombar o ViewModel diretamente aqui se vamos obtê-lo do Hilt
    // ou se a tela o obtém via hiltViewModel().
    // No entanto, se ExchangeListScreen explicitamente PEGA um ViewModel como parâmetro,
    // e esse ViewModel é criado FORA do escopo do Hilt na tela, então o mock direto ainda pode ser necessário.
    // Assumindo que a tela ou sua Activity/Fragment host usará Hilt para obter o ViewModel:
    private lateinit var viewModel: ExchangeListViewModel

    private var onNavigateToDetailCalledWith: String? = null

    @Before
    fun setUp() {
        hiltRule.inject() // Injete as dependências ANTES de cada teste
        onNavigateToDetailCalledWith = null

        // Se você estiver testando um Composable que usa hiltViewModel(),
        // o ViewModel será fornecido pelo Hilt.
        // Se sua ExchangeListScreen PEGA um viewModel como parâmetro e você quer usar
        // um viewModel construído pelo Hilt (com o @BindValue mockGetExchangesUseCase),
        // você precisaria de uma maneira de obter essa instância do Hilt.
        // Por simplicidade, vamos assumir que o ExchangeListScreen usa hiltViewModel()
        // ou que podemos passar um ViewModel criado manualmente, mas com o mock UseCase.

        // Cenário 1: Se ExchangeListScreen usa hiltViewModel() internamente
        // A configuração do mockGetExchangesUseCase já é suficiente.

        // Cenário 2: Se ExchangeListScreen pega viewModel como parâmetro
        // E queremos usar um ViewModel com o mock injetado, mas construído manualmente (menos ideal com Hilt puro)
        // viewModel = ExchangeListViewModel(mockGetExchangesUseCase) // ESTA LINHA MUDA
        // O ViewModel real será obtido pelo Hilt
    }

    private fun launchExchangeListScreen() {
        // Se a sua tela usa `hiltViewModel()` para obter o ViewModel,
        // você não precisa passar o viewModel explicitamente aqui.
        // No entanto, o seu `ExchangeListScreen` atual tem um parâmetro `viewModel`.
        // Para que o @BindValue funcione, o ViewModel precisa ser gerenciado pelo Hilt.

        composeTestRule.setContent {
            // Opção A: ExchangeListScreen usa hiltViewModel() internamente
            // Desafio_mbTheme {
            //     ExchangeListScreen(onNavigateToDetail = { exchangeId ->
            //         onNavigateToDetailCalledWith = exchangeId
            //     })
            // }

            // Opção B: Passamos um ViewModel, mas este ViewModel é o REAL que o Hilt cria
            // e no qual o Hilt injetou o mockGetExchangesUseCase.
            // Para isso, precisaríamos obter o viewModel do Hilt aqui, o que é um pouco mais complexo
            // em testes de composeTestRule.setContent diretamente sem uma Activity.

            // ABORDAGEM MAIS SIMPLES PARA SUA ESTRUTURA ATUAL DE ExchangeListScreen:
            // Vamos criar o ViewModel manualmente, mas ele usará o mockGetExchangesUseCase
            // que está disponível para o Hilt. Isso não testa a injeção do Hilt no ViewModel DIRETAMENTE,
            // mas testa a lógica do ViewModel com o mock, e a tela com esse ViewModel.
            // Para testar a injeção do Hilt, você precisaria de um HiltTestActivity.
            // **Como estamos usando @BindValue, o Hilt DEVE criar o ViewModel.**
            // A forma mais fácil de fazer isso em um teste de Composable puro é se o Composable
            // usa `viewModel<ExchangeListViewModel>()` ou `hiltViewModel<ExchangeListViewModel>()`.

            // ASSUMINDO que ExchangeListScreen usa hiltViewModel() internamente:
            // (Você precisaria mudar ExchangeListScreen para não pegar viewModel como param,
            // ou usar um entry point como uma Activity anotada com @AndroidEntryPoint)

            // Por enquanto, vamos manter a estrutura original do seu teste e do ExchangeListScreen
            // mas isso significa que o `@BindValue` não terá efeito direto sobre um `mockViewModel` que você cria.
            // O `@BindValue` é para quando o HILT constrói o objeto (neste caso, o ViewModel).

            // ---- REVISÃO DA ABORDAGEM PARA SE ADEQUAR MELHOR AO HILT E TESTES DE UI ----
            // A melhor maneira é ter uma Activity/Fragment anotada com @AndroidEntryPoint
            // que hospeda seu Composable. O ViewModel seria então obtido via Hilt.
            // Se ExchangeListScreen é o seu conteúdo principal, você pode precisar de um HiltTestActivity.

            // SOLUÇÃO PRAGMÁTICA para manter o teste de UI focado no Composable,
            // e ainda usar mocks para dependências externas (UseCase).
            // Vamos instanciar o ViewModel REAL manualmente, passando o mockGetExchangesUseCase.
            // Isso testa a lógica do ViewModel e a interação da UI com ele.
            // O @BindValue não é estritamente necessário se você fizer isso, mas é bom para o futuro.
            viewModel = ExchangeListViewModel(mockGetExchangesUseCase) // Use o mock UseCase

            Desafio_mbTheme {
                ExchangeListScreen(
                    viewModel = viewModel, // Passe o ViewModel que usa o mock UseCase
                    onNavigateToDetail = { exchangeId ->
                        onNavigateToDetailCalledWith = exchangeId
                    }
                )
            }
        }
    }


    @Test
    fun exchangeListScreen_showsLoading_whenStateIsLoading() = runBlocking { // runBlocking para mocks suspend
        // Se o estado inicial do ViewModel (antes do UseCase retornar) é Loading
        // e o UseCase é chamado no init, você pode precisar de um mock que simule um delay
        // ou configurar o mock para retornar um Flow que emita Loading primeiro.
        // Para um teste mais simples, assumimos que o estado inicial é controlado pelo ViewModel.

        // Configure o mock para um cenário onde o carregamento pode ser observado
        // Se o ViewModel emite Loading antes de chamar o use case, este teste é válido.
        // Se o ViewModel só emite Loading quando fetchExchanges é chamado,
        // e fetchExchanges retorna imediatamente por causa do mock, você pode não ver o Loading.

        // Neste caso, o ViewModel que estamos criando manualmente irá para Loading e depois chamará o UseCase.
        // Se o UseCase responder muito rapidamente, a UI pode pular o estado de Loading visualmente.
        // Para este teste, vamos assumir que o estado de Loading é o estado inicial padrão do LiveData no ViewModel.

        // Forçar o ViewModel a estar em estado de Loading para o teste da UI
        (viewModel.uiState as MutableLiveData).postValue(ExchangeUiState.Loading)


        launchExchangeListScreen()


        composeTestRule.onNodeWithText("Exchanges").assertIsDisplayed() // Título
        // As asserções para "Nenhuma exchange..." e "Tentar Novamente" dependem
        // do que sua UI de Loading realmente mostra. Se ela mostra APENAS um indicador de progresso,
        // então elas podem não existir.
        // Se sua UI de Loading é apenas um indicador de progresso, e o título está sempre lá:
        composeTestRule.onNodeWithText("Nenhuma exchange encontrada.").assertDoesNotExist()
        composeTestRule.onNodeWithText("Tentar Novamente").assertDoesNotExist()
    }


    @Test
    fun exchangeListScreen_showsExchangeItems_whenStateIsSuccessWithData(): Unit = runBlocking {
        val exchanges = listOf(
            Exchange("BITFINEX", "Bitfinex", 12345.67, "https://bitfinex.com", "2010-01-01", null),
            Exchange("KRAKEN", "Kraken", 76543.21, "https://kraken.com", "2011-01-01", null)
        )
        // Quando o mockGetExchangesUseCase for chamado, ele retornará sucesso
        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.success(exchanges))

        // Se o ViewModel chama fetchExchanges no init, isso já terá acontecido ao criar o viewModel.
        // Se não, você precisaria chamar viewModel.fetchExchanges() aqui.
        // Assumindo que é chamado no init do ViewModel que estamos criando manualmente:
        viewModel = ExchangeListViewModel(mockGetExchangesUseCase) // Recria com o mock configurado
        // para este teste específico.

        launchExchangeListScreen() // Isso irá (re)compor com o novo estado do viewModel

        composeTestRule.onNodeWithText("Bitfinex").assertIsDisplayed()
        composeTestRule.onNodeWithText("ID: BITFINEX").assertIsDisplayed()
        // Adapte a formatação do volume se mudou
        composeTestRule.onNodeWithText("Volume (24h USD): \$12,345.67").assertIsDisplayed()

        composeTestRule.onNodeWithText("Kraken").assertIsDisplayed()
        composeTestRule.onNodeWithText("ID: KRAKEN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Volume (24h USD): \$76,543.21").assertIsDisplayed()
    }

    @Test
    fun exchangeListScreen_showsNoExchangesMessage_whenStateIsSuccessWithEmptyData(): Unit = runBlocking {
        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.success(emptyList()))
        viewModel = ExchangeListViewModel(mockGetExchangesUseCase)

        launchExchangeListScreen()

        composeTestRule.onNodeWithText("Nenhuma exchange encontrada.").assertIsDisplayed()
    }

    @Test
    fun exchangeListScreen_showsError_whenStateIsError(): Unit = runBlocking {
        val errorMessage = "Falha ao carregar exchanges"
        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.failure(Exception(errorMessage)))
        viewModel = ExchangeListViewModel(mockGetExchangesUseCase)

        launchExchangeListScreen()

        composeTestRule.onNodeWithText("Erro: $errorMessage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tentar Novamente").assertIsDisplayed()
    }

    @Test
    fun clickingRetryButton_callsFetchExchanges_whenStateIsError(): Unit = runBlocking {
        val errorMessage = "Falha ao carregar"
        whenever(mockGetExchangesUseCase.invoke())
            .thenReturn(Result.failure(Exception(errorMessage))) // Primeira chamada falha
            .thenReturn(Result.success(emptyList())) // Segunda chamada (após retry) tem sucesso

        viewModel = ExchangeListViewModel(mockGetExchangesUseCase)
        launchExchangeListScreen() // UI mostra o erro

        composeTestRule.onNodeWithText("Tentar Novamente").performClick()

        // Verifique se o UseCase foi chamado novamente (total de duas vezes)
        // A primeira vez no init do ViewModel (ou chamada explícita), a segunda no retry.
        verify(mockGetExchangesUseCase).invoke() // O Mockito por padrão verifica pelo menos uma vez.
        // Para verificar N vezes: verify(mock, times(N)).invoke()
        // Neste caso, fetchExchanges é chamado no init, e depois no retry.
        // Mas como o mock é o mesmo objeto, e o ViewModel é recriado,
        // precisamos ser cuidadosos.

        // Com a recriação do viewModel = ExchangeListViewModel(mockGetExchangesUseCase) em cada teste
        // ou antes do launch, a verificação mais simples é que invoke() foi chamado
        // pela lógica de fetchExchanges() que é acionada pelo clique.
        // A verificação de que o ViewModel chamou o UseCase já é um teste de unidade do ViewModel.
        // Aqui, estamos mais interessados no comportamento da UI.
        // Se a segunda chamada ao use case (após retry) resultar em sucesso com dados vazios,
        // a UI deve atualizar para "Nenhuma exchange encontrada."
        composeTestRule.onNodeWithText("Nenhuma exchange encontrada.").assertIsDisplayed()
    }

    @Test
    fun clickingExchangeItem_callsSelectExchangeAndNavigates_whenStateIsSuccess() = runBlocking {
        val exchangeToClick = Exchange("COINBASE", "Coinbase", 9876.54, "https://coinbase.com", "2012-01-01", null)
        val exchanges = listOf(
            exchangeToClick,
            Exchange("BINANCE", "Binance", 11223.34, "https://binance.com", "2017-01-01", null)
        )
        whenever(mockGetExchangesUseCase.invoke()).thenReturn(Result.success(exchanges))
        viewModel = ExchangeListViewModel(mockGetExchangesUseCase) // Use o mock UseCase
        // Mock da função selectExchange no ViewModel real (se necessário, mas geralmente testamos o efeito)
        // Não precisamos zombar do ViewModel se estamos usando o real com um UseCase mockado.

        launchExchangeListScreen()

        composeTestRule.onNodeWithText("Coinbase").performClick()

        // Verifique se a navegação foi chamada com o ID correto
        // Isso é feito através da callback onNavigateToDetail
        assert(onNavigateToDetailCalledWith == "COINBASE")

        // Verifique também se o ViewModel teve seu método selectExchange chamado.
        // Isso é mais um teste de unidade do ViewModel, mas pode ser verificado aqui se crucial para a UI.
        // Se viewModel.selectExchange() atualiza um LiveData que a UI observa,
        // então a mudança na UI seria o foco principal.
        // Como `selectedExchange` é um LiveData no ViewModel, podemos observar seu valor.
        assert(viewModel.selectedExchange.value == exchangeToClick)
    }
}