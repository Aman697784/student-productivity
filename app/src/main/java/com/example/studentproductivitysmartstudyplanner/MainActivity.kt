package com.example.studentproductivitysmartstudyplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddTask: Button
    private lateinit var btnFocusMode: Button

    private lateinit var btnStudySchedule: Button
    private lateinit var txtTotal: TextView
    private lateinit var txtPending: TextView

    private lateinit var edtSearch: EditText

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var taskAdapter: TaskAdapter

    private var taskList = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView)
        btnAddTask = findViewById(R.id.btnAddTask)
        btnFocusMode = findViewById(R.id.btnFocusMode)
        btnStudySchedule = findViewById(R.id.btnStudySchedule)
        val btnExamPlanner = findViewById<Button>(R.id.btnExamPlanner)
        val btnAnalytics = findViewById<Button>(R.id.btnAnalytics)
        txtTotal = findViewById(R.id.txtTotal)
        txtPending = findViewById(R.id.txtPending)
        edtSearch = findViewById(R.id.edtSearch)
        requestNotificationPermission()
        val btnNotifications = findViewById<Button>(R.id.btnNotifications)
        val btnBackup = findViewById<Button>(R.id.btnBackup)

        btnBackup.setOnClickListener {
            val intent = Intent(this, BackupActivity::class.java)
            startActivity(intent)
        }

        btnNotifications.setOnClickListener {

            val intent =
                Intent(
                    this,
                    NotificationSettingsActivity::class.java
                )

            startActivity(intent)
        }

        val btnDailyGoals =
            findViewById<Button>(R.id.btnDailyGoals)

        btnDailyGoals.setOnClickListener {

            val intent =
                Intent(
                    this,
                    DailyGoalsActivity::class.java
                )

            startActivity(intent)
        }

        // Initialize Database Helper
        databaseHelper = DatabaseHelper(this)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(
            taskList,
            onEdit = { task -> editTask(task) },
            onDelete = { task -> deleteTask(task) },
            onComplete = { task -> completeTask(task) }
        )
        recyclerView.adapter = taskAdapter

        // Button Click Listeners
        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        btnFocusMode.setOnClickListener {
            val intent = Intent(this, FocusActivity::class.java)
            startActivity(intent)
        }

        btnStudySchedule.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        btnExamPlanner.setOnClickListener {
            val intent = Intent(this, ExamActivity::class.java)
            startActivity(intent)
        }

        btnAnalytics.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        // Search Listener
        edtSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTasks(s.toString())
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        taskList = databaseHelper.getAllTasks()
        taskAdapter.updateList(taskList)

        txtTotal.text = taskList.size.toString()

        val pending = taskList.count { it.status == "Pending" }
        txtPending.text = pending.toString()
    }

    private fun completeTask(task: Task) {
        task.status = "Completed"
        databaseHelper.updateTask(task)
        loadTasks()

        Toast.makeText(this, "Task Completed ✅", Toast.LENGTH_SHORT).show()
    }

    private fun deleteTask(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { _, _ ->
                databaseHelper.deleteTask(task.id)
                loadTasks()
                Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun editTask(task: Task) {
        Toast.makeText(this, "Edit feature will be added next", Toast.LENGTH_SHORT).show()
    }

    private fun searchTasks(query: String) {
        val allTasks = databaseHelper.getAllTasks()
        val filtered = ArrayList(
            allTasks.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.subject.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        )
        taskAdapter.updateList(filtered)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }
}