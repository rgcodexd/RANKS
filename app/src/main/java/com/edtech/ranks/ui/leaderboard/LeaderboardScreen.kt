package com.edtech.ranks.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.edtech.ranks.data.remote.supabase
import com.edtech.ranks.ui.components.Level1Card
import com.edtech.ranks.ui.components.Level2Card
import com.edtech.ranks.ui.components.glowEffect
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

    private val _selectedExam = MutableStateFlow("All Exams")
    val selectedExam: StateFlow<String> = _selectedExam.asStateFlow()

    val availableExams = listOf("All Exams", "SAT", "JEE", "MCAT")

    init {
        // We will mock the data to perfectly match the UI for now, 
        // but normally this fetches from fetchLeaderboard()
        loadMockData()
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
        loadMockData()
    }

    fun setExam(exam: String) {
        _selectedExam.value = exam
        loadMockData()
    }

    private fun loadMockData() {
        val mockEntries = listOf(
            LeaderboardEntry("1", "Sarah Chen", "All Star", 15890),
            LeaderboardEntry("2", "Alex Nova", "Physics", 14250),
            LeaderboardEntry("3", "Marcus Wu", "Math", 13900),
            LeaderboardEntry("4", "Elena Rostova", "Chemistry", 12450),
            LeaderboardEntry("5", "David Kim", "Biology", 11200),
            LeaderboardEntry("6", "Priya Patel", "JEE", 10850)
        )
        _leaderboardState.value = LeaderboardState.Success(mockEntries)
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _leaderboardState.value = LeaderboardState.Loading
            try {
                val viewName = if (_selectedTab.value == 0) "leaderboard_added" else "leaderboard_solved"
                
                var query = supabase.postgrest[viewName].select()
                
                if (_selectedExam.value != "All Exams") {
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp) // space for top app bar
        ) {
            // Header
            Text(
                text = "Global Leaderboard",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "See where you stand in the cosmic ranks.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Toggle
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (selectedTab == 0) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            .clickable { viewModel.setTab(0) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Scholars", color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (selectedTab == 1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            .clickable { viewModel.setTab(1) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Contributors", color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Filter Dropdown Mock
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(selectedExam, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterEnd).size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Content
            when (state) {
                is LeaderboardState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is LeaderboardState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((state as LeaderboardState.Error).message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is LeaderboardState.Success -> {
                    val entries = (state as LeaderboardState.Success).entries
                    val top3 = entries.take(3)
                    val rest = entries.drop(3)

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 140.dp) // space for pinned rank
                    ) {
                        item {
                            // Podium
                            if (top3.size == 3) {
                                PodiumView(top3)
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        if (rest.isNotEmpty()) {
                            item {
                                // List Header
                                Level1Card(modifier = Modifier.fillMaxWidth()) {
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                .padding(horizontal = 24.dp, vertical = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Rank", modifier = Modifier.width(64.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                            Text("Scholar", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                            Text("Points", modifier = Modifier.width(80.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
                                        }

                                        // List Items
                                        rest.forEachIndexed { index, entry ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text((index + 4).toString(), modifier = Modifier.width(64.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                                    Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceBright, CircleShape).clip(CircleShape)) {
                                                        // Fallback avatar
                                                        AsyncImage(
                                                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDVjNbi5j6UcUe95dscgrnLf3b0e_WcpdN-MtC29SfL3laFtdx51bvfs4xADwxNMgP6O0atL9jgTKlrGq2uoSgbig7ZkZe-GvOZve6876DmrV9RxnJwHbzzAS9ARwcqqTRkadwJ54I8KgpfuTkTyMGGk6mn8c4CMbA67zOnGZMaGP6E4Xe99dbbViJgDYSUFgJhlcXPEtcaXBkyDm9KvGbz1y3Ngu2HtAZmJzkZukfoNC847DcmMnNetw",
                                                            contentDescription = null,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                    Text(entry.full_name, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
                                                }
                                                Text("%,d".format(entry.count), modifier = Modifier.width(80.dp), color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
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

        // Pinned User Rank
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 80.dp) // above bottom nav
        ) {
            Level2Card(
                modifier = Modifier.fillMaxWidth().glowEffect(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), 30.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("42", color = MaterialTheme.colorScheme.tertiary, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(48.dp))
                    
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape).clip(CircleShape)) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD0py3QLqiKBIneY32tk55brixNx0f4v1hVHoGIh8w0jDAN5m3wC66gvKHMo0JXbfcDmbHfp6AM7hA2rRI5Wzh5M5iXKmw8L9zP7uf3rtARrY6pygF3Na1MFd9vCnMJfIrTeW1V0HbUAMdDUea9Y5COwSnnX5OG4axnZwXyVjGujwNhruMBSiE11k88DSIiyd0_IDTGwkJ2L_F4a6fZBWU18ZMascF6Sxslc0MYjmOpkgAr6dZSORsRhQ",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("You (Commander)", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                            Text("Top 15%", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("4,500 pts", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
                            Text("12 positions", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumView(top3: List<LeaderboardEntry>) {
    // Top 3 Podium layout
    // 2 - 1 - 3
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Gold (1st)
        PodiumCard(
            rank = 1,
            entry = top3[0],
            glowColor = MaterialTheme.colorScheme.tertiary,
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD91Nt6u6K1t6bK276hsD_SahhKAp_cOXqvxBn5PogcGMGQ95mIucdAsukXMIlM5PZLFd2mBrtMRtR0piaJvadE5B4QgJ9Dc1JbEFJ2YwHmLWIUVTeryGnWKMYvqFAOPuEhZnyMCdvKsFqbJFGSES1bMNCUz79CuZ9bvAwiJT7x8YhfglMXzImiVjQlQg_5KvetEqwOcCOk9FY5sC7yTG6E2m9Nvh_E2tUDuc_vLPWRmppMUZ5qh3qwug"
        )
        // Silver (2nd)
        PodiumCard(
            rank = 2,
            entry = top3[1],
            glowColor = MaterialTheme.colorScheme.onSurfaceVariant,
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDY5skXG6RTdCjgQFagHi54iJ8J78gGnAwwo06qfvv6CxtYG7CXfENgmDDZ0Mgz4mGIWjMD5Pu_do0j5ijQOc9YvRlj0RCkkxkJLGfdb6nXtgqbazF7XNRykEUbg_fIVrwv8OUQSIkLdhsvr8g6ciqVCSOPZuh3wmfEICrUAl61Bkj86iXRmzRriKZhD5MlXHdleg48VArzIBG7PhkDenbZEaqazvORwtuhvh6U1Iu7N7p9osAQq_2LfA"
        )
        // Bronze (3rd)
        PodiumCard(
            rank = 3,
            entry = top3[2],
            glowColor = MaterialTheme.colorScheme.tertiaryContainer,
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKfk3_Y9CDr7TjXllnAW9MZXCXhGfPGyh717jrH16IWpQ7DbatYGa1PRdQ5M3ET1lp8ogYxf2cGKuBQNIpbmnUDL2hPpuaeKS2vaW6cL_tSbVvcIyWg6oFukYzHQ8CRo59o4YgTCw6UgNEAf6aDZafvzdpwyeP4NGp-1hLmKHfzHttIAixOv_yUb0Rp7mvg46oWnM_VUTv1g5EuQVJJt_DnQop-cZbZVGUi-QBYwfxTmAUk1zE6j4KpQ"
        )
    }
}

@Composable
fun PodiumCard(rank: Int, entry: LeaderboardEntry, glowColor: Color, avatarUrl: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Level1Card(
            modifier = Modifier.fillMaxWidth().glowEffect(glowColor.copy(alpha = 0.2f), 20.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(if (rank == 1) 96.dp else 80.dp)
                        .border(4.dp, glowColor, CircleShape)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = entry.full_name,
                    style = if (rank == 1) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (rank == 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "%,d pts".format(entry.count),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (rank == 1) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .background(if (rank == 1) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .border(1.dp, if (rank == 1) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(entry.exam, color = if (rank == 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }
        
        // Rank Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-16).dp)
                .size(if (rank == 1) 40.dp else 32.dp)
                .background(glowColor, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                .glowEffect(glowColor.copy(alpha = 0.5f), 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                color = if (rank == 1) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = if (rank == 1) 20.sp else 16.sp
            )
        }
    }
}
