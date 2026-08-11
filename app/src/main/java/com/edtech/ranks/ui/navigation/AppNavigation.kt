package com.edtech.ranks.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edtech.ranks.ui.auth.AuthScreen
import com.edtech.ranks.ui.camera.CameraScreen
import com.edtech.ranks.ui.home.HomeScreen
import com.edtech.ranks.ui.practice.PracticeScreen
import com.edtech.ranks.ui.profile.ProfileSetupScreen
import com.edtech.ranks.ui.vault.VaultScreen
import com.edtech.ranks.ui.central.CentralScreen

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Practice : Screen("practice")
    object Vault : Screen("vault")
    object Central : Screen("central")
}

@Composable
fun AppNavigation(
    isAuthenticated: Boolean,
    onAuthSuccess: () -> Unit
) {
    val navController = rememberNavController()
    val startDestination = if (isAuthenticated) Screen.ProfileSetup.route else Screen.Auth.route

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(
                    serverClientId = "847821044733-cvalj83derag50vvlt37ekjkg9o2k0v0.apps.googleusercontent.com",
                    onAuthSuccess = {
                        onAuthSuccess()
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.ProfileSetup.route) {
                ProfileSetupScreen(
                    onSetupComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onAddQuestionClick = { navController.navigate(Screen.Camera.route) },
                    onCustomTestClick = { navController.navigate(Screen.Practice.route) },
                    onVaultClick = { navController.navigate(Screen.Vault.route) },
                    onCentralClick = { navController.navigate(Screen.Central.route) }
                )
            }
            composable(Screen.Camera.route) {
                CameraScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Practice.route) {
                PracticeScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Vault.route) {
                VaultScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Central.route) {
                CentralScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
