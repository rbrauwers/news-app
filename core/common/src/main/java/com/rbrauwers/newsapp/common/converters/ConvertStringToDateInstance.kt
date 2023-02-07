package com.rbrauwers.newsapp.common.converters

import java.text.SimpleDateFormat

/**
 * Converts a string to formatted date, using default date format.
 */
class ConvertStringToDateInstance(
    private val convertStringToDate: ConvertStringToDate = ConvertStringToDate(),
    private val style: Int = SimpleDateFormat.SHORT
) {

    operator fun invoke(string: String?): String? {
        val date = convertStringToDate(string) ?: return null
        return SimpleDateFormat.getDateInstance(style).format(date)
    }

}
