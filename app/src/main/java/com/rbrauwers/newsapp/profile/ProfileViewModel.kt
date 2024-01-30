package com.rbrauwers.newsapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.data.repository.UserSettingsRepository
import com.rbrauwers.newsapp.model.UserSettings
import com.rbrauwers.newsapp.model.isAuthenticated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val uiState: StateFlow<UiState> =
        userSettingsRepository.flow
            .map {
                UiState(
                    userSettings = if (it.isAuthenticated()) it else null,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState()
            )

    fun signOut() {
        viewModelScope.launch {
            userSettingsRepository.clear()
        }
    }

    data class UiState(
        val userSettings: UserSettings? = null,
        val isLoading: Boolean = true,
        val isLoginEnabled: Boolean = userSettings == null,
        val isSignOutEnabled: Boolean = userSettings != null,
    )

}