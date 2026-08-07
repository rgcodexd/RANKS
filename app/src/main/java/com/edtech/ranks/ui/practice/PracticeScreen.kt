package com.edtech.ranks.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.ui.theme.PrimaryBlue

@Composable
fun PracticeScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: PracticeViewModel = viewModel()
) {
    val dueQuestions by viewModel.dueQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()

    var showAnswer by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (dueQuestions.isEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "You're all caught up! 🎉",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "No questions due for review in the Mistake Vault right now.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onNavigateBack, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text("Go Back Home")
                }
            }
        } else {
            val currentQuestion = dueQuestions[currentIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with back button
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    }
                    Text(
                        text = "Custom Test",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
                }

                // Progress Indicator
                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / dueQuestions.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(bottom = 32.dp),
                    color = PrimaryBlue,
                    trackColor = MaterialTheme.colorScheme.surface
                )

                // Question Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentQuestion.questionText,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                            textAlign = TextAlign.Center
                        )
                        
                        if (showAnswer) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Topic: ${currentQuestion.topic} | Difficulty: ${currentQuestion.difficulty}/10",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Actions
                if (!showAnswer) {
                    Button(
                        onClick = { showAnswer = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Show Answer")
                    }
                } else {
                    Text(
                        "How difficult was it to recall?",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RatingButton(text = "Hard (0)", color = MaterialTheme.colorScheme.error) {
                            viewModel.submitAnswerQuality(0)
                            showAnswer = false
                        }
                        RatingButton(text = "Good (3)", color = com.edtech.ranks.ui.theme.PrimaryBlueVariant) {
                            viewModel.submitAnswerQuality(3)
                            showAnswer = false
                        }
                        RatingButton(text = "Easy (5)", color = androidx.compose.ui.graphics.Color(0xFF4CAF50)) {
                            viewModel.submitAnswerQuality(5)
                            showAnswer = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingButton(text: String, color: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text)
    }
}
