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
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.gotrue.auth
import com.edtech.ranks.data.remote.supabase

class CameraViewModel : ViewModel() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val _extractedText = MutableStateFlow("")
    val extractedText: StateFlow<String> = _extractedText.asStateFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying.asStateFlow()

    fun captureAndAnalyze(cameraController: LifecycleCameraController, context: Context) {
        cameraController.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        
                        recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                _extractedText.value = visionText.text
                                _isVerifying.value = true
                            }
                            .addOnFailureListener { e ->
                                e.printStackTrace()
                            }
                            .addOnCompleteListener { 
                                imageProxy.close() 
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }
        )
    }

    fun dismissVerification() {
        _isVerifying.value = false
        _extractedText.value = ""
    }

    fun saveQuestion(
        finalText: String, 
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
    val exam: String,
    val subject: String,
    val chapter: String,
    val topic: String,
    val difficulty: Int
)
