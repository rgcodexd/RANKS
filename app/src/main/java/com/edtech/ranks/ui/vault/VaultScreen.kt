package com.edtech.ranks.ui.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.ui.components.GlassCard
import com.edtech.ranks.ui.theme.*

@Composable
fun VaultScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VaultViewModel = viewModel()
) {
    val vaultState by viewModel.vaultState.collectAsState()
    
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Exam", "Chapter", "Topic")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Text("<", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primary)
            }
            Text(
                text = "Question Vault",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = onSurface
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Filters
        ScrollableTabRow(
            selectedTabIndex = filters.indexOf(selectedFilter),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            filters.forEachIndexed { index, filter ->
                Tab(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    text = { Text(filter) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when (vaultState) {
            is VaultState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primary)
                }
            }
            is VaultState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (vaultState as VaultState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.fetchQuestions() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is VaultState.Success -> {
                val questions = (vaultState as VaultState.Success).questions
                if (questions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No questions in vault yet.")
                    }
                } else {
                    // Grouping logic based on filter
                    val groupedQuestions = when (selectedFilter) {
                        "Exam" -> questions.groupBy { it.exam }
                        "Chapter" -> questions.groupBy { it.chapter }
                        "Topic" -> questions.groupBy { it.topic }
                        else -> mapOf("All Questions" to questions)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        groupedQuestions.forEach { (category, qList) ->
                            item {
                                Text(
                                    text = category.ifBlank { "Uncategorized" },
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = tertiary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            items(qList) { question ->
                                GlassCard(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = question.questionText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = onSurface
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "${question.subject} • ${question.chapter}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = onSurfaceVariant
                                            )
                                            Text(
                                                text = "Added: ${question.created_at.take(10)}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
