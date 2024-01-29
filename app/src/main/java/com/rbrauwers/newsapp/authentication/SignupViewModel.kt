package com.rbrauwers.newsapp.authentication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SignupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val emailState: StateFlow<String?> =
        savedStateHandle.getStateFlow(emailArg, null)

    private val passwordState: StateFlow<String?> =
        savedStateHandle.getStateFlow(StateKeys.Password.name, null)

    private val passwordConfirmationState: StateFlow<String?> =
        savedStateHandle.getStateFlow(StateKeys.PasswordConfirmation.name, null)

    private val statusState: MutableStateFlow<Status> = MutableStateFlow(Status())

    val uiState: StateFlow<UiState> =
        combine(
            emailState,
            passwordState,
            passwordConfirmationState,
            statusState
        ) { email, password, passwordConfirmation, status ->
            combine(
                email = email,
                password = password,
                passwordConfirmation = passwordConfirmation,
                status = status,
                passwordValidationResult = ValidatePasswordUseCase().validate(
                    password = password,
                    passwordConfirmation = passwordConfirmation
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState(email = "")
        )

    fun signup() {
        statusState.update {
            Status(isProcessing = true)
        }

        viewModelScope.launch {
            delay(4000)
            statusState.update {
                Status(
                    isProcessing = false,
                    isSuccess = true
                )
            }
        }
    }

    fun updatePassword(value: String?) {
        savedStateHandle[StateKeys.Password.name] = value
    }

    fun updatePasswordConfirmation(value: String?) {
        savedStateHandle[StateKeys.PasswordConfirmation.name] = value
    }

    data class UiState(
        val email: String,
        val canProceed: Boolean = false,
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false,
        val password: String = "",
        val passwordConfirmation: String = "",
        val passwordError: String? = null,
        val passwordConfirmationError: String? = null
    )

    data class Status(
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false
    )

    private fun combine(
        email: String?,
        password: String?,
        passwordConfirmation: String?,
        status: Status?,
        passwordValidationResult: ValidatePasswordUseCase.Result
    ): UiState {
        val validationError = passwordValidationResult as? ValidatePasswordUseCase.Result.Failure

        return UiState(
            email = email.orEmpty(),
            password = password.orEmpty(),
            passwordConfirmation = passwordConfirmation.orEmpty(),
            canProceed = passwordValidationResult is ValidatePasswordUseCase.Result.Success,
            passwordError = validationError?.passwordError,
            passwordConfirmationError = validationError?.passwordConfirmationError,
            isProcessing = status?.isProcessing ?: false,
            isSuccess = status?.isSuccess ?: false
        )
    }

    private enum class StateKeys {
        Password,
        PasswordConfirmation
    }

}
