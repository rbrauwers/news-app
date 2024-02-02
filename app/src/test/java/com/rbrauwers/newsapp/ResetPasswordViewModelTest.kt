package com.rbrauwers.newsapp

import com.rbrauwers.newsapp.resetpassword.ResetPasswordViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ResetPasswordViewModelTest {

    private lateinit var viewModel: ResetPasswordViewModel

    @Before
    fun setUp() {
        viewModel = ResetPasswordViewModel()
    }

    @Test
    fun typedEmailShouldBeCollected() {
        val email = "some@email.com"
        viewModel.update(email)
        Assert.assertEquals(email, viewModel.uiState.value.email)
    }

}

