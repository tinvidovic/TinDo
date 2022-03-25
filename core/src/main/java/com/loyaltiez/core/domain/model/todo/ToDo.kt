package com.loyaltiez.core.domain.model.todo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loyaltiez.core.helper.converters.convertDateToString
import com.loyaltiez.core.helper.converters.convertTimeToString
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

// Class representing a  To Do
//@extends Parcelable to be able to pass it via safeargs and jetpack navigation
// Defined as a room entity, stored in the table "to_dos"
@Entity(
    tableName = "to_dos"
)
@Parcelize
open class ToDo(
    open val userEmail: String,
    open val title: String,
    open val description: String,
    open val color: Int,
    open val time: Time,
    open var date: Date?,
    @PrimaryKey(autoGenerate = true)
    open var id: Int? = null,
    open var favourite: Boolean = false
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
    fun getTimeString(): String = convertTimeToString(time)

    fun getDateString(): String = convertDateToString(date)

}
