package com.rbrauwers.newsapp.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.data.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ResetPasswordViewModel @Inject constructor(
    val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val inputState = MutableStateFlow(Input())
    private val statusState = MutableStateFlow(Status())

    val uiState = combine(
        userSettingsRepository.flow,
        inputState,
        statusState
    ) { userSettings, input, status ->
        val isValidEmail = userSettings.username == input.email && !input.email.isNullOrEmpty()

        UiState(
            email = input.email.orEmpty(),
            isResetPasswordEnabled = isValidEmail,
            emailError = if (isValidEmail) null else "Invalid email",
            isSuccess = status.isSuccess,
            isProcessing = status.isProcessing
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

    fun resetPassword() {
        if (!uiState.value.isValidEmail) {
            return
        }

        statusState.update {
            Status(isProcessing = true, isSuccess = false)
        }

        viewModelScope.launch {
            delay(3000)
            statusState.update {
                Status(isProcessing = false, isSuccess = true)
            }
        }
    }

    data class UiState(
        val email: String = "",
        val emailError: String? = null,
        val isValidEmail: Boolean = emailError == null,
        val isResetPasswordEnabled: Boolean = false,
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false
    )

    internal data class Input(
        val email: String? = null
    )

    internal data class Status(
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false
    )
}