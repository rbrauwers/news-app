package com.rbrauwers.newsapp

import app.cash.turbine.test
import com.rbrauwers.newsapp.resetpassword.ResetPasswordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ResetPasswordViewModelTest {

    private val fakeUserSettingsRepository = FakeUserSettingsRepository()
    private lateinit var viewModel: ResetPasswordViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ResetPasswordViewModel(userSettingsRepository = fakeUserSettingsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun typedEmailShouldBeCollected() = runTest {
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.uiState.test {
            // Skip initial value
            skipItems(1)
            val email = "some@email.com"
            viewModel.update(email)
            Assert.assertEquals(email, awaitItem().email)
            job.cancel()
        }
    }

    @Test
    fun correctEmailShouldEnablePasswordReset() {
        val email = "some@email.com"
        fakeUserSettingsRepository.emit(email)
        viewModel.update(email)
        Assert.assertTrue(viewModel.uiState.isResetPasswordEnabled)
    }

}

