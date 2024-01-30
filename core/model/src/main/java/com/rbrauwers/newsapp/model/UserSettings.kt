package com.rbrauwers.newsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val username: String? = null,
    val password: String? = null
) {
    companion object {
        val Empty = UserSettings()
    }
}

fun UserSettings?.isAuthenticated() : Boolean {
    return this != null && this != UserSettings.Empty
}