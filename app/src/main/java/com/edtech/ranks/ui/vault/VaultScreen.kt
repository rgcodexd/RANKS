package com.edtech.ranks.ui.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.ui.components.Level1Card
import com.edtech.ranks.ui.components.PrimaryButton
import com.edtech.ranks.ui.components.StatusChip

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
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.primary)
            }
            Text(
                text = "Mistake Vault",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Filters
        ScrollableTabRow(
            selectedTabIndex = filters.indexOf(selectedFilter),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[filters.indexOf(selectedFilter)]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            filters.forEachIndexed { index, filter ->
                Tab(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    text = { 
                        Text(
                            text = filter,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                        ) 
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Content
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            when (vaultState) {
                is VaultState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                            PrimaryButton(
                                text = "Retry",
                                onClick = { viewModel.fetchQuestions() }
                            )
                        }
                    }
                }
                is VaultState.Success -> {
                    val questions = (vaultState as VaultState.Success).questions
                    if (questions.isEmpty()) {
                        // Premium Empty State
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Your Vault is Empty",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Scan and analyze questions to start building your personal mistake archive.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                )
                            }
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
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            groupedQuestions.forEach { (category, qList) ->
                                item {
                                    Text(
                                        text = category.ifBlank { "Uncategorized" },
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                    )
                                }
                                items(qList) { question ->
                                    Level1Card(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = question.questionText,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    if (question.subject.isNotBlank()) {
                                                        StatusChip(text = question.subject, color = MaterialTheme.colorScheme.tertiary)
                                                    }
                                                    if (question.chapter.isNotBlank()) {
                                                        StatusChip(text = question.chapter, color = MaterialTheme.colorScheme.secondary)
                                                    }
                                                }
                                                Text(
                                                    text = question.created_at.take(10),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
}
