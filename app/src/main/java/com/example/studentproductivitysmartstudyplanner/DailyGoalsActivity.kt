package com.example.studentproductivitysmartstudyplanner

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyGoalsActivity : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtStreak: TextView
    private lateinit var txtStreakMessage: TextView

    private lateinit var txtTaskGoal: TextView
    private lateinit var taskProgress: ProgressBar

    private lateinit var txtStudyGoal: TextView
    private lateinit var studyProgress: ProgressBar

    private lateinit var txtGoalStatus: TextView
    private lateinit var txtGoalDescription: TextView

    private lateinit var btnDailyCheckIn: Button

    private lateinit var taskDatabase: DatabaseHelper
    private lateinit var scheduleDatabase: DatabaseHelperSchedule

    private val preferencesName = "ProductivityPrefs"

    private val taskGoal = 5
    private val studyGoalMinutes = 180

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_daily_goals)

        // FIND VIEWS

        txtDate = findViewById(R.id.txtDate)

        txtStreak = findViewById(R.id.txtStreak)

        txtStreakMessage =
            findViewById(R.id.txtStreakMessage)

        txtTaskGoal =
            findViewById(R.id.txtTaskGoal)

        taskProgress =
            findViewById(R.id.taskProgress)

        txtStudyGoal =
            findViewById(R.id.txtStudyGoal)

        studyProgress =
            findViewById(R.id.studyProgress)

        txtGoalStatus =
            findViewById(R.id.txtGoalStatus)

        txtGoalDescription =
            findViewById(R.id.txtGoalDescription)

        btnDailyCheckIn =
            findViewById(R.id.btnDailyCheckIn)

        // DATABASES

        taskDatabase =
            DatabaseHelper(this)

        scheduleDatabase =
            DatabaseHelperSchedule(this)

        showDate()

        loadDailyProgress()

        updateStreakUI()

        btnDailyCheckIn.setOnClickListener {

            completeDailyCheckIn()
        }
    }

    override fun onResume() {

        super.onResume()

        loadDailyProgress()

        updateStreakUI()
    }

    // --------------------------------
    // SHOW TODAY'S DATE
    // --------------------------------

    private fun showDate() {

        val format =
            SimpleDateFormat(
                "EEEE, dd MMMM yyyy",
                Locale.ENGLISH
            )

        txtDate.text =
            format.format(Calendar.getInstance().time)
    }

    // --------------------------------
    // DAILY PROGRESS
    // --------------------------------

    private fun loadDailyProgress() {

        val tasks =
            taskDatabase.getAllTasks()

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

        val schedules =
            scheduleDatabase.getAllSchedules()

        var studyMinutes = 0

        for (schedule in schedules) {

            studyMinutes +=
                calculateDuration(
                    schedule.startTime,
                    schedule.endTime
                )
        }

        // TASK

        val taskDisplay =
            minOf(completedTasks, taskGoal)

        txtTaskGoal.text =
            "$taskDisplay / $taskGoal tasks completed"

        taskProgress.progress =
            taskDisplay

        // STUDY

        val studyDisplay =
            minOf(studyMinutes, studyGoalMinutes)

        val studyHours =
            studyDisplay / 60.0

        txtStudyGoal.text =
            String.format(
                Locale.ENGLISH,
                "%.1f / 3.0 hours",
                studyHours
            )

        studyProgress.progress =
            studyDisplay

        // OVERALL GOAL

        val taskCompleted =
            completedTasks >= taskGoal

        val studyCompleted =
            studyMinutes >= studyGoalMinutes

        if (taskCompleted && studyCompleted) {

            txtGoalStatus.text =
                "🎉 Daily Goal Completed!"

            txtGoalDescription.text =
                "Amazing! You completed today's task and study goals. 🔥"

        } else if (taskCompleted) {

            txtGoalStatus.text =
                "📋 Task Goal Completed!"

            txtGoalDescription.text =
                "Great! Now complete your study goal."

        } else if (studyCompleted) {

            txtGoalStatus.text =
                "📚 Study Goal Completed!"

            txtGoalDescription.text =
                "Excellent! Now complete your task goal."

        } else {

            txtGoalStatus.text =
                "🎯 Keep Going!"

            txtGoalDescription.text =
                "Complete your daily goals and build your streak."
        }
    }

    // --------------------------------
    // CALCULATE STUDY TIME
    // --------------------------------

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
                            (1000 * 60))
                        .toInt()

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

    // --------------------------------
    // DAILY CHECK-IN
    // --------------------------------

    private fun completeDailyCheckIn() {

        val prefs =
            getSharedPreferences(
                preferencesName,
                Context.MODE_PRIVATE
            )

        val today =
            getTodayKey()

        val lastCheckIn =
            prefs.getString(
                "lastCheckIn",
                ""
            )

        if (lastCheckIn == today) {

            Toast.makeText(
                this,
                "Today's check-in is already completed 🔥",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val yesterday =
            getYesterdayKey()

        val oldStreak =
            prefs.getInt(
                "streak",
                0
            )

        val newStreak =
            if (lastCheckIn == yesterday) {

                oldStreak + 1

            } else {

                1
            }

        prefs.edit()
            .putInt(
                "streak",
                newStreak
            )
            .putString(
                "lastCheckIn",
                today
            )
            .apply()

        updateStreakUI()

        Toast.makeText(
            this,
            "🔥 Streak updated! $newStreak days",
            Toast.LENGTH_SHORT
        ).show()
    }

    // --------------------------------
    // STREAK UI
    // --------------------------------

    private fun updateStreakUI() {

        val prefs =
            getSharedPreferences(
                preferencesName,
                Context.MODE_PRIVATE
            )

        val streak =
            prefs.getInt(
                "streak",
                0
            )

        txtStreak.text =
            "$streak Days"

        when {

            streak == 0 -> {

                txtStreakMessage.text =
                    "Start your productivity streak!"
            }

            streak == 1 -> {

                txtStreakMessage.text =
                    "Great start! Come back tomorrow. 💪"
            }

            streak < 7 -> {

                txtStreakMessage.text =
                    "🔥 Keep going! Build your streak."
            }

            streak < 30 -> {

                txtStreakMessage.text =
                    "🏆 Amazing! You're building a strong habit."
            }

            else -> {

                txtStreakMessage.text =
                    "👑 Incredible! You're a productivity master."
            }
        }

        val today =
            getTodayKey()

        val lastCheckIn =
            prefs.getString(
                "lastCheckIn",
                ""
            )

        if (lastCheckIn == today) {

            btnDailyCheckIn.text =
                "✅ Today's Check-in Completed"

        } else {

            btnDailyCheckIn.text =
                "🔥 Complete Daily Check-in"
        }
    }

    // --------------------------------
    // DATE KEY
    // --------------------------------

    private fun getTodayKey(): String {

        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.ENGLISH
        ).format(
            Calendar.getInstance().time
        )
    }

    private fun getYesterdayKey(): String {

        val calendar =
            Calendar.getInstance()

        calendar.add(
            Calendar.DAY_OF_YEAR,
            -1
        )

        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.ENGLISH
        ).format(
            calendar.time
        )
    }
}