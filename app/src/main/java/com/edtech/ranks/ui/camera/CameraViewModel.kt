package com.edtech.ranks.ui.camera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import androidx.camera.core.ImageCaptureException
import android.net.Uri
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.gotrue.auth
import com.edtech.ranks.data.remote.supabase

class CameraViewModel : ViewModel() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val _extractedText = MutableStateFlow("")
    val extractedText: StateFlow<String> = _extractedText.asStateFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying.asStateFlow()

    private val _capturedImages = MutableStateFlow<List<CapturedImage>>(emptyList())
    val capturedImages: StateFlow<List<CapturedImage>> = _capturedImages.asStateFlow()

    fun captureImage(cameraController: LifecycleCameraController, context: Context) {
        val outputDirectory = context.getExternalFilesDir(null)
        val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        cameraController.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val newImage = CapturedImage(savedUri.toString(), System.currentTimeMillis())
                    _capturedImages.value = _capturedImages.value + newImage
                }
                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                }
            }
        )
    }

    fun processBatch(context: Context) {
        val currentImages = _capturedImages.value
        if (currentImages.isEmpty()) return

        // Save metadata to JSON
        val jsonFile = File(context.getExternalFilesDir(null), "batch_${System.currentTimeMillis()}.json")
        val jsonString = Json.encodeToString(currentImages)
        jsonFile.writeText(jsonString)

        // For this prototype, we'll process the first image to extract text for the Verification Modal
        // In a full LLM integration, we would send all images/text to the LLM.
        val firstImageUri = Uri.parse(currentImages.first().uri)
        val image = InputImage.fromFilePath(context, firstImageUri)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                _extractedText.value = visionText.text
                _isVerifying.value = true
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun clearBatch() {
        _capturedImages.value = emptyList()
    }

    fun dismissVerification() {
        _isVerifying.value = false
        _extractedText.value = ""
    }

    fun saveQuestion(
        finalText: String, 
        answerText: String,
        isPublic: Boolean,
        exam: String, 
        subject: String, 
        chapter: String, 
        topic: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()
                val newQuestion = SupabaseQuestionInsert(
                    user_id = user?.id ?: "",
                    questionText = finalText,
                    answer = answerText,
                    is_public = isPublic,
                    exam = exam,
                    subject = subject,
                    chapter = chapter,
                    topic = topic,
                    difficulty = 1
                )
                
                // Assuming your table is named "questions"
                supabase.postgrest["questions"].insert(newQuestion)
                
                _isVerifying.value = false
                _extractedText.value = ""
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e.message ?: "Unknown error")
            }
        }
    }
}

@Serializable
data class SupabaseQuestionInsert(
    val user_id: String,
    @SerialName("questiontext")
    val questionText: String,
    val answer: String? = null,
    val image_url: String? = null,
    val is_public: Boolean = false,
    val exam: String,
    val subject: String,
    val chapter: String,
    val topic: String,
    val difficulty: Int
)

@Serializable
data class CapturedImage(
    val uri: String,
    val timestamp: Long
)
