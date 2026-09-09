package com.edtech.ranks.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edtech.ranks.ui.auth.AuthScreen
import com.edtech.ranks.ui.camera.CameraScreen
import com.edtech.ranks.ui.home.HomeScreen
import com.edtech.ranks.ui.practice.PracticeScreen
import com.edtech.ranks.ui.profile.ProfileScreen
import com.edtech.ranks.ui.vault.VaultScreen
import com.edtech.ranks.ui.central.CentralScreen
import com.edtech.ranks.ui.leaderboard.LeaderboardScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Profile : Screen("profile")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Practice : Screen("practice")
    object Vault : Screen("vault")
    object Central : Screen("central")
    object Leaderboard : Screen("leaderboard")
}

@Composable
fun AppNavigation(
    isAuthenticated: Boolean,
    onAuthSuccess: () -> Unit
) {
    val navController = rememberNavController()
    val startDestination = if (isAuthenticated) Screen.Home.route else Screen.Auth.route
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Screens where we show the bottom nav
    val showBottomNav = currentRoute in listOf(
        Screen.Home.route,
        Screen.Vault.route,
        Screen.Central.route,
        Screen.Leaderboard.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                AppBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showBottomNav) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Camera.route) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.DocumentScanner, contentDescription = "Scan Question")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(
                    serverClientId = "847821044733-cvalj83derag50vvlt37ekjkg9o2k0v0.apps.googleusercontent.com",
                    onAuthSuccess = {
                        onAuthSuccess()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSignOut = {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onAddQuestionClick = { navController.navigate(Screen.Camera.route) },
                    onCustomTestClick = { navController.navigate(Screen.Practice.route) },
                    onVaultClick = { navController.navigate(Screen.Vault.route) },
                    onCentralClick = { navController.navigate(Screen.Central.route) },
                    onLeaderboardClick = { navController.navigate(Screen.Leaderboard.route) }
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
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun AppBottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = { onNavigate(Screen.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Vault") },
            label = { Text("Vault") },
            selected = currentRoute == Screen.Vault.route,
            onClick = { onNavigate(Screen.Vault.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = "Central") },
            label = { Text("History") },
            selected = currentRoute == Screen.Central.route,
            onClick = { onNavigate(Screen.Central.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Rank") },
            label = { Text("Rank") },
            selected = currentRoute == Screen.Leaderboard.route,
            onClick = { onNavigate(Screen.Leaderboard.route) }
        )
    }
}
