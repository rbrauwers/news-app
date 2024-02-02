package com.rbrauwers.newsapp.resetpassword

import kotlinx.coroutines.flow.MutableStateFlow

class ResetPasswordViewModel {

    val uiState = MutableStateFlow(UiState())

    fun update(email: String) {

    }

    data class UiState(val email: String = "")

}