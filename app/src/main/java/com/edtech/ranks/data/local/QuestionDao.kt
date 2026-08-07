package com.edtech.ranks.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM mistake_vault WHERE nextReviewDate <= :currentDate ORDER BY nextReviewDate ASC LIMIT 20")
    fun getQuestionsDueForReview(currentDate: Long): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: QuestionEntity)

    @Update
    fun updateQuestion(question: QuestionEntity)

    @Query("DELETE FROM mistake_vault WHERE id = :id")
    fun deleteQuestion(id: Int)
}
