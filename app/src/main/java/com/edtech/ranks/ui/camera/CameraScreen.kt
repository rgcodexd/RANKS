package com.edtech.ranks.ui.camera

import android.Manifest
import android.net.Uri
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Sync
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.edtech.ranks.domain.parser.models.ParsedPage
import com.edtech.ranks.ui.components.glowEffect
import com.edtech.ranks.ui.theme.*

@Composable
fun CameraScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    
    var hasPermission by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val extractedText by viewModel.extractedText.collectAsState()
    val isVerifying by viewModel.isVerifying.collectAsState()
    val capturedImages by viewModel.capturedImages.collectAsState()
    val parsedPage by viewModel.parsedPage.collectAsState()
    
    var previewImageUri by remember { mutableStateOf<String?>(null) }

    // Animation for scanline
    val infiniteTransition = rememberInfiniteTransition(label = "scanline")
    val scanlineY by infiniteTransition.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanlineAnim"
    )
    
    // Animation for pulse rings
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = CubicBezierEasing(0f, 0f, 0.2f, 1f)),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAnim"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = CubicBezierEasing(0f, 0f, 0.2f, 1f)),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlphaAnim"
    )

    if (hasPermission) {
        Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
            // Camera Preview
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )

            // Viewfinder overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                com.edtech.ranks.ui.components.ScannerViewfinder(
                    isScanning = isVerifying || (viewModel.parsedPage.collectAsState().value != null)
                )
            }

            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(background.copy(alpha = 0.8f), Color.Transparent)))
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearBatch()
                        onNavigateBack()
                    },
                    modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = onSurfaceVariant)
                }
                
                // Batch Mode Indicator
                Row(
                    modifier = Modifier
                        .background(primaryContainer.copy(alpha = 0.2f), RoundedCornerShape(100.dp))
                        .border(1.dp, primary.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_camera), contentDescription = null, tint = primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("BATCH MODE ON", color = primary, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
                
                IconButton(
                    onClick = { /* Toggle Flash */ },
                    modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = "Flash", tint = onSurfaceVariant)
                }
            }

            // OCR Status Indicator (Optional/Mock for UI)
            if (viewModel.parsedPage.collectAsState().value != null || isVerifying) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 100.dp)
                        .background(surfaceContainerHigh.copy(alpha = 0.9f), RoundedCornerShape(100.dp))
                        .border(1.dp, outlineVariant, RoundedCornerShape(100.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Memory, contentDescription = null, tint = secondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Processing question...", color = onSurface, fontSize = 14.sp)
                }
            }

            // Bottom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(surfaceContainer.copy(alpha = 0.8f))
                    .padding(top = 16.dp, bottom = 32.dp)
            ) {
                // Captured Thumbnails
                if (capturedImages.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(capturedImages) { index, image ->
                            Box(
                                modifier = Modifier
                                    .size(width = 60.dp, height = 80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(if (index == capturedImages.size - 1) 2.dp else 1.dp, if (index == capturedImages.size - 1) primary else outlineVariant, RoundedCornerShape(8.dp))
                                    .clickable { previewImageUri = image.uri }
                            ) {
                                AsyncImage(
                                    model = Uri.parse(image.uri),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(16.dp)
                                        .background(if (index == capturedImages.size - 1) primary else surfaceVariant, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${index + 1}", color = if (index == capturedImages.size - 1) onPrimary else onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Action Buttons for Captured Images
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.processBatch(context) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = surfaceVariant,
                                contentColor = onSurfaceVariant
                            )
                        ) {
                            Icon(Icons.Default.Memory, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Check OCR", fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = { 
                                // Directly add questions (if required) or show confirmation 
                                // Assuming we want them to go through Check OCR for now, or just process without modal
                                // For now, let's trigger processBatch which opens verification.
                                viewModel.processBatch(context)
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primary,
                                contentColor = onPrimary
                            )
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Question", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Primary Capture Button
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Pulsing Ring
                    val pulsingColor = primary.copy(alpha = pulseAlpha)
                    val staticRingColor = primary.copy(alpha = 0.3f)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .drawBehind {
                                drawCircle(
                                    color = pulsingColor,
                                    radius = size.width / 2 * pulseScale
                                )
                                drawCircle(
                                    color = staticRingColor,
                                    radius = size.width / 2 * 1.2f,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                                )
                            }
                    )

                    // Actual Button
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(surfaceContainerHigh, surface)))
                            .border(2.dp, primary, CircleShape)
                            .clickable { viewModel.captureImage(cameraController, context) },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Capture", tint = onPrimary)
                            }
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission required", color = Color.White)
        }
    }

    if (isVerifying) {
        VerificationModal(
            initialText = extractedText,
            parsedPage = parsedPage,
            onDismiss = { 
                viewModel.dismissVerification() 
                viewModel.clearBatch()
            },
            onSave = { finalText, answerText, isPublic, exam, subject, chapter, topic, imageUri -> 
                viewModel.saveQuestion(
                    finalText, answerText, isPublic, exam, subject, chapter, topic, imageUri,
                    onSuccess = {
                        android.widget.Toast.makeText(context, "Question added successfully!", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    onError = { errorMsg ->
                        android.widget.Toast.makeText(context, "Error: $errorMsg", android.widget.Toast.LENGTH_LONG).show()
                    }
                )
            }
        )
    }

    if (previewImageUri != null) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { previewImageUri = null },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                AsyncImage(
                    model = Uri.parse(previewImageUri),
                    contentDescription = "Preview",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
                // Top Bar with Close
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { previewImageUri = null }) {
                        Icon(androidx.compose.material.icons.Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                    androidx.compose.material3.TextButton(onClick = { 
                        viewModel.removeImage(previewImageUri!!)
                        previewImageUri = null 
                    }) {
                        Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CanvasCorners() {
    val color = secondary
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val cornerLength = 40.dp.toPx()
        val strokeWidth = 3.dp.toPx()
        
        // Top Left
        drawLine(color, Offset(0f, 0f), Offset(cornerLength, 0f), strokeWidth)
        drawLine(color, Offset(0f, 0f), Offset(0f, cornerLength), strokeWidth)
        
        // Top Right
        drawLine(color, Offset(size.width, 0f), Offset(size.width - cornerLength, 0f), strokeWidth)
        drawLine(color, Offset(size.width, 0f), Offset(size.width, cornerLength), strokeWidth)
        
        // Bottom Left
        drawLine(color, Offset(0f, size.height), Offset(cornerLength, size.height), strokeWidth)
        drawLine(color, Offset(0f, size.height), Offset(0f, size.height - cornerLength), strokeWidth)
        
        // Bottom Right
        drawLine(color, Offset(size.width, size.height), Offset(size.width - cornerLength, size.height), strokeWidth)
        drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - cornerLength), strokeWidth)
    }
}

@Composable
fun VerificationModal(
    initialText: String,
    parsedPage: ParsedPage?,
    onDismiss: () -> Unit,
    onSave: (String, String, Boolean, String, String, String, String, String?) -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val totalQuestions = parsedPage?.questions?.size ?: 1
    
    var questionText by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(false) }
    var exam by remember { mutableStateOf("JEE Advanced") }
    var subject by remember { mutableStateOf("Physics") }
    var questionImageUri by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(currentQuestionIndex, parsedPage) {
        val q = parsedPage?.questions?.getOrNull(currentQuestionIndex)
        val o = q?.options
        questionText = q?.questionText ?: if (currentQuestionIndex == 0) initialText else ""
        optionA = o?.getOrNull(0)?.text ?: "Option A"
        optionB = o?.getOrNull(1)?.text ?: "Option B"
        optionC = o?.getOrNull(2)?.text ?: "Option C"
        optionD = o?.getOrNull(3)?.text ?: "Option D"
        questionImageUri = null
        
        if (currentQuestionIndex == 0 && !parsedPage?.pageHeader.isNullOrBlank()) {
            val header = parsedPage?.pageHeader ?: ""
            if (header.contains("Physics", ignoreCase = true)) subject = "Physics"
            else if (header.contains("Chemistry", ignoreCase = true)) subject = "Chemistry"
            else if (header.contains("Math", ignoreCase = true)) subject = "Math"
            else if (header.contains("Computer", ignoreCase = true)) subject = "Computers"
        }
    }

    val imagePickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        questionImageUri = uri?.toString()
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main Modal Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceContainerHighest.copy(alpha = 0.95f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceContainerLowest.copy(alpha = 0.5f))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.05f)), // Simulated bottom border
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("OCR Verification (Question ${currentQuestionIndex + 1} of $totalQuestions)", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = onSurface)
                            Text("Review and edit extracted content", style = MaterialTheme.typography.labelSmall, color = onSurfaceVariant)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = onSurfaceVariant)
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Question Stem Editor
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("QUESTION STEM", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp), color = primary)
                            Text(
                                "+ Add Image/Diagram", 
                                style = MaterialTheme.typography.labelSmall, 
                                color = secondary, 
                                modifier = Modifier.clickable { 
                                    imagePickerLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            )
                        }
                        if (questionImageUri != null) {
                            Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(8.dp))) {
                                AsyncImage(
                                    model = Uri.parse(questionImageUri),
                                    contentDescription = "Question Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = { questionImageUri = null },
                                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(Color.Black.copy(alpha=0.5f), CircleShape).size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove Image", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        OutlinedTextField(
                            value = questionText,
                            onValueChange = { questionText = it },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = surfaceContainerLowest.copy(alpha = 0.5f),
                                focusedContainerColor = surfaceContainerLowest.copy(alpha = 0.5f),
                                unfocusedBorderColor = outlineVariant,
                                focusedBorderColor = primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Options Editor
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("OPTIONS", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp), color = primary)
                        
                        // Option A
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, outlineVariant, RoundedCornerShape(8.dp)).background(surfaceContainerLow),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text("A", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = onSurfaceVariant)
                            }
                            BasicTextField(
                                value = optionA,
                                onValueChange = { optionA = it },
                                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurface)
                            )
                        }
                        
                        // Option B
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, outlineVariant, RoundedCornerShape(8.dp)).background(surfaceContainerLow),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text("B", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = onSurfaceVariant)
                            }
                            BasicTextField(
                                value = optionB,
                                onValueChange = { optionB = it },
                                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurface)
                            )
                        }
                        
                        // Option C (Highlighted Correct)
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, primary, RoundedCornerShape(8.dp)).background(primaryContainer.copy(alpha = 0.2f)).glowEffect(primary.copy(alpha = 0.2f), 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.background(primary.copy(alpha = 0.2f)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text("C", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = primary)
                            }
                            BasicTextField(
                                value = optionC,
                                onValueChange = { optionC = it },
                                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurface)
                            )
                        }
                        
                        // Option D
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, outlineVariant, RoundedCornerShape(8.dp)).background(surfaceContainerLow),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text("D", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = onSurfaceVariant)
                            }
                            BasicTextField(
                                value = optionD,
                                onValueChange = { optionD = it },
                                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurface)
                            )
                        }
                    }

                    // Metadata Tags
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(surfaceContainerLow)
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Metadata Tags", style = MaterialTheme.typography.labelLarge, color = onSurfaceVariant)
                        
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Subject", style = MaterialTheme.typography.labelSmall, color = outline)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.background(primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)).border(1.dp, primary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                                    Text("Physics", color = primary, fontSize = 12.sp)
                                }
                                Box(modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)).border(1.dp, outlineVariant, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                                    Text("Math", color = onSurfaceVariant, fontSize = 12.sp)
                                }
                                Box(modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)).border(1.dp, outlineVariant, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                                    Text("+ Add", color = onSurfaceVariant, fontSize = 12.sp)
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Exam Context", style = MaterialTheme.typography.labelSmall, color = outline)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.background(tertiary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)).border(1.dp, tertiary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                                    Text("JEE Advanced", color = tertiary, fontSize = 12.sp)
                                }
                                Box(modifier = Modifier.background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)).border(1.dp, outlineVariant, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                                    Text("NEET", color = onSurfaceVariant, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Visibility
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(surfaceContainerLow)
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Visibility", style = MaterialTheme.typography.labelLarge, color = onSurfaceVariant)
                        
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { isPublic = false }) {
                            RadioButton(selected = !isPublic, onClick = { isPublic = false }, colors = RadioButtonDefaults.colors(selectedColor = primary, unselectedColor = outline))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Private Vault", color = if (!isPublic) primary else onSurface, fontSize = 16.sp)
                                Text("Only you can access this.", color = outline, fontSize = 12.sp)
                            }
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { isPublic = true }) {
                            RadioButton(selected = isPublic, onClick = { isPublic = true }, colors = RadioButtonDefaults.colors(selectedColor = secondary, unselectedColor = outline))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Central Database", color = if (isPublic) secondary else onSurface, fontSize = 16.sp)
                                Text("Share with the community.", color = outline, fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Footer Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.05f)),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isLast = currentQuestionIndex == totalQuestions - 1
                    OutlinedButton(
                        onClick = {
                            if (isLast) onDismiss() else currentQuestionIndex++
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = onSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, outlineVariant)
                    ) {
                        Text(if (isLast) "Cancel" else "Skip")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            val combinedAnswer = "A) $optionA\nB) $optionB\nC) $optionC\nD) $optionD" 
                            onSave(questionText, combinedAnswer, isPublic, exam, subject, "", "", questionImageUri) 
                            
                            if (isLast) {
                                onDismiss()
                            } else {
                                currentQuestionIndex++
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Brush.horizontalGradient(listOf(primary, primaryContainer)), RoundedCornerShape(8.dp))
                                .glowEffect(primary.copy(alpha = 0.4f), 15.dp)
                                .padding(horizontal = 32.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (isLast) "Confirm & Finish" else "Save & Next", color = surfaceContainerLowest, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
