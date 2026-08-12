package com.edtech.ranks.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edtech.ranks.ui.components.GlassCard
import com.edtech.ranks.ui.components.glowEffect
import com.edtech.ranks.ui.theme.*

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
            .background(background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Stellar HUD
        Text(
            text = "Welcome back, Commander",
            style = MaterialTheme.typography.headlineLarge,
            color = onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HudBadge(
                icon = "🔥", // Using emoji as placeholder for local_fire_department
                text = "Streak: 15 Days",
                iconColor = tertiary
            )
            HudBadge(
                icon = "🏅", // Using emoji for military_tech
                text = "Rank #42",
                iconColor = primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bento Grid Layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Daily Mission Ring
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(220.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Daily Mission",
                        style = MaterialTheme.typography.headlineMedium,
                        color = onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                    DailyMissionRing(progress = 0.75f, current = 15, total = 20)
                }
            }

            // Continue Practice
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(220.dp)
                    .glowEffect(color = primary.copy(alpha = 0.1f), radius = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Next Protocol", style = MaterialTheme.typography.headlineMedium, color = onSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Quantum Mechanics", style = MaterialTheme.typography.bodyMedium, color = onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(primary.copy(alpha = 0.1f), CircleShape)
                                .border(1.dp, primary.copy(alpha = 0.2f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Physics", style = MaterialTheme.typography.labelSmall, color = primary)
                        }
                    }
                    Button(
                        onClick = onCustomTestClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Brush.horizontalGradient(listOf(primaryContainer, primary)), RoundedCornerShape(8.dp))
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Initiate Sequence", style = MaterialTheme.typography.labelMedium, color = onPrimaryContainer)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = onPrimaryContainer, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Recent Captures Horizontal Scroll
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Recent Captures", style = MaterialTheme.typography.headlineMedium, color = onSurface)
            Text(
                text = "View Vault",
                style = MaterialTheme.typography.labelSmall,
                color = primary,
                modifier = Modifier.clickable { onVaultClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecentCaptureCard(title = "Calculus Definite Integrals", time = "2 hours ago", status = "done")
            RecentCaptureCard(title = "Organic Synthesis", time = "Yesterday", status = "pending")
        }

        Spacer(modifier = Modifier.height(100.dp)) // FAB spacer
    }
}

@Composable
fun HudBadge(icon: String, text: String, iconColor: Color) {
    Row(
        modifier = Modifier
            .background(surfaceVariant, CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp) // Fallback for Material Symbols
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = onSurface)
    }
}

@Composable
fun DailyMissionRing(progress: Float, current: Int, total: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    val currentProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300)
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = surfaceVariant,
            strokeWidth = 8.dp,
        )
        CircularProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier.fillMaxSize(),
            color = tertiary,
            strokeWidth = 8.dp,
            strokeCap = StrokeCap.Round
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = current.toString(), style = MaterialTheme.typography.displayMedium, color = onSurface)
            Text(text = "/$total Solved", style = MaterialTheme.typography.labelSmall, color = onSurfaceVariant)
        }
    }
}

@Composable
fun RecentCaptureCard(title: String, time: String, status: String) {
    GlassCard(
        modifier = Modifier
            .width(200.dp)
            .clickable { }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(surfaceVariant)
            ) {
                // Image placeholder
                Box(modifier = Modifier.fillMaxSize().background(primary.copy(alpha = 0.1f)))
                
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(GlassBackground, RoundedCornerShape(4.dp))
                        .padding(4.dp)
                ) {
                    if (status == "done") {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = tertiary, modifier = Modifier.size(16.dp))
                    } else {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = onSurfaceVariant, modifier = Modifier.size(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = onSurface, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = time, style = MaterialTheme.typography.labelSmall, color = onSurfaceVariant)
        }
    }
}
