package com.edtech.ranks.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edtech.ranks.data.remote.supabase
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String, // corresponds to auth.users id
    val full_name: String,
    val target_exam: String,
    val class_grade: String,
    val setup_complete: Boolean = true
)

sealed class ProfileState {
    object Loading : ProfileState()
    object NotFound : ProfileState() // Profile needs to be set up
    data class Success(val profile: UserProfile) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        checkProfileExistence()
    }

    fun checkProfileExistence() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user == null) {
                    _profileState.value = ProfileState.Error("User not logged in")
                    return@launch
                }

                // Check if profile exists in public.profiles table
                val profileResult = supabase.postgrest["profiles"]
                    .select {
                        filter {
                            eq("id", user.id)
                        }
                    }
                    .decodeSingleOrNull<UserProfile>()

                if (profileResult != null && profileResult.setup_complete) {
                    _profileState.value = ProfileState.Success(profileResult)
                } else {
                    _profileState.value = ProfileState.NotFound
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // If the table doesn't exist yet or other error, assume NotFound for now
                // so the user can at least try to set it up (though save will fail without table)
                _profileState.value = ProfileState.NotFound
            }
        }
    }

    fun saveProfile(fullName: String, targetExam: String, classGrade: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user == null) {
                    _profileState.value = ProfileState.Error("User not logged in")
                    return@launch
                }

                val newProfile = UserProfile(
                    id = user.id,
                    full_name = fullName,
                    target_exam = targetExam,
                    class_grade = classGrade,
                    setup_complete = true
                )

                supabase.postgrest["profiles"].upsert(newProfile)
                
                _profileState.value = ProfileState.Success(newProfile)
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                _profileState.value = ProfileState.Error("Failed to save profile: ${e.message}")
            }
        }
    }
}
