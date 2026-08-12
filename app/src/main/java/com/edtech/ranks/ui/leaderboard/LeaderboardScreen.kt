package com.edtech.ranks.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.data.remote.supabase
import com.edtech.ranks.ui.theme.PrimaryBlue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val user_id: String,
    val full_name: String,
    val exam: String,
    val count: Long
)

sealed class LeaderboardState {
    object Loading : LeaderboardState()
    data class Success(val entries: List<LeaderboardEntry>) : LeaderboardState()
    data class Error(val message: String) : LeaderboardState()
}

class LeaderboardViewModel : ViewModel() {
    private val _leaderboardState = MutableStateFlow<LeaderboardState>(LeaderboardState.Loading)
    val leaderboardState: StateFlow<LeaderboardState> = _leaderboardState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedExam = MutableStateFlow("All")
    val selectedExam: StateFlow<String> = _selectedExam.asStateFlow()

    val availableExams = listOf("All", "NEET", "JEE", "UPSC", "SSC")

    init {
        fetchLeaderboard()
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
        fetchLeaderboard()
    }

    fun setExam(exam: String) {
        _selectedExam.value = exam
        fetchLeaderboard()
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _leaderboardState.value = LeaderboardState.Loading
            try {
                val viewName = if (_selectedTab.value == 0) "leaderboard_added" else "leaderboard_solved"
                
                var query = supabase.postgrest[viewName].select()
                
                if (_selectedExam.value != "All") {
                    query = supabase.postgrest[viewName].select {
                        filter {
                            eq("exam", _selectedExam.value)
                        }
                    }
                }

                // Decode and sort descending
                val result = query.decodeList<LeaderboardEntry>().sortedByDescending { it.count }
                
                _leaderboardState.value = LeaderboardState.Success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                _leaderboardState.value = LeaderboardState.Error("Failed to fetch leaderboard.")
            }
        }
    }
}

@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaderboardViewModel = viewModel()
) {
    val state by viewModel.leaderboardState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val selectedExam by viewModel.selectedExam.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Text("<", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }
            Text(
                text = "Global Leaderboard",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Tabs
        TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.surfaceVariant) {
            Tab(selected = selectedTab == 0, onClick = { viewModel.setTab(0) }) {
                Text("Top Contributors", modifier = Modifier.padding(16.dp), fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal)
            }
            Tab(selected = selectedTab == 1, onClick = { viewModel.setTab(1) }) {
                Text("Top Scholars", modifier = Modifier.padding(16.dp), fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exam Filters
        ScrollableTabRow(
            selectedTabIndex = viewModel.availableExams.indexOf(selectedExam),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            viewModel.availableExams.forEachIndexed { index, exam ->
                Tab(
                    selected = selectedExam == exam,
                    onClick = { viewModel.setExam(exam) },
                    text = { Text(exam) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when (state) {
            is LeaderboardState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }
            is LeaderboardState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state as LeaderboardState.Error).message, color = MaterialTheme.colorScheme.error)
                }
            }
            is LeaderboardState.Success -> {
                val entries = (state as LeaderboardState.Success).entries
                if (entries.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No one is on the leaderboard yet!")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(entries) { index, entry ->
                            LeaderboardCard(rank = index + 1, entry = entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardCard(rank: Int, entry: LeaderboardEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = if (rank <= 3) PrimaryBlue else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(48.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.full_name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = entry.exam,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = entry.count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}
