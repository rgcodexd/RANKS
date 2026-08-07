package com.edtech.ranks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.edtech.ranks.ui.auth.AuthScreen
import com.edtech.ranks.ui.camera.CameraScreen
import com.edtech.ranks.ui.theme.RANKSTheme
import com.edtech.ranks.ui.navigation.AppNavigation
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.jan.supabase.gotrue.handleDeeplinks
import com.edtech.ranks.data.remote.supabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabase.handleDeeplinks(intent)
        
        setContent {
            RANKSTheme {
                var isAuthenticated by remember { mutableStateOf(false) }

                AppNavigation(
                    isAuthenticated = isAuthenticated,
                    onAuthSuccess = { isAuthenticated = true }
                )
            }
        }
    }
}

