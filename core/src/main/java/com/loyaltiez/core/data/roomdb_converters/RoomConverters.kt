package com.loyaltiez.core.data.roomdb_converters

import androidx.room.TypeConverter
import com.loyaltiez.core.helper.converters.convertDateToString
import com.loyaltiez.core.helper.converters.convertStringToDate
import com.loyaltiez.core.helper.converters.convertStringToTime
import com.loyaltiez.core.helper.converters.convertTimeToString
import java.sql.Date
import java.sql.Time

// The converters used when storing Date and Time objects to the database
// The functions convert Date and Time to strings and vice-versa
class RoomConverters {

    @TypeConverter
    fun fromTime(time: Time): String {
        return convertTimeToString(time)
    }

    @TypeConverter
    fun toTime(time: String): Time {

        return convertStringToTime(time)
    }

    @TypeConverter
    fun fromDate(date: Date?): String {
        return convertDateToString(date)
    }

    @TypeConverter
    fun toDate(date: String): Date? {

        return convertStringToDate(date)
    }
}