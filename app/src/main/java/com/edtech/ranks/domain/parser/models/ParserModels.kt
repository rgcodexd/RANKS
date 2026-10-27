package com.edtech.ranks.domain.parser.models

import android.graphics.Rect
import kotlinx.serialization.Serializable

enum class MarkerType {
    ARABIC,
    ROMAN_UPPER,
    ROMAN_LOWER,
    LETTER_UPPER,
    LETTER_LOWER,
    HINDI,
    Q_PREFIX,
    QUESTION_PREFIX,
    BULLET,
    NONE
}

data class DetectedMarker(
    val raw: String,
    val normalized: String?,
    val type: MarkerType,
    val number: Int?,
    val confidence: Float
)

enum class BlockType {
    QUESTION,
    OPTION,
    QUESTION_CONTINUATION,
    OPTION_CONTINUATION,
    HEADER,
    INSTRUCTION,
    UNKNOWN
}

data class OcrElement(
    val text: String,
    val boundingBox: Rect?,
    val blockIndex: Int,
    val lineIndex: Int
)

@Serializable
data class ParsedOption(
    val id: String,
    val label: String,
    val normalizedLabel: String?,
    val labelType: String,
    val text: String
)

@Serializable
data class ParsedQuestion(
    val id: String,
    val number: Int?,
    val originalMarker: String,
    val markerType: String,
    val questionText: String,
    val options: List<ParsedOption>,
    val optionPattern: String,
    val confidence: Float,
    val needsReview: Boolean,
    val validationIssues: List<String> = emptyList()
)

@Serializable
data class ParsedPage(
    val scanId: String,
    val pageNumber: Int,
    val questions: List<ParsedQuestion>,
    val pageHeader: String? = null
)
