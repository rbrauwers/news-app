package com.rbrauwers.newsapp.authentication

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.data.repository.UserSettingsRepository
import com.rbrauwers.newsapp.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
internal class SignupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val emailState: StateFlow<String?> =
        savedStateHandle.getStateFlow(emailArg, null)

    private val inputState: StateFlow<Input> =
        savedStateHandle.getStateFlow(StateKeys.Input.name, Input())

    private val statusState: MutableStateFlow<Status> = MutableStateFlow(Status())

    val uiState: StateFlow<UiState> =
        combine(
            emailState,
            inputState,
            statusState
        ) { email, input, status ->
            combine(
                email = email,
                input = input,
                status = status,
                passwordValidationResult = ValidatePasswordUseCase().validate(
                    password = input.password,
                    passwordConfirmation = input.passwordConfirmation
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
            userSettingsRepository.save(
                UserSettings(
                    username = emailState.value,
                    password = inputState.value.password
                )
            )

            statusState.update {
                Status(
                    isProcessing = false,
                    isSuccess = true
                )
            }
        }
    }

    fun updatePassword(value: String?) {
        savedStateHandle[StateKeys.Input.name] = inputState.value.copy(password = value)
    }

    fun updatePasswordConfirmation(value: String?) {
        savedStateHandle[StateKeys.Input.name] = inputState.value.copy(passwordConfirmation = value)
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

    internal data class Status(
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false
    )

    @Parcelize
    internal data class Input(
        val password: String? = null,
        val passwordConfirmation: String? = null
    ) : Parcelable

    private fun combine(
        email: String?,
        input: Input,
        status: Status?,
        passwordValidationResult: ValidatePasswordUseCase.Result
    ): UiState {
        val validationError = passwordValidationResult as? ValidatePasswordUseCase.Result.Failure

        return UiState(
            email = email.orEmpty(),
            password = input.password.orEmpty(),
            passwordConfirmation = input.passwordConfirmation.orEmpty(),
            canProceed = passwordValidationResult is ValidatePasswordUseCase.Result.Success,
            passwordError = validationError?.passwordError,
            passwordConfirmationError = validationError?.passwordConfirmationError,
            isProcessing = status?.isProcessing ?: false,
            isSuccess = status?.isSuccess ?: false
        )
    }

    private enum class StateKeys {
        Input
    }

}
