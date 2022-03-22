package com.loyaltiez.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Time

@Parcelize
class DailyToDo(
    override val title: String,
    override val description: String,
    override val color: Int,
    override val time: Time
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

        return "Daily"
    }
}