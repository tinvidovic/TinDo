package com.loyaltiez.core.domain

import android.os.Parcelable
import com.loyaltiez.core.helper.converters.convertTimeToString
import java.sql.Time

abstract class ToDo(
    open val title: String,
    open val description: String,
    open val color: Int,
    open val time: Time
): Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    fun getTimeString(): String = convertTimeToString(time)

    abstract fun getToDoType(): String
}