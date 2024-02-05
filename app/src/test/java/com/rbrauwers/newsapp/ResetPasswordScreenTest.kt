package com.rbrauwers.newsapp

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performTextInput
import com.rbrauwers.newsapp.model.UserSettings
import com.rbrauwers.newsapp.resetpassword.ResetPasswordRoute
import com.rbrauwers.newsapp.resetpassword.ResetPasswordViewModel
import com.rbrauwers.newsapp.tests.FakeUserSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ResetPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeUserSettingsRepository = FakeUserSettingsRepository()
    private lateinit var viewModel: ResetPasswordViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ResetPasswordViewModel(userSettingsRepository = fakeUserSettingsRepository)

        composeTestRule.setContent {
            ResetPasswordRoute(
                onBackClick = { },
                viewModel = viewModel
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun incorrectEmailShouldDisableResetPasswordButton() = runTest {
        val email = "some@email.com"
        val other = "other@email.com"

        fakeUserSettingsRepository.save(UserSettings(username = email))

        composeTestRule.onNodeWithTag("emailField")
            .performTextInput(other)

        advanceUntilIdle()

        composeTestRule.onNodeWithTag("resetPasswordButton")
            .assertIsNotEnabled()
    }

    @Test
    fun correctEmailShouldEnableResetPasswordButton() = runTest {
        val email = "some@email.com"
        fakeUserSettingsRepository.save(UserSettings(username = email))

        composeTestRule.onNodeWithTag("emailField")
            .performTextInput(email)

        advanceUntilIdle()

        composeTestRule.onNodeWithTag("resetPasswordButton")
            .assertIsEnabled()
    }

}