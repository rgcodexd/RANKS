package com.edtech.ranks.domain.parser

import com.edtech.ranks.domain.parser.models.DetectedMarker
import com.edtech.ranks.domain.parser.models.MarkerType

object MarkerDetector {

    // Regex Patterns
    private val arabicPattern = Regex("""^(\d+)[\.\)\:\-]\s*""")
    private val romanUpperPattern = Regex("""^([IVX]+)[\.\)\:\-]\s*""")
    private val romanLowerPattern = Regex("""^([ivx]+)[\.\)\:\-]\s*""")
    private val letterUpperPattern = Regex("""^[\(\[]?([A-Z])[\.\)\:\-]\s*""")
    private val letterLowerPattern = Regex("""^[\(\[]?([a-z])[\.\)\:\-]\s*""")
    private val hindiPattern = Regex("""^([कखगघचछजझटठडढतथदधनपफबभमयरलवशषसह])[\.\)\:\-]\s*""")
    private val qPrefixPattern = Regex("""^(?i)q(?:uestion)?\.?\s*(?:no\.?)?\s*(\d+)[\.\)\:\-]?\s*""")
    private val bulletPattern = Regex("""^([•\-\*\◦\■\❖\➢\>])\s*""")
    private val oBulletPattern = Regex("""^([Oo])\s+""")
    
    // Hindi mapping (क -> 1, ख -> 2, etc. simplified for ABCD equivalent)
    private val hindiMap = mapOf(
        "क" to 1, "ख" to 2, "ग" to 3, "घ" to 4,
        "च" to 1, "छ" to 2, "ज" to 3, "झ" to 4,
        "ट" to 1, "ठ" to 2, "ड" to 3, "ढ" to 4,
        "त" to 1, "थ" to 2, "द" to 3, "ध" to 4, "न" to 5,
        "प" to 1, "फ" to 2, "ब" to 3, "भ" to 4, "म" to 5
    )

    fun detect(text: String): DetectedMarker {
        val trimmed = text.trim()
        
        qPrefixPattern.find(trimmed)?.let { match ->
            return DetectedMarker(
                raw = match.value,
                normalized = match.groupValues[1],
                type = MarkerType.Q_PREFIX,
                number = match.groupValues[1].toIntOrNull(),
                confidence = 0.99f
            )
        }

        arabicPattern.find(trimmed)?.let { match ->
            return DetectedMarker(
                raw = match.value,
                normalized = match.groupValues[1],
                type = MarkerType.ARABIC,
                number = match.groupValues[1].toIntOrNull(),
                confidence = 0.9f
            )
        }
        
        romanUpperPattern.find(trimmed)?.let { match ->
            val romanStr = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = romanStr,
                type = MarkerType.ROMAN_UPPER,
                number = romanToInt(romanStr),
                confidence = 0.85f
            )
        }

        romanLowerPattern.find(trimmed)?.let { match ->
            val romanStr = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = romanStr,
                type = MarkerType.ROMAN_LOWER,
                number = romanToInt(romanStr),
                confidence = 0.85f
            )
        }
        
        letterUpperPattern.find(trimmed)?.let { match ->
            val letter = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = letter,
                type = MarkerType.LETTER_UPPER,
                number = letter[0] - 'A' + 1,
                confidence = 0.8f
            )
        }
        
        letterLowerPattern.find(trimmed)?.let { match ->
            val letter = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = letter,
                type = MarkerType.LETTER_LOWER,
                number = letter[0] - 'a' + 1,
                confidence = 0.8f
            )
        }
        
        hindiPattern.find(trimmed)?.let { match ->
            val letter = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = letter,
                type = MarkerType.HINDI,
                number = hindiMap[letter],
                confidence = 0.95f
            )
        }
        
        bulletPattern.find(trimmed)?.let { match ->
            val bullet = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = bullet,
                type = MarkerType.BULLET,
                number = null,
                confidence = 0.7f
            )
        }

        oBulletPattern.find(trimmed)?.let { match ->
            val bullet = match.groupValues[1]
            return DetectedMarker(
                raw = match.value,
                normalized = bullet,
                type = MarkerType.BULLET,
                number = null,
                confidence = 0.7f
            )
        }
        
        return DetectedMarker(

            raw = "",
            normalized = null,
            type = MarkerType.NONE,
            number = null,
            confidence = 0f
        )
    }

    private fun romanToInt(s: String): Int {
        var result = 0
        val map = mapOf('I' to 1, 'V' to 5, 'X' to 10, 'i' to 1, 'v' to 5, 'x' to 10)
        for (i in s.indices) {
            val current = map[s[i]] ?: return 0
            val next = if (i + 1 < s.length) map[s[i + 1]] ?: return 0 else 0
            if (current < next) {
                result -= current
            } else {
                result += current
            }
        }
        return result
    }
}
