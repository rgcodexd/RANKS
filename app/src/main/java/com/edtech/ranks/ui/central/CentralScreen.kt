package com.edtech.ranks.ui.central

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.data.remote.supabase
import com.edtech.ranks.ui.components.Level1Card
import com.edtech.ranks.ui.components.StatusChip
import com.edtech.ranks.ui.vault.VaultQuestion
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CentralState {
    object Loading : CentralState()
    data class Success(val questions: List<VaultQuestion>) : CentralState()
    data class Error(val message: String) : CentralState()
}

class CentralViewModel : ViewModel() {
    private val _centralState = MutableStateFlow<CentralState>(CentralState.Loading)
    val centralState: StateFlow<CentralState> = _centralState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchPublicQuestions()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        fetchPublicQuestions(query)
    }

    fun fetchPublicQuestions(query: String = "") {
        viewModelScope.launch {
            _centralState.value = CentralState.Loading
            try {
                // Fetch public questions
                val result = if (query.isNotBlank()) {
                    supabase.postgrest["questions"]
                        .select {
                            filter {
                                eq("is_public", true)
                                ilike("questiontext", "%$query%")
                            }
                        }
                        .decodeList<VaultQuestion>()
                } else {
                    supabase.postgrest["questions"]
                        .select {
                            filter {
                                eq("is_public", true)
                            }
                        }
                        .decodeList<VaultQuestion>()
                }
                
                _centralState.value = CentralState.Success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                _centralState.value = CentralState.Error("Failed to load central database.")
            }
        }
    }
}

@Composable
fun CentralScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CentralViewModel = viewModel()
) {
    val centralState by viewModel.centralState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

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
                text = "Central Hive",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search community questions...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(percent = 50)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Content
            when (centralState) {
                is CentralState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is CentralState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (centralState as CentralState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(onClick = { viewModel.fetchPublicQuestions(searchQuery) }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is CentralState.Success -> {
                    val questions = (centralState as CentralState.Success).questions
                    if (questions.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No questions found.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(questions) { question ->
                                Level1Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = question.questionText,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (!question.answer.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = "A: ${question.answer}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
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
                                                if (question.exam.isNotBlank()) {
                                                    StatusChip(text = question.exam, color = MaterialTheme.colorScheme.secondary)
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
