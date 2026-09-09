package com.edtech.ranks.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edtech.ranks.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    serverClientId: String
) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onAuthSuccess()
        }
    }

    // Animation states for floating orbs
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    val offsetY1 by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )
    val offsetY2 by infiniteTransition.animateFloat(
        initialValue = 30f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background)
    ) {
        // Nebula Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(primaryContainer.copy(alpha = 0.15f), background),
                        radius = 800f
                    )
                )
        )

        // Floating Orbs
        Box(
            modifier = Modifier
                .offset(x = 40.dp, y = (100 + offsetY1).dp)
                .size(200.dp)
                .background(primaryContainer.copy(alpha = 0.2f), RoundedCornerShape(100.dp))
                .blur(80.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-40).dp, y = (-100 + offsetY2).dp)
                .size(250.dp)
                .background(tertiaryContainer.copy(alpha = 0.2f), RoundedCornerShape(125.dp))
                .blur(100.dp)
        )

        // Main Glass Panel
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x990B1326))
                .border(1.dp, primary.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Text(
                    text = "RANKS",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp
                    ),
                    color = primary
                )
                Text(
                    text = "Project Anti-Gravity Initialization",
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                // Auth Form
                AuthForm(
                    onGoogleClick = { viewModel.signInWithGoogle(context, serverClientId) },
                    isLoading = authState is AuthState.Loading,
                    errorMessage = (authState as? AuthState.Error)?.message,
                    onSkip = onAuthSuccess
                )
            }
        }
    }
}

@Composable
fun AuthForm(
    onGoogleClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSkip: () -> Unit
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Toggle Switch
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isLogin) primaryContainer else Color.Transparent)
                .clickable { isLogin = true }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Login",
                color = if (isLogin) onPrimaryContainer else onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (!isLogin) primaryContainer else Color.Transparent)
                .clickable { isLogin = false }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up",
                color = if (!isLogin) onPrimaryContainer else onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Form Fields
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        placeholder = { Text("Commander Email", color = onSurfaceVariant.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = onSurfaceVariant) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = surfaceVariant.copy(alpha = 0.3f),
            focusedContainerColor = surfaceVariant.copy(alpha = 0.5f),
            unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.3f),
            focusedBorderColor = primary.copy(alpha = 0.5f),
            cursorColor = primary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        placeholder = { Text("Security Protocol (Password)", color = onSurfaceVariant.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = onSurfaceVariant) },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = onSurfaceVariant
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = surfaceVariant.copy(alpha = 0.3f),
            focusedContainerColor = surfaceVariant.copy(alpha = 0.5f),
            unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.3f),
            focusedBorderColor = primary.copy(alpha = 0.5f),
            cursorColor = primary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(8.dp))
    
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Text("Forgot Protocol?", color = primary, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { })
    }

    Spacer(modifier = Modifier.height(24.dp))

    if (errorMessage != null) {
        Text(text = errorMessage, color = error, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 8.dp))
    }

    Button(
        onClick = { /* Email auth not yet implemented */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = primaryContainer)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = onPrimaryContainer, modifier = Modifier.size(24.dp))
        } else {
            Text("Initiate Mission", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = onPrimaryContainer)
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Divider
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.weight(1f), color = onSurfaceVariant.copy(alpha = 0.2f))
        Text(
            text = "Or dock via",
            color = onSurfaceVariant,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
        Divider(modifier = Modifier.weight(1f), color = onSurfaceVariant.copy(alpha = 0.2f))
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Social Buttons
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = surfaceVariant.copy(alpha = 0.3f),
                contentColor = onSurface
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, onSurfaceVariant.copy(alpha = 0.2f))
        ) {
            Text("Google", fontWeight = FontWeight.SemiBold)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        OutlinedButton(
            onClick = { /* Github auth */ },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = surfaceVariant.copy(alpha = 0.3f),
                contentColor = onSurface
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, onSurfaceVariant.copy(alpha = 0.2f))
        ) {
            Text("Github", fontWeight = FontWeight.SemiBold)
        }
    }
    
    Spacer(modifier = Modifier.height(32.dp))
    
    Row(
        modifier = Modifier.clickable { onSkip() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Skip for now", color = onSurfaceVariant, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}
