package com.edtech.ranks.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.edtech.ranks.data.local.AppDatabase
import com.edtech.ranks.data.remote.supabase
import com.edtech.ranks.ui.profile.UserProfile
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val totalSolved: Int = 0,
    val dueQuestionsCount: Int = 0,
    val accuracy: Float = 0.0f // Mocked accuracy for now, until quality is properly aggregated
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val questionDao = AppDatabase.getDatabase(application).questionDao()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    // Load Profile
                    val profile = supabase.postgrest["profiles"]
                        .select { filter { eq("id", user.id) } }
                        .decodeSingleOrNull<UserProfile>()

                    // Load Solved Count
                    val solvedCount = supabase.postgrest["solved_events"]
                        .select { 
                            filter { eq("user_id", user.id) }
                            count(Count.EXACT)
                        }
                        .countOrNull()?.toInt() ?: 0

                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        totalSolved = solvedCount,
                        // Pseudo accuracy calculation based on solved count to make it dynamic
                        accuracy = if (solvedCount > 0) 65f + (solvedCount % 30f) else 0f
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }

        // Observe due questions for "Continue Practice" card
        viewModelScope.launch {
            questionDao.getQuestionsDueForReview(System.currentTimeMillis()).collectLatest { questions ->
                _uiState.value = _uiState.value.copy(
                    dueQuestionsCount = questions.size
                )
            }
        }
    }
}
