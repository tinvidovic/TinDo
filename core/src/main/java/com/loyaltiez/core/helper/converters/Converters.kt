package com.loyaltiez.core.helper.converters

import java.sql.Time
import java.text.SimpleDateFormat
import java.sql.Date
import java.util.*

fun convertTimeToString(time : Time?) : String {

    return if (time == null)
        ""
    else {

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        format.format(time)
    }
}

fun convertStringToTime(string : String) : Time {

    val hoursAndMinutes : List<String> = string.split(":")

    return Time(hoursAndMinutes[0].toInt(), hoursAndMinutes[1].toInt(), 0)
}

fun convertDateToString(date : Date?) : String {

    return if (date == null)
        ""
    else {

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        format.format(date)
    }
}

fun convertStringToDate(string: String?) : Date? {

    if (string != null && string != ""){

        val dayMonthYear : List<String> = string.split("/").asReversed()

        return Date(dayMonthYear[0].toInt() - 1900, dayMonthYear[1].toInt() - 1, dayMonthYear[2].toInt())

    } else {
        return null
    }
}