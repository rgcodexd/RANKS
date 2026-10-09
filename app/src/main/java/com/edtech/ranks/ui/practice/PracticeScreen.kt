package com.edtech.ranks.ui.practice

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.ui.components.Level1Card
import com.edtech.ranks.ui.components.Level2Card
import com.edtech.ranks.ui.components.PrimaryButton
import com.edtech.ranks.ui.components.SecondaryButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PracticeScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: PracticeViewModel = viewModel()
) {
    val dueQuestions by viewModel.dueQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()

    var showAnswer by remember { mutableStateOf(false) }

    // Live Timer State
    var timeElapsed by remember { mutableStateOf(0) }
    LaunchedEffect(currentIndex, dueQuestions.isNotEmpty()) {
        timeElapsed = 0
        if (dueQuestions.isNotEmpty()) {
            while (true) {
                delay(1000)
                timeElapsed++
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (dueQuestions.isEmpty()) {
            // Empty / Done State
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Sprint Complete 🎉",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "You've conquered all due questions.\nRest up, champion.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                PrimaryButton(
                    text = "Return to Base",
                    onClick = onNavigateBack
                )
            }
        } else {
            val currentQuestion = dueQuestions[currentIndex]
            
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Exam HUD
                Level1Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.Default.Close, contentDescription = "Exit Sprint")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Daily Review Sprint",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Question ${currentIndex + 1} of ${dueQuestions.size}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            // Live timer
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(percent = 50))
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = "Timer", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                val minutes = timeElapsed / 60
                                val seconds = timeElapsed % 60
                                Text(
                                    text = String.format("%02d:%02d", minutes, seconds),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Progress Track
                        LinearProgressIndicator(
                            progress = (currentIndex + 1).toFloat() / dueQuestions.size,
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(percent = 50)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                // Question Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    AnimatedContent(
                        targetState = currentIndex,
                        transitionSpec = { fadeIn() with fadeOut() },
                        modifier = Modifier.fillMaxSize()
                    ) { _ ->
                        Level2Card(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = currentQuestion.questionText,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                if (showAnswer) {
                                    Spacer(modifier = Modifier.height(32.dp))
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                    Spacer(modifier = Modifier.height(32.dp))
                                    
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(
                                                text = "Metadata",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Topic: ${currentQuestion.topic}\nDifficulty: ${currentQuestion.difficulty}/10",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Actions Bottom Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    if (!showAnswer) {
                        PrimaryButton(
                            text = "Reveal Answer",
                            onClick = { showAnswer = true },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            "Recall Difficulty",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SecondaryButton(
                                text = "Hard",
                                onClick = {
                                    viewModel.submitAnswerQuality(0)
                                    showAnswer = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                            SecondaryButton(
                                text = "Good",
                                onClick = {
                                    viewModel.submitAnswerQuality(3)
                                    showAnswer = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                            PrimaryButton(
                                text = "Easy",
                                onClick = {
                                    viewModel.submitAnswerQuality(5)
                                    showAnswer = false
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
