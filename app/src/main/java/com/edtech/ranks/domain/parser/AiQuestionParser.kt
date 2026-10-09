package com.edtech.ranks.domain.parser

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AiParsedQuestion(
    val topic: String,
    val theme: String,
    val questionText: String,
    val options: List<String>,
    val answer: String? = null,
    val unnecessaryData: String? = null
)

class AiQuestionParser(apiKey: String) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro-latest", // Use the latest model
        apiKey = apiKey,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )

    suspend fun parseQuestionText(rawText: String): AiParsedQuestion? {
        val prompt = """
            You are an AI assistant designed to extract question information from raw OCR text.
            Analyze the following text and separate the actual question, its options (if any), 
            and the answer (if available). 
            Identify the topic and the theme of the question.
            Filter out all unnecessary data (like page numbers, random characters, irrelevant headers).
            
            Return the output STRICTLY as a JSON object matching this schema:
            {
              "topic": "String",
              "theme": "String",
              "questionText": "String",
              "options": ["Option A", "Option B", ...], // Empty list if no options
              "answer": "String or null",
              "unnecessaryData": "String or null" // The filtered out junk text
            }
            
            Raw Text:
            $rawText
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(content { text(prompt) })
            response.text?.let { jsonResponse ->
                val jsonString = jsonResponse.removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                Json { ignoreUnknownKeys = true }.decodeFromString<AiParsedQuestion>(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
