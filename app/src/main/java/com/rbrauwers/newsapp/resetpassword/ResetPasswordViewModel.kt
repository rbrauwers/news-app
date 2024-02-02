package com.rbrauwers.newsapp.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.data.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val inputState = MutableStateFlow(Input())

    val uiState = combine(userSettingsRepository.flow, inputState) { userSettings, input ->
        UiState(
            email = input.email.orEmpty(),
            isResetPasswordEnabled = userSettings.username == input.email && !input.email.isNullOrEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun update(email: String) {
        inputState.update {
            it.copy(
                email = email
            )
        }
    }

    data class UiState(
        val email: String = "",
        val isResetPasswordEnabled: Boolean = false
    )

    data class Input(
        val email: String? = null
    )

}