package com.rbrauwers.newsapp

import org.junit.Assert
import org.junit.Test

class ResetPasswordTests {

    private val email = "some@email.com"

    @Test
    fun correctEmailShouldPass() {
        Assert.assertEquals(email, getTypedEmail())
    }

    private fun getTypedEmail(): String {
        return email
    }

}