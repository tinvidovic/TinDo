package com.loyaltiez.core.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.content.edit
import com.loyaltiez.core.R

/*
The alarm service class, used for setting/removing the alarms and
updating the sharedPreferences, with the currently set alarm ids
 */
class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    // Sets the alarm at the exact specified time with the specified parameters
    // and saves the alarmId in the Shared preferences
    fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent, requestCode: Int) {
        alarmManager?.let {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent,
            )
        }

        saveAlarmId(requestCode)
    }

    // Removes the alarm with the specified parameters
    // and removes the alarmId in the Shared preferences
    fun removeAlarm(pendingIntent: PendingIntent, requestCode: Int) {

        alarmManager?.cancel(pendingIntent)

        removeAlarmId(requestCode)
    }

    // UTILITY FUNCTIONS:
    // Saves the passed in alarm id to the sharedPreferences
    private fun saveAlarmId(id: Int) {

        if (id !in getAlarmIds()) {

            val prefs = context.getSharedPreferences(
                context.getString(R.string.alarm_ids_file_key),
                Context.MODE_PRIVATE
            )

            prefs.edit {
                putInt(id.toString(), id)
                apply()
            }
        }
    }

    // Removoes the provided alarmId from the SharedPreferences
    private fun removeAlarmId(id: Int) {

        if (id in getAlarmIds()) {

            val prefs = context.getSharedPreferences(
                context.getString(R.string.alarm_ids_file_key),
                Context.MODE_PRIVATE
            )

            prefs.edit {
                remove(id.toString())
                apply()
            }
        }
    }

    // Returns all the alarmIds of currently set alarms from the sharedPreferences
    fun getAlarmIds(): MutableList<Int> {
        val ids: MutableList<Int> = mutableListOf()
        try {
            val prefs = context.getSharedPreferences(
                context.getString(R.string.alarm_ids_file_key),
                Context.MODE_PRIVATE
            )
            for (id in prefs.all)
                ids.add(id.value as Int)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ids
    }

}