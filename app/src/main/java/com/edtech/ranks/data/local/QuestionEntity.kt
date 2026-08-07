package com.edtech.ranks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mistake_vault")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val supabaseId: String,
    val questionText: String,
    val subject: String,
    val topic: String,
    val difficulty: Int,
    // SuperMemo-2 (SM-2) algorithm fields for spaced repetition
    val repetitionCount: Int = 0,
    val intervalDays: Int = 1,
    val easeFactor: Float = 2.5f,
    val nextReviewDate: Long = System.currentTimeMillis()
)
