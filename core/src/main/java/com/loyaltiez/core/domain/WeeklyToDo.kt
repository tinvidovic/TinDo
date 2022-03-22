package com.loyaltiez.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

@Parcelize
class WeeklyToDo(
    override val title: String,
    override val description: String,
    override val color: Int,
    override val time: Time,
    private val date: Date
) : ToDo(title, description, color, time), Parcelable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun getToDoType(): String {

        return "Weekly"
    }

    fun getDate() : Date = date

    fun getDateString(): String = date.toString()
}