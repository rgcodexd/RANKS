package com.edtech.ranks.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.edtech.ranks.ui.components.Level1Card
import com.edtech.ranks.ui.components.PrimaryButton

@Composable
fun HomeScreen(
    onAddQuestionClick: () -> Unit,
    onCustomTestClick: () -> Unit,
    onVaultClick: () -> Unit,
    onCentralClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Micro Header / Welcoming Context
            MicroHeader(
                userName = uiState.profile?.full_name ?: "Student",
                targetExam = uiState.profile?.target_exam ?: "Set Target Exam",
                onShareClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Time Horizon Filter Track
            TimeFilterTrack()

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Stats Bento Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccuracyCard(
                    accuracy = uiState.accuracy,
                    modifier = Modifier.weight(1f)
                )
                QuestionsSolvedCard(
                    totalSolved = uiState.totalSolved,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Practice Action Area
            Text(
                text = "Active Sprints",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            ActiveSprintCard(
                dueQuestions = uiState.dueQuestionsCount,
                onPracticeClick = onCustomTestClick
            )

            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for navigation bar
        }
    }
}

@Composable
private fun MicroHeader(userName: String, targetExam: String, onShareClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Top 4%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = "$targetExam Sprint Cohort",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .size(40.dp)
        ) {
            Icon(Icons.Default.IosShare, contentDescription = "Share Report", tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun TimeFilterTrack() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(percent = 50))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeFilterButton(text = "7 Days", isSelected = true, modifier = Modifier.weight(1f))
        TimeFilterButton(text = "30 Days", isSelected = false, modifier = Modifier.weight(1f))
        TimeFilterButton(text = "All Time", isSelected = false, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TimeFilterButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    
    Box(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(percent = 50))
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun AccuracyCard(accuracy: Float, modifier: Modifier = Modifier) {
    Level1Card(modifier = modifier.height(140.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "ACCURACY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = "+5.2%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
            
            Text(
                text = "${String.format("%.1f", accuracy)}%",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(percent = 50))) {
                Box(modifier = Modifier.fillMaxWidth(accuracy / 100f).fillMaxHeight().background(MaterialTheme.colorScheme.primary, RoundedCornerShape(percent = 50)))
            }
        }
    }
}

@Composable
private fun QuestionsSolvedCard(totalSolved: Int, modifier: Modifier = Modifier) {
    Level1Card(modifier = modifier.height(140.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "QUESTIONS SOLVED", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Text(
                text = totalSolved.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "14 Day Streak", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

@Composable
private fun ActiveSprintCard(dueQuestions: Int, onPracticeClick: () -> Unit) {
    Level1Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Daily Review Sprint", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "Mixed Subjects • $dueQuestions Qs Due", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(progress = 0.3f, color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxSize())
                    Text(text = dueQuestions.toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Enter Sprint",
                onClick = onPracticeClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
