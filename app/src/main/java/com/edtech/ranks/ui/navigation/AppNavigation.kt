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
import com.edtech.ranks.ui.leaderboard.LeaderboardScreen

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Practice : Screen("practice")
    object Vault : Screen("vault")
    object Central : Screen("central")
    object Leaderboard : Screen("leaderboard")
}

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.edtech.ranks.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    isAuthenticated: Boolean,
    onAuthSuccess: () -> Unit
) {
    val navController = rememberNavController()
    val startDestination = if (isAuthenticated) Screen.ProfileSetup.route else Screen.Auth.route
    
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
        topBar = {
            if (showBottomNav) {
                StellarTopAppBar()
            }
        },
        bottomBar = {
            if (showBottomNav) {
                CosmicBottomNav(
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
                    containerColor = Color.Transparent,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    modifier = Modifier
                        .offset(y = 24.dp) // Hover above the nav bar
                        .size(64.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.topRightToBottomLeft(listOf(tertiary, tertiaryContainer)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Capture", tint = onTertiaryContainer, modifier = Modifier.size(32.dp))
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            // ... (keep auth and profile) ...
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
fun StellarTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GlassBackground)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Send, contentDescription = "Logo", tint = primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "RANKS",
                style = MaterialTheme.typography.displayMedium,
                color = primary
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(surfaceVariant)
        )
    }
}

@Composable
fun CosmicBottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = GlassBackground,
        tonalElevation = 0.dp,
        modifier = Modifier.background(GlassBackground)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = { onNavigate(Screen.Home.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primary,
                selectedTextColor = primary,
                indicatorColor = primary.copy(alpha = 0.2f),
                unselectedIconColor = onSurfaceVariant,
                unselectedTextColor = onSurfaceVariant
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Vault") },
            label = { Text("Vault") },
            selected = currentRoute == Screen.Vault.route,
            onClick = { onNavigate(Screen.Vault.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primary,
                selectedTextColor = primary,
                indicatorColor = primary.copy(alpha = 0.2f),
                unselectedIconColor = onSurfaceVariant,
                unselectedTextColor = onSurfaceVariant
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Central") },
            label = { Text("Central") },
            selected = currentRoute == Screen.Central.route,
            onClick = { onNavigate(Screen.Central.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primary,
                selectedTextColor = primary,
                indicatorColor = primary.copy(alpha = 0.2f),
                unselectedIconColor = onSurfaceVariant,
                unselectedTextColor = onSurfaceVariant
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Leaderboard") },
            label = { Text("Rank") },
            selected = currentRoute == Screen.Leaderboard.route,
            onClick = { onNavigate(Screen.Leaderboard.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primary,
                selectedTextColor = primary,
                indicatorColor = primary.copy(alpha = 0.2f),
                unselectedIconColor = onSurfaceVariant,
                unselectedTextColor = onSurfaceVariant
            )
        )
    }
}
