package com.example.studentproductivitysmartstudyplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddSchedule: Button
    private lateinit var txtSelectedDay: TextView

    private lateinit var btnMonday: Button
    private lateinit var btnTuesday: Button
    private lateinit var btnWednesday: Button
    private lateinit var btnThursday: Button
    private lateinit var btnFriday: Button
    private lateinit var btnSaturday: Button
    private lateinit var btnSunday: Button

    private lateinit var databaseHelper: DatabaseHelperSchedule
    private lateinit var scheduleAdapter: ScheduleAdapter

    private var scheduleList = ArrayList<StudySchedule>()

    private var selectedDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_schedule)

        // Views

        recyclerView =
            findViewById(R.id.scheduleRecyclerView)

        btnAddSchedule =
            findViewById(R.id.btnAddSchedule)

        txtSelectedDay =
            findViewById(R.id.txtSelectedDay)

        btnMonday =
            findViewById(R.id.btnMonday)

        btnTuesday =
            findViewById(R.id.btnTuesday)

        btnWednesday =
            findViewById(R.id.btnWednesday)

        btnThursday =
            findViewById(R.id.btnThursday)

        btnFriday =
            findViewById(R.id.btnFriday)

        btnSaturday =
            findViewById(R.id.btnSaturday)

        btnSunday =
            findViewById(R.id.btnSunday)

        // Database

        databaseHelper =
            DatabaseHelperSchedule(this)

        // RecyclerView

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        scheduleAdapter = ScheduleAdapter(
            ArrayList(),
            selectedDay = selectedDay,
            onDelete = { schedule ->
                deleteSchedule(schedule)
            }
        )

        recyclerView.adapter =
            scheduleAdapter

        // Add button

        btnAddSchedule.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddScheduleActivity::class.java
                )

            startActivity(intent)
        }

        // Day buttons

        btnMonday.setOnClickListener {
            selectDay("Monday")
        }

        btnTuesday.setOnClickListener {
            selectDay("Tuesday")
        }

        btnWednesday.setOnClickListener {
            selectDay("Wednesday")
        }

        btnThursday.setOnClickListener {
            selectDay("Thursday")
        }

        btnFriday.setOnClickListener {
            selectDay("Friday")
        }

        btnSaturday.setOnClickListener {
            selectDay("Saturday")
        }

        btnSunday.setOnClickListener {
            selectDay("Sunday")
        }

        // Automatically select today's day

        selectedDay = getTodayDay()

        selectDay(selectedDay)
    }

    override fun onResume() {
        super.onResume()

        if (selectedDay.isNotEmpty()) {
            loadSchedulesForDay(selectedDay)
        }
        scheduleAllNotifications()
    }

    private fun getTodayDay(): String {

        val calendar = Calendar.getInstance()

        return SimpleDateFormat(
            "EEEE",
            Locale.ENGLISH
        ).format(calendar.time)
    }

    private fun selectDay(day: String) {

        selectedDay = day

        txtSelectedDay.text =
            "SCHEDULE — ${day.uppercase()}"

        loadSchedulesForDay(day)
    }

    private fun loadSchedulesForDay(day: String) {

        val allSchedules =
            databaseHelper.getAllSchedules()

        val filteredList =
            ArrayList(
                allSchedules.filter {
                    it.day.equals(
                        day,
                        ignoreCase = true
                    )
                }
            )

        scheduleAdapter.updateList(
            filteredList
        )
    }

    private fun scheduleAllNotifications() {
        val allSchedules = databaseHelper.getAllSchedules()
        for (schedule in allSchedules) {
            NotificationManagerHelper.scheduleNotification(this, schedule)
        }
    }

    private fun deleteSchedule(
        schedule: StudySchedule
    ) {

        AlertDialog.Builder(this)

            .setTitle("Delete Schedule")

            .setMessage(
                "Delete this study session?"
            )

            .setPositiveButton("Yes") { _, _ ->

                databaseHelper.deleteSchedule(
                    schedule.id
                )

                NotificationManagerHelper.cancelNotification(
                    this,
                    schedule.id
                )

                loadSchedulesForDay(
                    selectedDay
                )

                Toast.makeText(
                    this,
                    "Schedule Deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }

            .setNegativeButton(
                "No",
                null
            )

            .show()
    }
}