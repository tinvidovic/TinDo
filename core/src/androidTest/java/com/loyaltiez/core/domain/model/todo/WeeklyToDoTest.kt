package com.loyaltiez.core.domain.model.todo

import com.loyaltiez.core.R
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.sql.Date
import java.sql.Time

class WeeklyToDoTest {

    private lateinit var weeklyToDo: WeeklyToDo
    @Before
    fun setUp() {

        weeklyToDo = WeeklyToDo(
            "example@example.com",
            "Some Title",
            "Some Description",
            R.color.tinDoRed,
            Time(17, 34, 54),
            Date(122, 0, 20)
        )
    }

    @Test
    fun testToDoType(){

        assertEquals(WeeklyToDo.getTypeString(), "weekly")
    }

    @Test
    fun testGetTimeString(){

        assertEquals(weeklyToDo.getTimeString(), "17:34")
    }

    @Test
    fun testGetDateString(){
        assertEquals(weeklyToDo.getDateString(), "20/01/2022")
    }
}