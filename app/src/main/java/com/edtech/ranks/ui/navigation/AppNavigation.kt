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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(bottom = 8.dp) // extra padding from screen bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                // Frosted glass effect approx: semi-transparent surface
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == Screen.Home.route,
                onClick = { onNavigate(Screen.Home.route) },
                modifier = Modifier.weight(1f)
            )
            NavItem(
                icon = Icons.Default.Bookmark,
                label = "Vault",
                selected = currentRoute == Screen.Vault.route,
                onClick = { onNavigate(Screen.Vault.route) },
                modifier = Modifier.weight(1f).padding(end = 24.dp) // Make room for FAB
            )
            // FAB space in center
            NavItem(
                icon = Icons.Default.History,
                label = "History",
                selected = currentRoute == Screen.Central.route,
                onClick = { onNavigate(Screen.Central.route) },
                modifier = Modifier.weight(1f).padding(start = 24.dp) // Make room for FAB
            )
            NavItem(
                icon = Icons.Default.Leaderboard,
                label = "Rank",
                selected = currentRoute == Screen.Leaderboard.route,
                onClick = { onNavigate(Screen.Leaderboard.route) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
