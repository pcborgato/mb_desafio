package com.example.desafio_mb.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.desafio_mb.ui.view.ExchangeDetailScreen
import com.example.desafio_mb.ui.view.ExchangeListScreen
import com.example.desafio_mb.ui.viewmodel.ExchangeListViewModel

object AppDestinations {
    const val EXCHANGE_LIST = "exchange_list"
    const val EXCHANGE_DETAIL = "exchange_detail"
    const val EXCHANGE_ID_ARG = "exchangeId"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    exchangeListViewModel: ExchangeListViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.EXCHANGE_LIST
    ) {
        composable(AppDestinations.EXCHANGE_LIST) {
            ExchangeListScreen(
                viewModel = exchangeListViewModel,
                onNavigateToDetail = { exchangeId ->
                    navController.navigate("${AppDestinations.EXCHANGE_DETAIL}/$exchangeId")
                }
            )
        }
        composable(
            route = "${AppDestinations.EXCHANGE_DETAIL}/{${AppDestinations.EXCHANGE_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.EXCHANGE_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val exchangeId = backStackEntry.arguments?.getString(AppDestinations.EXCHANGE_ID_ARG)
            requireNotNull(exchangeId) { "exchangeId não pode ser nulo" }

            ExchangeDetailScreen(
                exchangeId = exchangeId,
                viewModel = exchangeListViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}