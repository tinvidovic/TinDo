package com.loyaltiez.core.helper.converters

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

fun convertTimeToString(time : Time?) : String {

    return if (time == null)
        ""
    else {

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        format.format(time)
    }
}

fun convertDateToString(date : Date?) : String {

    return if (date == null)
        ""
    else {

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        format.format(date)
    }
}