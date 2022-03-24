package com.loyaltiez.core.broadcast_receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.loyaltiez.core.R
import com.loyaltiez.core.helper.converters.convertStringToTime
import com.loyaltiez.core.services.AlarmService
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val bundle: Bundle = intent.extras!!
        val title = bundle.getString("title")!!
        val description = bundle.getString("description")!!
        val time = bundle.getString("time")!!
        val id = bundle.getInt("id")
        val type = bundle.getString("type")!!

        showNotification(context, title, description, time, id)
        setNextAlarm(time, type, intent, id, context)
    }

    private fun showNotification(
        context: Context,
        title: String,
        description: String,
        time: String,
        id: Int
    ) {

        // Build the notification
        val builder = context.let {
            NotificationCompat.Builder(it, "ID1")
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setContentTitle(title)
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
            set(Calendar.HOUR_OF_DAY, timeOfDay.hours)
            set(Calendar.MINUTE, timeOfDay.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var untilNext: Long = TimeUnit.DAYS.toMillis(1)

        if (type == "weekly") {
            untilNext = TimeUnit.DAYS.toMillis(7)
        }

        alarmService.setAlarm(
            calendar.timeInMillis + untilNext,
            getPendingIntent(intent, context, id)
        )
    }

    private fun getPendingIntent(intent: Intent, context: Context, requestCode: Int) =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

}