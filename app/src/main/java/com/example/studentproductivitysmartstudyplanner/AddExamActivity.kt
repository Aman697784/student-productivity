package com.example.studentproductivitysmartstudyplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddExamActivity : AppCompatActivity() {

    private lateinit var spinnerSubject: Spinner
    private lateinit var edtExamTitle: EditText
    private lateinit var edtExamLocation: EditText

    private lateinit var btnExamDate: Button
    private lateinit var btnExamTime: Button
    private lateinit var btnSaveExam: Button

    private lateinit var databaseHelper: DatabaseHelperExam

    private var examDate = ""
    private var examTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_add_exam
        )

        spinnerSubject =
            findViewById(R.id.spinnerExamSubject)

        edtExamTitle =
            findViewById(R.id.edtExamTitle)

        edtExamLocation =
            findViewById(R.id.edtExamLocation)

        btnExamDate =
            findViewById(R.id.btnExamDate)

        btnExamTime =
            findViewById(R.id.btnExamTime)

        btnSaveExam =
            findViewById(R.id.btnSaveExam)

        databaseHelper =
            DatabaseHelperExam(this)

        setupSubjectSpinner()

        btnExamDate.setOnClickListener {
            selectExamDate()
        }

        btnExamTime.setOnClickListener {
            selectExamTime()
        }

        btnSaveExam.setOnClickListener {
            saveExam()
        }
    }

    // -----------------------------
    // SUBJECT SPINNER
    // -----------------------------

    private fun setupSubjectSpinner() {

        val subjects = arrayOf(
            "Operating Systems",
            "Computer Networks",
            "Cryptography",
            "Mobile App Development",
            "Machine Learning",
            "DAA",
            "Other"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            subjects
        )

        spinnerSubject.adapter = adapter
    }

    // -----------------------------
    // DATE PICKER
    // -----------------------------

    private fun selectExamDate() {

        val calendar =
            Calendar.getInstance()

        val year =
            calendar.get(Calendar.YEAR)

        val month =
            calendar.get(Calendar.MONTH)

        val day =
            calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                examDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                btnExamDate.text =
                    "📅 $selectedDay/${selectedMonth + 1}/$selectedYear"
            },
            year,
            month,
            day
        )

        // Don't allow selecting past dates
        dialog.datePicker.minDate =
            System.currentTimeMillis()

        dialog.show()
    }

    // -----------------------------
    // TIME PICKER
    // -----------------------------

    private fun selectExamTime() {

        val calendar =
            Calendar.getInstance()

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minute =
            calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->

                examTime =
                    formatTime(
                        selectedHour,
                        selectedMinute
                    )

                btnExamTime.text =
                    "⏰ $examTime"
            },
            hour,
            minute,
            false
        )

        dialog.show()
    }

    // -----------------------------
    // FORMAT TIME
    // -----------------------------

    private fun formatTime(
        hour: Int,
        minute: Int
    ): String {

        val amPm =
            if (hour < 12) "AM"
            else "PM"

        val displayHour =
            when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }

        return String.format(
            "%02d:%02d %s",
            displayHour,
            minute,
            amPm
        )
    }

    // -----------------------------
    // SAVE EXAM
    // -----------------------------

    private fun saveExam() {

        val subject =
            spinnerSubject.selectedItem.toString()

        val title =
            edtExamTitle.text
                .toString()
                .trim()

        val location =
            edtExamLocation.text
                .toString()
                .trim()

        // TITLE VALIDATION

        if (title.isEmpty()) {

            edtExamTitle.error =
                "Please enter exam title"

            return
        }

        // DATE VALIDATION

        if (examDate.isEmpty()) {

            Toast.makeText(
                this,
                "Please select exam date",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // TIME VALIDATION

        if (examTime.isEmpty()) {

            Toast.makeText(
                this,
                "Please select exam time",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val exam = Exam(

            subject = subject,

            title = title,

            date = examDate,

            time = examTime,

            location = location
        )

        val success =
            databaseHelper.addExam(exam)

        if (success) {

            Toast.makeText(
                this,
                "Exam Added Successfully 📚",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Error saving exam",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}