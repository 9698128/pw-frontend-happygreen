package com.happygreen.frontend.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.happygreen.frontend.ui.screens.auth.AuthViewModel
import com.happygreen.frontend.ui.screens.auth.LoginScreen
import com.happygreen.frontend.ui.screens.auth.RegisterScreen
import com.happygreen.frontend.ui.screens.home.HomeScreen

// Definizione delle rotte
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun HappyGreenNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val uiState by authViewModel.uiState.collectAsState()

    // Determina la schermata iniziale in base allo stato di login
    val startDestination = if (uiState.isLoggedIn) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Schermata Login
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        // Rimuove login dallo stack così non si può tornare indietro
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Schermata Registrazione
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        // Rimuove tutto lo stack di auth
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Schermata Home
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        // Rimuove home dallo stack
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}