package com.loyaltiez.core.broadcast_receivers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.loyaltiez.core.R
import com.loyaltiez.core.broadcast_receivers.AlarmReceiverConstants.EPSILON_MILLIS
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.helper.converters.convertStringToDate
import com.loyaltiez.core.helper.converters.convertStringToTime
import com.loyaltiez.core.services.AlarmService
import java.util.*
import java.util.concurrent.TimeUnit

/*
The alarm receiver class, concerned with receiving the events passed by the alarms set on the tindo's
@extends: BroadcastReceiver
 */
@Suppress("DEPRECATION")
class AlarmReceiver : BroadcastReceiver() {

    // The onReceive function, called everytime an alarm of a tindo is triggered
    override fun onReceive(context: Context, intent: Intent) {

        // Get the extras passed in with the intent
        val bundle: Bundle = intent.extras!!
        val title = bundle.getString("title")!!
        val description = bundle.getString("description")!!
        val time = bundle.getString("time")!!
        val date = bundle.getString("date")
        val id = bundle.getInt("id")
        val type = bundle.getString("type")!!

        if (shouldShowNotification(time, date)) {
            showNotification(context, title, description, time, id)
        }
        setNextAlarm(time, type, intent, id, context)
    }

    // Builds and displays a notification based on the passed in parameters
    private fun showNotification(
        context: Context,
        title: String,
        description: String,
        time: String,
        id: Int
    ) {

        // Build the notification
        val builder = context.let {
            NotificationCompat.Builder(it, context.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setContentTitle("$time - $title")
                .setContentText(description)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(description)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        }

        // Show the notification
        context.let { NotificationManagerCompat.from(it) }.notify(id, builder.build())

    }

    // Check if the notification should be shown, to avoid showing notifications with an old date immediately
    private fun shouldShowNotification(time: String, date: String?): Boolean {

        val currentTimeInMillis = System.currentTimeMillis()

        val currentTime = Date(currentTimeInMillis)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = convertStringToDate(date)?.time ?: System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, convertStringToTime(time).hours)
            set(Calendar.MINUTE, convertStringToTime(time).minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // show the notification if the tindo time of day is not earlier than the current time of day
        // (minus some epsilon to account for system clock ticks required for execution time)
        return calendar.timeInMillis >= currentTime.time - EPSILON_MILLIS
    }

    // Set the next alarm for daily and weekly notifications
    private fun setNextAlarm(
        time: String,
        type: String,
        intent: Intent,
        id: Int,
        context: Context
    ) {

        val alarmService = AlarmService(context)

        val timeOfDay = convertStringToTime(time)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, timeOfDay.hours)
            set(Calendar.MINUTE, timeOfDay.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var untilNext: Long = TimeUnit.DAYS.toMillis(1)

        if (type == WeeklyToDo.getTypeString()) {
            untilNext = TimeUnit.DAYS.toMillis(7)
        }

        alarmService.setAlarm(
            calendar.timeInMillis + untilNext,
            getPendingIntent(intent, context, id),
            id
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(intent: Intent, context: Context, requestCode: Int) =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

}