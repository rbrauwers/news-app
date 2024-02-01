package com.rbrauwers.newsapp.headline

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeadlineScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun authorShouldBeDisplayed() {
        composeTestRule.setContent {
            Headline(
                article = article,
                isFirst = true,
                isLast = false,
                onLikedChanged = { _, _ -> })
        }

        composeTestRule
            .onNodeWithText(text = article.author ?: "")
            .assertExists()
    }

}