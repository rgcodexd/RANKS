package com.edtech.ranks.ui.practice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.edtech.ranks.data.local.AppDatabase
import com.edtech.ranks.data.local.QuestionEntity
import com.edtech.ranks.data.local.SpacedRepetitionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.edtech.ranks.data.remote.supabase
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable

@Serializable
data class SolvedEventInsert(
    val user_id: String,
    val exam: String
)

class PracticeViewModel(application: Application) : AndroidViewModel(application) {

    private val questionDao = AppDatabase.getDatabase(application).questionDao()

    private val _dueQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val dueQuestions: StateFlow<List<QuestionEntity>> = _dueQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    init {
        loadDueQuestions()
    }

    private fun loadDueQuestions() {
        viewModelScope.launch {
            questionDao.getQuestionsDueForReview(System.currentTimeMillis()).collectLatest { questions ->
                _dueQuestions.value = questions
                _currentQuestionIndex.value = 0
            }
        }
    }

    fun submitAnswerQuality(quality: Int) {
        val questions = _dueQuestions.value
        val index = _currentQuestionIndex.value

        if (index < questions.size) {
            val currentQuestion = questions[index]
            val updatedQuestion = SpacedRepetitionHelper.calculateNextReview(currentQuestion, quality)
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                questionDao.updateQuestion(updatedQuestion)

                // If quality indicates it was solved/reviewed successfully
                if (quality > 0) {
                    try {
                        val user = supabase.auth.currentUserOrNull()
                        if (user != null) {
                            val event = SolvedEventInsert(user.id, currentQuestion.exam)
                            supabase.postgrest["solved_events"].insert(event)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            if (index < questions.size - 1) {
                _currentQuestionIndex.value = index + 1
            } else {
                // Done with all questions in this session
                _dueQuestions.value = emptyList()
            }
        }
    }
}
