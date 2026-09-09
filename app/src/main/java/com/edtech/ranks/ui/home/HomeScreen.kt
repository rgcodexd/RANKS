package com.edtech.ranks.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    onAddQuestionClick: () -> Unit,
    onCustomTestClick: () -> Unit,
    onVaultClick: () -> Unit,
    onCentralClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        HomeTopBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // User Greeting & Stats
            UserHeader(onLeaderboardClick = onLeaderboardClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Subjects Grid
            Text(
                text = "Subjects",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            SubjectsGrid()

            Spacer(modifier = Modifier.height(32.dp))

            // Practice Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Continue Practice",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            PracticeCard(onPracticeClick = onCustomTestClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Access
            Text(
                text = "Quick Access",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QuickAccessCard(
                    title = "Bookmarks",
                    icon = Icons.Default.BookmarkBorder,
                    onClick = onVaultClick,
                    modifier = Modifier.weight(1f)
                )
                QuickAccessCard(
                    title = "Recent Errors",
                    icon = Icons.Default.ErrorOutline,
                    onClick = onCentralClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for navigation bar
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.School, contentDescription = "App Logo", tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "RANKS",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        // Gamification - Coins
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "450", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun UserHeader(onLeaderboardClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAqSuFdmBprpjgqS-kCWsHZNxgmyuYUEctXCmthpYgHgUdwn3bgUOTsZ-dKXYWAoIh7206mCBDfbxaAYocVmhQx40jBJGh3mf6F1ySgN5blXfNjoRHk2LducOZ2vsEOyd_cp4I1K6pFK4w9gGbOWcPePI_re3zeKaDi6W-gIYGKp1fQN_V7rexkYKKXzJTJ4tbHtehC_ge4PElEKoUbasOjfyOPAx6K2eKBIkUtfPfGC-Bt_jCSosBXnQ",
            contentDescription = "User Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Hello, Student", style = MaterialTheme.typography.headlineSmall)
            Text(text = "JEE Main Target 2025", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        IconButton(onClick = onLeaderboardClick) {
            Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun SubjectsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SubjectCard(title = "Physics", icon = Icons.Default.Science, progress = 0.4f, modifier = Modifier.weight(1f))
            SubjectCard(title = "Chemistry", icon = Icons.Default.Biotech, progress = 0.6f, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SubjectCard(title = "Mathematics", icon = Icons.Default.Functions, progress = 0.3f, modifier = Modifier.weight(1f))
            SubjectCard(title = "Mock Tests", icon = Icons.Default.Quiz, progress = 0f, modifier = Modifier.weight(1f), isSpecial = true)
        }
    }
}

@Composable
private fun SubjectCard(title: String, icon: ImageVector, progress: Float, modifier: Modifier = Modifier, isSpecial: Boolean = false) {
    ElevatedCard(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSpecial) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = title, 
                tint = if (isSpecial) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title, 
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSpecial) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                if (!isSpecial) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progress, 
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PracticeCard(onPracticeClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Thermodynamics", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Physics • 15 Qs Left", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                CircularProgressIndicator(progress = 0.65f, modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onPracticeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Resume Practice")
            }
        }
    }
}

@Composable
private fun QuickAccessCard(title: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelLarge)
        }
    }
}
