package com.edtech.ranks.domain.parser

import com.edtech.ranks.domain.parser.models.BlockType
import com.edtech.ranks.domain.parser.models.DetectedMarker
import com.edtech.ranks.domain.parser.models.MarkerType
import com.edtech.ranks.domain.parser.models.OcrElement
import com.edtech.ranks.domain.parser.models.ParsedOption
import com.edtech.ranks.domain.parser.models.ParsedPage
import com.edtech.ranks.domain.parser.models.ParsedQuestion
import com.google.mlkit.vision.text.Text
import java.util.UUID

object QuestionSegmentationEngine {

    fun parsePage(scanId: String, pageNumber: Int, text: Text): ParsedPage {
        // 1. Flatten into OcrElements and sort
        val elements = flattenAndSort(text)

        // 2. Marker Detection and Sequence Analysis
        val markers = elements.map { MarkerDetector.detect(it.text) }
        
        // 3. Determine Dominant Markers
        val questionMarkerType = determineQuestionMarkerType(markers)
        val optionMarkerType = determineOptionMarkerType(markers, questionMarkerType)

        // 4. Block Classification
        val classifiedBlocks = classifyBlocks(elements, markers, questionMarkerType, optionMarkerType)

        // 5. Grouping
        val questions = groupIntoQuestions(classifiedBlocks, elements, markers)

        // 6. Extract Header (text before the first question)
        val firstQuestionIndex = classifiedBlocks.indexOfFirst { it == BlockType.QUESTION }
        val headerElements = if (firstQuestionIndex > 0) {
            elements.subList(0, firstQuestionIndex).filterIndexed { index, _ -> classifiedBlocks[index] == BlockType.UNKNOWN }
        } else if (firstQuestionIndex == -1) {
            elements.filterIndexed { index, _ -> classifiedBlocks[index] == BlockType.UNKNOWN }
        } else {
            emptyList()
        }
        val pageHeader = if (headerElements.isNotEmpty()) headerElements.joinToString(" ") { it.text.trim() } else null

        return ParsedPage(
            scanId = scanId,
            pageNumber = pageNumber,
            questions = questions,
            pageHeader = pageHeader
        )
    }

    private fun flattenAndSort(text: Text): List<OcrElement> {
        val elements = mutableListOf<OcrElement>()
        for ((blockIndex, block) in text.textBlocks.withIndex()) {
            for ((lineIndex, line) in block.lines.withIndex()) {
                elements.add(
                    OcrElement(
                        text = line.text,
                        boundingBox = line.boundingBox,
                        blockIndex = blockIndex,
                        lineIndex = lineIndex
                    )
                )
            }
        }
        
        // Sort top-to-bottom, then left-to-right
        return elements.sortedWith(Comparator { e1, e2 ->
            val box1 = e1.boundingBox
            val box2 = e2.boundingBox
            if (box1 == null && box2 == null) return@Comparator 0
            if (box1 == null) return@Comparator -1
            if (box2 == null) return@Comparator 1

            // If roughly on the same line (within 10 pixels vertically)
            if (Math.abs(box1.top - box2.top) < 10) {
                box1.left.compareTo(box2.left)
            } else {
                box1.top.compareTo(box2.top)
            }
        })
    }

    private fun determineQuestionMarkerType(markers: List<DetectedMarker>): MarkerType {
        // Look for the most common sequence starting from 1
        // Simplified approach: find the first sequence that makes sense as a question
        val counts = markers.filter { it.type != MarkerType.NONE }.groupingBy { it.type }.eachCount()
        // If there's Q_PREFIX or QUESTION_PREFIX, favor them
        if (counts.keys.contains(MarkerType.Q_PREFIX)) return MarkerType.Q_PREFIX
        if (counts.keys.contains(MarkerType.QUESTION_PREFIX)) return MarkerType.QUESTION_PREFIX
        
        // Otherwise ARABIC is most common for questions
        if (counts.keys.contains(MarkerType.ARABIC)) return MarkerType.ARABIC
        
        return counts.maxByOrNull { it.value }?.key ?: MarkerType.NONE
    }

    private fun determineOptionMarkerType(markers: List<DetectedMarker>, questionMarkerType: MarkerType): MarkerType {
        val counts = markers.filter { it.type != MarkerType.NONE && it.type != questionMarkerType }.groupingBy { it.type }.eachCount()
        
        if (counts.keys.contains(MarkerType.LETTER_UPPER)) return MarkerType.LETTER_UPPER
        if (counts.keys.contains(MarkerType.LETTER_LOWER)) return MarkerType.LETTER_LOWER
        if (counts.keys.contains(MarkerType.HINDI)) return MarkerType.HINDI
        if (counts.keys.contains(MarkerType.ROMAN_LOWER)) return MarkerType.ROMAN_LOWER
        if (counts.keys.contains(MarkerType.BULLET)) return MarkerType.BULLET
        
        return counts.maxByOrNull { it.value }?.key ?: MarkerType.NONE
    }

