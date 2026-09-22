package com.example.studentproductivitysmartstudyplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val subject =
            intent.getStringExtra("subject")
                ?: "Study Session"

        val topic =
            intent.getStringExtra("topic")
                ?: "Study Time"

        NotificationHelper.showStudyReminder(
            context,
            subject,
            topic
        )
    }
}