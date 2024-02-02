package com.rbrauwers.newsapp.resetpassword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun update(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    data class UiState(val email: String = "")

}