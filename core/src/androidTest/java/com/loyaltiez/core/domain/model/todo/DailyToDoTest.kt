package com.loyaltiez.core.domain.model.todo

import com.loyaltiez.core.R
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.sql.Time

class DailyToDoTest {

    private lateinit var dailyToDo: DailyToDo
    @Before
    fun setUp() {

        dailyToDo = DailyToDo(
            "example@example.com",
            "Some Title",
            "Some Description",
            R.color.tinDoBlue,
            Time(16, 0, 0)
        )
    }

    @Test
    fun testToDoType(){

        assertEquals(DailyToDo.getTypeString(), "daily")
    }

    @Test
    fun testGetTimeString(){

        assertEquals(dailyToDo.getTimeString(), "16:00")
    }

    @Test
    fun testGetDateString(){
        assertEquals(dailyToDo.getDateString(), "")
    }
}