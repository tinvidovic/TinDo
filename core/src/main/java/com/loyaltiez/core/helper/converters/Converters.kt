@file:Suppress("DEPRECATION")

package com.loyaltiez.core.helper.converters

import com.loyaltiez.core.helper.converters.ConverterConstants.MONTH_OFFSET
import com.loyaltiez.core.helper.converters.ConverterConstants.YEAR_OFFSET
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

/*
The individual converter functions which take a nullable Time or Date object and
convert it to String and vice-versa, based on the specified format
 */
fun convertTimeToString(time: Time?): String {

    return if (time == null)
        ""
    else {

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        format.format(time)
    }
}

fun convertStringToTime(string: String): Time {

    val hoursAndMinutes: List<String> = string.split(":")

    return Time(hoursAndMinutes[0].toInt(), hoursAndMinutes[1].toInt(), 0)
}

fun convertDateToString(date: Date?): String {

    return if (date == null)
        ""
    else {

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        format.format(date)
    }
}

fun convertStringToDate(string: String?): Date? {

    return if (string != null && string != "") {

        val dayMonthYear: List<String> = string.split("/").asReversed()

        Date(
            dayMonthYear[0].toInt() - YEAR_OFFSET,
            dayMonthYear[1].toInt() - MONTH_OFFSET,
            dayMonthYear[2].toInt()
        )

    } else {
        null
    }
}