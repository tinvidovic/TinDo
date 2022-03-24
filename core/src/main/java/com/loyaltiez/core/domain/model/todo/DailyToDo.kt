package com.loyaltiez.core.domain.model.todo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Time

@Parcelize
class DailyToDo(
    override val userEmail: String,
    override val title: String,
    override val description: String,
    override val color: Int,
    override val time: Time,
    override var id: Int? = null
) : ToDo(userEmail, title, description, color, time, null, id), Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}