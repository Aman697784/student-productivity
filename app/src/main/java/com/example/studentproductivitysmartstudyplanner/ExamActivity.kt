package com.example.studentproductivitysmartstudyplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ExamActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var btnAddExam: Button

    private lateinit var txtEmptyExam: TextView

    private lateinit var databaseHelper: DatabaseHelperExam

    private lateinit var examAdapter: ExamAdapter

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_exam
        )

        recyclerView =
            findViewById(
                R.id.examRecyclerView
            )

        btnAddExam =
            findViewById(
                R.id.btnAddExam
            )

        txtEmptyExam =
            findViewById(
                R.id.txtEmptyExam
            )

        databaseHelper =
            DatabaseHelperExam(this)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        examAdapter =
            ExamAdapter(
                ArrayList(),
                onDelete = { exam ->
                    deleteExam(exam)
                }
            )

        recyclerView.adapter =
            examAdapter

        // ADD EXAM

        btnAddExam.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddExamActivity::class.java
                )

            startActivity(intent)
        }

        loadExams()
    }

    override fun onResume() {

        super.onResume()

        loadExams()
    }

    // -----------------------------
    // LOAD EXAMS
    // -----------------------------

    private fun loadExams() {

        val allExams =
            databaseHelper.getAllExams()

        val sortedExams =
            ArrayList(
                allExams.sortedBy {
                    it.date
                }
            )

        examAdapter.updateList(
            sortedExams
        )

        if (sortedExams.isEmpty()) {

            recyclerView.visibility =
                View.GONE

            txtEmptyExam.visibility =
                View.VISIBLE

        } else {

            recyclerView.visibility =
                View.VISIBLE

            txtEmptyExam.visibility =
                View.GONE
        }
    }

    // -----------------------------
    // DELETE
    // -----------------------------

    private fun deleteExam(
        exam: Exam
    ) {

        AlertDialog.Builder(this)

            .setTitle("Delete Exam")

            .setMessage(
                "Delete ${exam.subject} exam?"
            )

            .setPositiveButton("Yes") {
                    _, _ ->

                databaseHelper.deleteExam(
                    exam.id
                )

                loadExams()

                Toast.makeText(
                    this,
                    "Exam Deleted",
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