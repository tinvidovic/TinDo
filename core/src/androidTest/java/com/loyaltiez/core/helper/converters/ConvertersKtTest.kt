package com.loyaltiez.core.helper.converters

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.sql.Time
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class ConvertersKtTest {

    private lateinit var testTime1 : Time
    private lateinit var testTime2 : Time
    private lateinit var testTime1String : String
    private lateinit var testTime2String : String
    private lateinit var testTime3 : Time
    private lateinit var currentTimeString : String

    private lateinit var testDate1 : Date
    private lateinit var testDate2 : Date
    private lateinit var testDate1String : String
    private lateinit var testDate2String : String
    private lateinit var testDate3 : Date
    private lateinit var currentDateString : String

    @Before
    fun setUp() {

        testTime1 = Time(16, 54, 0)
        testTime1String = convertTimeToString(testTime1)
        testTime2 = Time(23, 0,0)
        testTime2String = convertTimeToString(testTime2)

        val currentTime = System.currentTimeMillis()
        currentTimeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime)
        testTime3 = Time(currentTime)

        testDate1 = Date(122, 0, 1)
        testDate1String = convertDateToString(testDate1)
        testDate2 = Date(122, 11,29)
        testDate2String = convertDateToString(testDate2)

        val currentDate = System.currentTimeMillis()
        currentDateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentDate)
        testDate3 = Date(currentTime)
    }

    @Test
    fun testConvertTimeToString(){

        assertEquals(convertTimeToString(testTime1), "16:54")
        assertEquals(convertTimeToString(testTime2), "23:00")


        assertEquals(convertTimeToString(testTime3), currentTimeString)
    }

    @Test
    fun testConvertStringToTime(){

        assertEquals(convertStringToTime("16:54"), testTime1)
        assertEquals(convertStringToTime("23:00"), testTime2)

        assertEquals(convertStringToTime(currentTimeString).hours, testTime3.hours)
        assertEquals(convertStringToTime(currentTimeString).minutes, testTime3.minutes)
        assertEquals(convertStringToTime(currentTimeString).seconds, 0)
    }

    @Test
    fun testConvertDateToString(){

        assertEquals(convertDateToString(testDate1), "01/01/2022")
        assertEquals(convertDateToString(testDate2), "29/12/2022")


        assertEquals(convertDateToString(testDate3), currentDateString)
    }

    @Test
    fun testConvertStringToDate(){

        assertEquals(convertStringToDate("01/01/2022"), testDate1)
        assertEquals(convertStringToDate("29/12/2022"), testDate2)

        assertEquals(convertStringToDate(currentDateString)!!.year, testDate3.year)
        assertEquals(convertStringToDate(currentDateString)!!.month, testDate3.month)
        assertEquals(convertStringToDate(currentDateString)!!.day, testDate3.day)
    }
}