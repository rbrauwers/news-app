package com.rbrauwers.newsapp.headline

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rbrauwers.newsapp.common.converters.ConvertStringToDateTimeInstance
import com.rbrauwers.newsapp.tests.articlesList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeadlineScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun authorShouldBeDisplayed() {
        val articleUi = articlesList.first().toArticleUi(dateConverter = ConvertStringToDateTimeInstance())

        composeTestRule.setContent {
            Headline(
                article = articleUi,
                isFirst = true,
                isLast = false,
                onLikedChanged = { _, _ -> })
        }

        composeTestRule
            .onNodeWithText(text = articleUi.author ?: "")
            .assertExists()
    }

}