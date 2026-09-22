package com.example.studentproductivitysmartstudyplanner

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var txtTotalTasks: TextView
    private lateinit var txtCompletedTasks: TextView
    private lateinit var txtPendingTasks: TextView
    private lateinit var txtStudyHours: TextView

    private lateinit var txtProductivity: TextView
    private lateinit var productivityProgress: ProgressBar

    private lateinit var txtTaskOverview: TextView
    private lateinit var txtSessionOverview: TextView
    private lateinit var txtHoursOverview: TextView

    private lateinit var txtMotivation: TextView

    private lateinit var taskDatabase: DatabaseHelper
    private lateinit var scheduleDatabase: DatabaseHelperSchedule

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_analytics
        )

        // FIND VIEWS

        txtTotalTasks =
            findViewById(R.id.txtTotalTasks)

        txtCompletedTasks =
            findViewById(R.id.txtCompletedTasks)

        txtPendingTasks =
            findViewById(R.id.txtPendingTasks)

        txtStudyHours =
            findViewById(R.id.txtStudyHours)

        txtProductivity =
            findViewById(R.id.txtProductivity)

        productivityProgress =
            findViewById(R.id.productivityProgress)

        txtTaskOverview =
            findViewById(R.id.txtTaskOverview)

        txtSessionOverview =
            findViewById(R.id.txtSessionOverview)

        txtHoursOverview =
            findViewById(R.id.txtHoursOverview)

        txtMotivation =
            findViewById(R.id.txtMotivation)

        // DATABASES

        taskDatabase =
            DatabaseHelper(this)

        scheduleDatabase =
            DatabaseHelperSchedule(this)

        loadAnalytics()
    }

    override fun onResume() {

        super.onResume()

        loadAnalytics()
    }

    private fun loadAnalytics() {

        // -----------------------------
        // TASK ANALYTICS
        // -----------------------------

        val tasks =
            taskDatabase.getAllTasks()

        val totalTasks =
            tasks.size

        var completedTasks = 0

        for (task in tasks) {

            if (
                task.status.equals(
                    "Completed",
                    ignoreCase = true
                )
            ) {
                completedTasks++
            }
        }

        val pendingTasks =
            totalTasks - completedTasks

        // -----------------------------
        // STUDY SCHEDULE ANALYTICS
        // -----------------------------

        val schedules =
            scheduleDatabase.getAllSchedules()

        var totalStudyMinutes = 0

        for (schedule in schedules) {

            totalStudyMinutes +=
                calculateDuration(
                    schedule.startTime,
                    schedule.endTime
                )
        }

        val studyHours =
            totalStudyMinutes / 60.0

        // -----------------------------
        // PRODUCTIVITY %
        // -----------------------------

        val productivity: Int

        if (totalTasks == 0) {

            productivity = 0

        } else {

            productivity =
                ((completedTasks.toDouble()
                        / totalTasks.toDouble()) * 100)
                    .toInt()
        }

        // -----------------------------
        // UPDATE UI
        // -----------------------------

        txtTotalTasks.text =
            totalTasks.toString()

        txtCompletedTasks.text =
            completedTasks.toString()

        txtPendingTasks.text =
            pendingTasks.toString()

        txtStudyHours.text =
            formatHours(studyHours)

        txtProductivity.text =
            "$productivity%"

        productivityProgress.progress =
            productivity

        txtTaskOverview.text =
            "Tasks completed: $completedTasks"

        txtSessionOverview.text =
            "Study sessions: ${schedules.size}"

        txtHoursOverview.text =
            "Total study hours: ${formatHours(studyHours)}"

        updateMotivation(productivity)
    }

    // -----------------------------
    // CALCULATE STUDY DURATION
    // -----------------------------

    private fun calculateDuration(
        startTime: String,
        endTime: String
    ): Int {

        return try {

            val format =
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.ENGLISH
                )

            val start =
                format.parse(startTime)

            val end =
                format.parse(endTime)

            if (start != null && end != null) {

                val difference =
                    end.time - start.time

                val minutes =
                    (difference /
                            (1000 * 60)).toInt()

                if (minutes > 0) {
                    minutes
                } else {
                    0
                }

            } else {

                0
            }

        } catch (e: Exception) {

            0
        }
    }

    // -----------------------------
    // FORMAT HOURS
    // -----------------------------

    private fun formatHours(
        hours: Double
    ): String {

        return if (
            hours == hours.toInt().toDouble()
        ) {

            "${hours.toInt()}h"

        } else {

            String.format(
                Locale.ENGLISH,
                "%.1fh",
                hours
            )
        }
    }

    // -----------------------------
    // MOTIVATION
    // -----------------------------

    private fun updateMotivation(
        productivity: Int
    ) {

        when {

            productivity == 0 -> {

                txtMotivation.text =
                    "🚀 Start today! Your productivity journey begins now."
            }

            productivity < 30 -> {

                txtMotivation.text =
                    "💪 Good start! Keep completing your tasks."
            }

            productivity < 60 -> {

                txtMotivation.text =
                    "🔥 You're making progress! Keep going."
            }

            productivity < 80 -> {

                txtMotivation.text =
                    "⭐ Great work! You're becoming more productive."
            }

            productivity < 100 -> {

                txtMotivation.text =
                    "🏆 Excellent! You're almost at 100%."
            }

            else -> {

                txtMotivation.text =
                    "🎉 Perfect! Amazing productivity!"
            }
        }
    }
}