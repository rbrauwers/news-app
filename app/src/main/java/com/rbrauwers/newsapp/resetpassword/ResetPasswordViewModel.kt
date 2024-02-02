package com.rbrauwers.newsapp.resetpassword

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ResetPasswordViewModel {

    val uiState = MutableStateFlow(UiState())

    fun update(email: String) {
        uiState.update {
            it.copy(email = email)
        }
    }

    data class UiState(val email: String = "")

}