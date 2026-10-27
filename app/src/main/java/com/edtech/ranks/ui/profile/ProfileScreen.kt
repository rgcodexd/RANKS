package com.edtech.ranks.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.edtech.ranks.ui.components.GlassCard
import com.edtech.ranks.ui.components.glowEffect
import com.edtech.ranks.ui.theme.*

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val profileState by viewModel.profileState.collectAsState()
    
    var fullName = "Commander"
    var targetExam = "Physics Expert"
    var classGrade = "Calculus Elite"
    
    if (profileState is ProfileState.Success) {
        val p = (profileState as ProfileState.Success).profile
        fullName = p.full_name
        targetExam = p.target_exam
        classGrade = p.class_grade
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background)
    ) {
        // Ambient background effect
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(300.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(300.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(tertiary.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header Section
            ProfileHeaderCard(fullName = fullName, targetExam = targetExam, classGrade = classGrade)

            Spacer(modifier = Modifier.height(16.dp))

            // Bento Grid
            AnalyticsGrid()

            Spacer(modifier = Modifier.height(16.dp))

            // Mission Goals
            MissionGoalsCard()

            Spacer(modifier = Modifier.height(16.dp))

            // System Controls
            SystemControls(onSignOut = onSignOut)

            Spacer(modifier = Modifier.height(100.dp)) // padding for bottom nav
        }
    }
}

@Composable
fun ProfileHeaderCard(fullName: String, targetExam: String, classGrade: String) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar & PRO badge
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, primary.copy(alpha = 0.5f), CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCz5T_zHvnZf-NBioClCKefIKlyUfkbZOtqLeYQVuax9_egEA4Q3IyO6kOA1ZA7AW6Blrf164gXDpY7TuC_9h6Q_qrRsoOFlkejCS5ZCvFPBvfp_stGt2LS5CB6Jj42TYRxz6d7qAWVxVVpPDxFpR-gWC5RvH3Wo76RJD-Jl2D8iLI5fthLn6EO3CF4-OS6-ZIow6fXU6-Xf8OeK6H69We__8vN2tddY8RBsNvEevOAWPl0-pgcI-dcRA",
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                // PRO badge
                Row(
                    modifier = Modifier
                        .offset(y = 10.dp)
                        .background(tertiary, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = onTertiary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PRO", color = onTertiary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = fullName,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = primary
            )
            Text(
                text = "High-Performance Scholar Division",
                style = MaterialTheme.typography.bodyLarge,
                color = onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            
            // Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(text = targetExam, bgColor = primary.copy(alpha = 0.1f), textColor = primary)
                Spacer(modifier = Modifier.width(8.dp))
                Chip(text = classGrade, bgColor = secondary.copy(alpha = 0.1f), textColor = secondary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Chip(text = "Joined 2084", bgColor = surfaceVariant, textColor = onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Buttons
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Mission Profile", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, onSurfaceVariant.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = onSurfaceVariant)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Stats", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun Chip(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.dp, textColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun AnalyticsGrid() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            // Global Rank
            GlassCard(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = tertiary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Global Rank", color = onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("#42", color = tertiary, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("↑ Top 0.5%", color = secondary, fontSize = 10.sp, modifier = Modifier.padding(bottom = 6.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(surface, CircleShape)
                            .border(1.dp, tertiary.copy(alpha = 0.3f), CircleShape)
                            .glowEffect(tertiary, 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = tertiary, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            // Accuracy Rate
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = secondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Accuracy Rate", color = onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("85%", color = secondary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        CircularProgressIndicator(
                            progress = 0.85f,
                            modifier = Modifier.size(36.dp),
                            color = secondary,
                            trackColor = surfaceVariant,
                            strokeWidth = 3.dp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Questions Solved
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Questions Solved", color = onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text("1,204", color = primary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("+24 this week", color = onSurfaceVariant.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 3
        GlassCard(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Insights, contentDescription = null, tint = primaryContainer, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("30-Day Activity Matrix", color = onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Mock bars
                    for (i in 0 until 30) {
                        val height = (10..80).random()
                        val isActive = i > 25
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(height / 100f)
                                .padding(horizontal = 1.dp)
                                .background(
                                    color = if (isActive) primaryContainer else surfaceVariant,
                                    shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 4
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            // Contributed
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LibraryAdd, contentDescription = null, tint = tertiary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Contributed", color = onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text("34", color = onSurface, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Database additions", color = onSurfaceVariant.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Primary Focus
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Icon(
                        Icons.Default.Science,
                        contentDescription = null,
                        tint = onSurfaceVariant.copy(alpha = 0.1f),
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 10.dp, y = 10.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = onSurfaceVariant, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Primary Focus", color = onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            Text("Quantum Mech", color = onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("142 hrs logged", color = secondary, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MissionGoalsCard() {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Current Objectives", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = primary)
                }
                Icon(Icons.Default.MoreHoriz, contentDescription = null, tint = onSurfaceVariant)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Goal 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .border(1.dp, onSurfaceVariant.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Solve 50 Physics Questions", color = onSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("38 / 50", color = secondary, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = 38f / 50f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = secondary,
                        trackColor = surfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Goal 2
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .border(1.dp, onSurfaceVariant.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Maintain 7-Day Streak", color = onSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Day 5", color = tertiary, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (i in 0 until 7) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .background(if (i < 5) tertiary else surfaceVariant, RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SystemControls(onSignOut: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        GlassCard(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = onSurface, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("System Preferences", color = onSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        GlassCard(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            borderColor = error.copy(alpha = 0.3f)
        ) {
            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = error, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Disconnect", color = error, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
