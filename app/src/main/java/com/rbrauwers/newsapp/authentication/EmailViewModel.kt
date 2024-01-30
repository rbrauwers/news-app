package com.rbrauwers.newsapp.authentication

import android.os.Parcelable
import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
internal class EmailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val emailState: StateFlow<String?> =
        savedStateHandle.getStateFlow(emailStateKey, null)

    val uiState: StateFlow<UiState> =
        emailState.map { email ->
            email.toUiState()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState()
        )

    fun update(email: String?) {
        savedStateHandle[emailStateKey] = email
    }

    private fun String?.toUiState(): UiState {
        val email = this
        val isValid = email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val error = if (isValid || email.isNullOrBlank()) null else "Invalid email"

        return UiState(
            email = email,
            error = error,
            canProceed = isValid
        )
    }

    @Parcelize
    data class UiState(
        val email: String? = null,
        val error: String? = null,
        val canProceed: Boolean = false
    ) : Parcelable

    companion object {
        internal const val emailStateKey = "emailState"
    }

}
