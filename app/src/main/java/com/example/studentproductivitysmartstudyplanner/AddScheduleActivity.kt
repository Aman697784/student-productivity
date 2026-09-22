package com.example.studentproductivitysmartstudyplanner

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var spinnerSubject: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var edtTopic: EditText
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnSaveSchedule: Button

    private lateinit var databaseHelper: DatabaseHelperSchedule

    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        spinnerSubject = findViewById(R.id.spinnerSubject)
        spinnerDay = findViewById(R.id.spinnerDay)
        edtTopic = findViewById(R.id.edtTopic)
        btnStartTime = findViewById(R.id.btnStartTime)
        btnEndTime = findViewById(R.id.btnEndTime)
        btnSaveSchedule = findViewById(R.id.btnSaveSchedule)

        databaseHelper = DatabaseHelperSchedule(this)

        setupSpinners()

        btnStartTime.setOnClickListener {
            selectStartTime()
        }

        btnEndTime.setOnClickListener {
            selectEndTime()
        }

        btnSaveSchedule.setOnClickListener {
            saveSchedule()
        }
    }

    private fun setupSpinners() {
        val subjects = arrayOf(
            "Operating Systems",
            "Computer Networks",
            "Cryptography",
            "Mobile App Development",
            "Machine Learning",
            "DAA",
            "Other"
        )
        val subjectAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subjects)
        spinnerSubject.adapter = subjectAdapter

        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        spinnerDay.adapter = dayAdapter
    }

    private fun selectStartTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            startTime = formatTime(selectedHour, selectedMinute)
            btnStartTime.text = "⏰ Start: $startTime"
        }, hour, minute, false)
        dialog.show()
    }

    private fun selectEndTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            endTime = formatTime(selectedHour, selectedMinute)
            btnEndTime.text = "⏰ End: $endTime"
        }, hour, minute, false)
        dialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return String.format("%02d:%02d %s", displayHour, minute, amPm)
    }

    private fun saveSchedule() {
        val subject = spinnerSubject.selectedItem.toString()
        val topic = edtTopic.text.toString().trim()
        val day = spinnerDay.selectedItem.toString()

        if (topic.isEmpty()) {
            edtTopic.error = "Please enter study topic"
            return
        }
        if (startTime.isEmpty()) {
            Toast.makeText(this, "Please select start time", Toast.LENGTH_SHORT).show()
            return
        }
        if (endTime.isEmpty()) {
            Toast.makeText(this, "Please select end time", Toast.LENGTH_SHORT).show()
            return
        }

        val schedule = StudySchedule(
            subject = subject,
            topic = topic,
            day = day,
            startTime = startTime,
            endTime = endTime
        )

        val success = databaseHelper.addSchedule(schedule)

        if (success) {
            val schedules = databaseHelper.getAllSchedules()
            val savedSchedule = schedules.firstOrNull {
                it.subject == schedule.subject &&
                        it.topic == schedule.topic &&
                        it.day == schedule.day &&
                        it.startTime == schedule.startTime &&
                        it.endTime == schedule.endTime
            }

            if (savedSchedule != null) {
                NotificationManagerHelper.scheduleNotification(this, savedSchedule)
            }

            Toast.makeText(this, "Study Session Added 📚", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error saving schedule", Toast.LENGTH_SHORT).show()
        }
    }
}
