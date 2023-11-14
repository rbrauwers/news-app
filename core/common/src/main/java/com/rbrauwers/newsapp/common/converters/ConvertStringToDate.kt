package com.rbrauwers.newsapp.common.converters

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Converts a string to date, using the default format of News API.
 */
class ConvertStringToDate {

    operator fun invoke(string: String?): Date? {
        string ?: return null
        val formatter = SimpleDateFormat(FORMAT, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }

        return runCatching {
            formatter.parse(string)
        }.getOrNull()
    }

    companion object {
        private const val FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }

}