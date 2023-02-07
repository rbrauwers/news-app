package com.rbrauwers.newsapp.common.converters

import java.text.SimpleDateFormat

/**
 * Converts a string to formatted date, using default date time format.
 */
class ConvertStringToDateTimeInstance(
    private val convertStringToDate: ConvertStringToDate = ConvertStringToDate(),
    private val dateStyle: Int = SimpleDateFormat.SHORT,
    private val timeStyle: Int = SimpleDateFormat.SHORT
) {

    operator fun invoke(string: String?): String? {
        val date = convertStringToDate(string) ?: return null
        return SimpleDateFormat.getDateTimeInstance(dateStyle, timeStyle).format(date)
    }

}
