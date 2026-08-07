package com.edtech.ranks.ui.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edtech.ranks.data.remote.supabase
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> _authState.value = AuthState.Authenticated(status.session.user!!.id)
                    is SessionStatus.LoadingFromStorage -> _authState.value = AuthState.Loading
                    is SessionStatus.NetworkError -> _authState.value = AuthState.Error("Network Error")
                    is SessionStatus.NotAuthenticated -> _authState.value = AuthState.Idle
                }
            }
        }
    }

    fun signInWithGoogle(context: Context, serverClientId: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val credentialManager = CredentialManager.create(context)
                
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(serverClientId)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                
                if (credential is androidx.credentials.CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        supabaseAuthWithGoogle(idToken)
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error("Failed to parse credential: ${e.message}")
                    }
                } else {
                    _authState.value = AuthState.Error("Unexpected credential type")
                }

            } catch (e: Exception) {
                // Fallback to Supabase Native Web OAuth
                try {
                    val url = "${com.edtech.ranks.data.remote.SUPABASE_URL}/auth/v1/authorize?provider=google&redirect_to=ranks://login"
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch(webEx: Exception) {
                    _authState.value = AuthState.Error("Browser Error: ${webEx.message}")
                }
            }
        }
    }

    private suspend fun supabaseAuthWithGoogle(idToken: String) {
        try {
            supabase.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
            val user = supabase.auth.currentUserOrNull()
            if (user != null) {
                _authState.value = AuthState.Authenticated(user.id)
            } else {
                _authState.value = AuthState.Error("Supabase user is null")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Supabase authentication failed")
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
            } catch (e: Exception) {
                // Ignore error
            }
            _authState.value = AuthState.Idle
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
