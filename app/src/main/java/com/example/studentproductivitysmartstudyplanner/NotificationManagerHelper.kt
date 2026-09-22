package com.example.studentproductivitysmartstudyplanner

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NotificationManagerHelper {

    private const val PREFS_NAME =
        "NotificationPrefs"

    private const val NOTIFICATIONS_ENABLED =
        "notifications_enabled"

    fun setNotificationsEnabled(
        context: Context,
        enabled: Boolean
    ) {

        val prefs =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        prefs.edit()
            .putBoolean(
                NOTIFICATIONS_ENABLED,
                enabled
            )
            .apply()
    }

    fun areNotificationsEnabled(
        context: Context
    ): Boolean {

        return context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).getBoolean(
            NOTIFICATIONS_ENABLED,
            true
        )
    }

    fun scheduleNotification(
        context: Context,
        schedule: StudySchedule
    ) {

        if (!areNotificationsEnabled(context)) {
            return
        }

        try {

            val timeFormat =
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.ENGLISH
                )

            val selectedTime =
                timeFormat.parse(schedule.startTime)
                    ?: return

            val timeCalendar =
                Calendar.getInstance()

            timeCalendar.time = selectedTime

            val target =
                Calendar.getInstance()

            target.set(
                Calendar.HOUR_OF_DAY,
                timeCalendar.get(Calendar.HOUR_OF_DAY)
            )

            target.set(
                Calendar.MINUTE,
                timeCalendar.get(Calendar.MINUTE)
            )

            target.set(
                Calendar.SECOND,
                0
            )

            target.set(
                Calendar.MILLISECOND,
                0
            )

            val targetDay =
                getDayNumber(schedule.day)

            val currentDay =
                target.get(Calendar.DAY_OF_WEEK)

            var daysUntil =
                targetDay - currentDay

            if (daysUntil < 0) {
                daysUntil += 7
            }

            target.add(
                Calendar.DAY_OF_YEAR,
                daysUntil
            )

            // Reminder 10 minutes before study session

            target.add(
                Calendar.MINUTE,
                -10
            )

            // If calculated time already passed,
            // schedule it for next week.

            if (
                target.timeInMillis <=
                System.currentTimeMillis()
            ) {

                target.add(
                    Calendar.DAY_OF_YEAR,
                    7
                )
            }

            val intent =
                Intent(
                    context,
                    NotificationReceiver::class.java
                )

            intent.putExtra(
                "subject",
                schedule.subject
            )

            intent.putExtra(
                "topic",
                schedule.topic
            )

            val requestCode =
                schedule.id + 1000

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            // Repeat every 7 days

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    fun cancelNotification(
        context: Context,
        scheduleId: Int
    ) {

        val intent =
            Intent(
                context,
                NotificationReceiver::class.java
            )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                scheduleId + 1000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        alarmManager.cancel(
            pendingIntent
        )

        pendingIntent.cancel()
    }

    private fun getDayNumber(
        day: String
    ): Int {

        return when (
            day.lowercase(Locale.ENGLISH)
        ) {

            "sunday" -> Calendar.SUNDAY
            "monday" -> Calendar.MONDAY
            "tuesday" -> Calendar.TUESDAY
            "wednesday" -> Calendar.WEDNESDAY
            "thursday" -> Calendar.THURSDAY
            "friday" -> Calendar.FRIDAY
            "saturday" -> Calendar.SATURDAY

            else -> Calendar.MONDAY
        }
    }
}