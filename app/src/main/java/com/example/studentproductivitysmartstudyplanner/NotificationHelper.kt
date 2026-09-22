package com.example.studentproductivitysmartstudyplanner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {

    private const val CHANNEL_ID = "study_reminder_channel"

    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Study Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description =
                "Notifications for upcoming study sessions"

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    fun showStudyReminder(
        context: Context,
        subject: String,
        topic: String
    ) {

        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🔔 Study Session Starting Soon!")
            .setContentText("$subject • $topic")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Your study session is starting soon.\n\n" +
                                "📚 Subject: $subject\n" +
                                "📖 Topic: $topic\n\n" +
                                "Stay focused and make progress! 💪"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}