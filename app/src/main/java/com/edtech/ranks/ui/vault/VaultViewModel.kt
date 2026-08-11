package com.edtech.ranks.ui.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edtech.ranks.data.remote.supabase
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class VaultQuestion(
    val id: String,
    @SerialName("questiontext")
    val questionText: String,
    val exam: String,
    val subject: String,
    val chapter: String,
    val topic: String,
    val difficulty: Int,
    val created_at: String
)

sealed class VaultState {
    object Loading : VaultState()
    data class Success(val questions: List<VaultQuestion>) : VaultState()
    data class Error(val message: String) : VaultState()
}

class VaultViewModel : ViewModel() {

    private val _vaultState = MutableStateFlow<VaultState>(VaultState.Loading)
    val vaultState: StateFlow<VaultState> = _vaultState.asStateFlow()

    init {
        fetchQuestions()
    }

    fun fetchQuestions() {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user == null) {
                    _vaultState.value = VaultState.Error("User not logged in")
                    return@launch
                }

                // Assuming the table is "questions"
                val questions = supabase.postgrest["questions"]
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<VaultQuestion>()
                
                _vaultState.value = VaultState.Success(questions)

            } catch (e: Exception) {
                e.printStackTrace()
                _vaultState.value = VaultState.Error("Failed to load questions from Vault.")
            }
        }
    }
}
