package com.edtech.ranks.data.local

object SpacedRepetitionHelper {

    // Simple SuperMemo-2 Algorithm implementation
    fun calculateNextReview(question: QuestionEntity, quality: Int): QuestionEntity {
        // quality: 0-5
        // 5: perfect response
        // 4: correct response after a hesitation
        // 3: correct response recalled with serious difficulty
        // 2: incorrect response; where the correct one seemed easy to recall
        // 1: incorrect response; the correct one remembered
        // 0: complete blackout

        var newRepetitionCount = question.repetitionCount
        var newIntervalDays = question.intervalDays
        var newEaseFactor = question.easeFactor

        if (quality >= 3) {
            if (newRepetitionCount == 0) {
                newIntervalDays = 1
            } else if (newRepetitionCount == 1) {
                newIntervalDays = 6
            } else {
                newIntervalDays = (newIntervalDays * newEaseFactor).toInt()
            }
            newRepetitionCount++
        } else {
            newRepetitionCount = 0
            newIntervalDays = 1
        }

        newEaseFactor = newEaseFactor + (0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f))
        if (newEaseFactor < 1.3f) {
            newEaseFactor = 1.3f
        }

        val nextReviewDate = System.currentTimeMillis() + (newIntervalDays * 24L * 60L * 60L * 1000L)

        return question.copy(
            repetitionCount = newRepetitionCount,
            intervalDays = newIntervalDays,
            easeFactor = newEaseFactor,
            nextReviewDate = nextReviewDate
        )
    }
}