    private fun classifyBlocks(
        elements: List<OcrElement>,
        markers: List<DetectedMarker>,
        questionMarkerType: MarkerType,
        optionMarkerType: MarkerType
    ): List<BlockType> {
        val classified = mutableListOf<BlockType>()
        var currentContext = BlockType.UNKNOWN

        for (i in elements.indices) {
            val marker = markers[i]
            
            val type = when {
                marker.type == questionMarkerType && marker.type != MarkerType.NONE -> {
                    currentContext = BlockType.QUESTION
                    BlockType.QUESTION
                }
                marker.type == optionMarkerType && marker.type != MarkerType.NONE -> {
                    currentContext = BlockType.OPTION
                    BlockType.OPTION
                }
                // Fallback: If it has a marker but we aren't sure, let's see if it continues a sequence or is indented
                marker.type != MarkerType.NONE && currentContext == BlockType.QUESTION -> {
                    currentContext = BlockType.OPTION
                    BlockType.OPTION
                }
                marker.type != MarkerType.NONE && currentContext == BlockType.OPTION -> {
                    BlockType.OPTION
                }
                currentContext == BlockType.QUESTION || currentContext == BlockType.OPTION -> {
                    // Check for positional separation if no marker
                    val prevElement = if (i > 0) elements[i - 1] else null
                    val isNewBlock = prevElement != null && prevElement.blockIndex != elements[i].blockIndex
                    val isSignificantSpace = prevElement != null && elements[i].boundingBox != null && prevElement.boundingBox != null && 
                                             (elements[i].boundingBox!!.top - prevElement.boundingBox!!.bottom) > 20
                    
                    if ((isNewBlock || isSignificantSpace) && optionMarkerType == MarkerType.NONE && currentContext == BlockType.QUESTION) {
                        currentContext = BlockType.OPTION
                        BlockType.OPTION
                    } else if ((isNewBlock || isSignificantSpace) && currentContext == BlockType.OPTION) {
                        BlockType.OPTION
                    } else {
                        if (currentContext == BlockType.QUESTION) BlockType.QUESTION_CONTINUATION else BlockType.OPTION_CONTINUATION
                    }
                }
                else -> BlockType.UNKNOWN
            }
            classified.add(type)
        }
        return classified
    }

    private fun groupIntoQuestions(
        classifiedBlocks: List<BlockType>,
        elements: List<OcrElement>,
        markers: List<DetectedMarker>
    ): List<ParsedQuestion> {
        val questions = mutableListOf<ParsedQuestion>()
        
        var currentQuestion: ParsedQuestion? = null
        var currentOptions = mutableListOf<ParsedOption>()
        var currentOption: ParsedOption? = null
        var currentQuestionText = ""
        var currentOptionText = ""

        fun finalizeOption() {
            currentOption?.let {
                currentOptions.add(it.copy(text = currentOptionText.trim()))
            }
            currentOption = null
            currentOptionText = ""
        }

        fun finalizeQuestion() {
            finalizeOption()
            currentQuestion?.let {
                questions.add(
                    it.copy(
                        questionText = currentQuestionText.trim(),
                        options = currentOptions.toList(),
                        // Basic validation: Check if we have 0 options or weird gaps
                        needsReview = currentOptions.isEmpty() || currentOptions.size !in 2..5,
                        confidence = if (currentOptions.size in 2..5) 0.95f else 0.6f
                    )
                )
            }
            currentQuestion = null
            currentQuestionText = ""
            currentOptions = mutableListOf()
        }

        for (i in elements.indices) {
            val type = classifiedBlocks[i]
            val element = elements[i]
            val marker = markers[i]

            when (type) {
                BlockType.QUESTION -> {
                    finalizeQuestion()
                    currentQuestion = ParsedQuestion(
                        id = "q_${UUID.randomUUID().toString().take(8)}",
                        number = marker.number,
                        originalMarker = marker.raw,
                        markerType = marker.type.name,
                        questionText = "",
                        options = emptyList(),
                        optionPattern = "UNKNOWN",
                        confidence = 1.0f,
                        needsReview = false
                    )
                    currentQuestionText = element.text.removePrefix(marker.raw).trim()
                }
                BlockType.QUESTION_CONTINUATION -> {
                    if (currentQuestionText.isNotEmpty()) currentQuestionText += " "
                    currentQuestionText += element.text.trim()
                }
                BlockType.OPTION -> {
                    finalizeOption()
                    currentOption = ParsedOption(
                        id = "opt_${UUID.randomUUID().toString().take(8)}",
                        label = marker.raw,
                        normalizedLabel = marker.normalized,
                        labelType = marker.type.name,
                        text = ""
                    )
                    currentOptionText = element.text.removePrefix(marker.raw).trim()
                }
                BlockType.OPTION_CONTINUATION -> {
                    if (currentOptionText.isNotEmpty()) currentOptionText += " "
                    currentOptionText += element.text.trim()
                }
                else -> {}
            }
        }
        
        finalizeQuestion()
        
        return questions
    }
}
