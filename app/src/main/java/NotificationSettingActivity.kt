package com.example.studentproductivitysmartstudyplanner

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NotificationSettingsActivity :
    AppCompatActivity() {

    private lateinit var switchNotifications: Switch

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_notification_setting
        )

        switchNotifications =
            findViewById(
                R.id.switchNotifications
            )

        switchNotifications.isChecked =
            NotificationManagerHelper
                .areNotificationsEnabled(this)

        switchNotifications.setOnCheckedChangeListener {
                _, isChecked ->

            NotificationManagerHelper
                .setNotificationsEnabled(
                    this,
                    isChecked
                )

            if (isChecked) {

                val database =
                    DatabaseHelperSchedule(this)

                val schedules =
                    database.getAllSchedules()

                for (schedule in schedules) {

                    NotificationManagerHelper
                        .scheduleNotification(
                            this,
                            schedule
                        )
                }

                Toast.makeText(
                    this,
                    "Study reminders enabled 🔔",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Study reminders disabled 🔕",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}