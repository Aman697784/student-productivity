package com.example.studentproductivitysmartstudyplanner

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText

    private lateinit var spinnerSubject: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerPriority: Spinner

    private lateinit var btnDate: Button
    private lateinit var btnSave: Button

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_task)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)

        spinnerSubject = findViewById(R.id.spinnerSubject)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerPriority = findViewById(R.id.spinnerPriority)

        btnDate = findViewById(R.id.btnDate)
        btnSave = findViewById(R.id.btnSave)

        databaseHelper = DatabaseHelper(this)

        setupSpinners()

        btnDate.setOnClickListener {

            showDatePicker()

        }

        btnSave.setOnClickListener {

            saveTask()

        }
    }

    private fun setupSpinners() {

        val subjects = arrayOf(
            "Operating Systems",
            "Cryptography & Network Security",
            "Machine Learning",
            "Computer Architecture",
            "Mobile App Development",
            "Data Structures",
            "DAA",
            "Other"
        )

        val categories = arrayOf(
            "Assignment",
            "Practical",
            "Project",
            "Exam",
            "Study",
            "Personal",
            "Other"
        )

        val priorities = arrayOf(
            "High",
            "Medium",
            "Low"
        )

        spinnerSubject.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            subjects
        )

        spinnerCategory.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        spinnerPriority.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            priorities
        )
    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                selectedDate =
                    "$selectedDay/${selectedMonth + 1}/$selectedYear"

                btnDate.text = "📅 $selectedDate"

            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun saveTask() {

        val title =
            edtTitle.text.toString().trim()

        val description =
            edtDescription.text.toString().trim()

        val subject =
            spinnerSubject.selectedItem.toString()

        val category =
            spinnerCategory.selectedItem.toString()

        val priority =
            spinnerPriority.selectedItem.toString()

        if (title.isEmpty()) {

            edtTitle.error =
                "Please enter task title"

            return
        }

        if (selectedDate.isEmpty()) {

            Toast.makeText(
                this,
                "Please select due date",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val task = Task(

            title = title,

            description = description,

            subject = subject,

            category = category,

            dueDate = selectedDate,

            priority = priority,

            status = "Pending"
        )

        val success =
            databaseHelper.addTask(task)

        if (success) {

            Toast.makeText(
                this,
                "Task Added Successfully 🎉",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Toast.makeText(
                this,
                "Error adding task",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
