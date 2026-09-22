package com.example.studentproductivitysmartstudyplanner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelperExam(context: Context) :
    SQLiteOpenHelper(context, "ExamDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = """
            CREATE TABLE exams (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                subject TEXT NOT NULL,
                title TEXT NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                location TEXT
            )
        """.trimIndent()

        db.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS exams")
        onCreate(db)
    }

    // ADD EXAM
    fun addExam(exam: Exam): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("subject", exam.subject)
        values.put("title", exam.title)
        values.put("date", exam.date)
        values.put("time", exam.time)
        values.put("location", exam.location)

        val result = db.insert(
            "exams",
            null,
            values
        )

        return result != -1L
    }

    // GET ALL EXAMS
    fun getAllExams(): ArrayList<Exam> {

        val examList = ArrayList<Exam>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM exams",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val exam = Exam(

                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),

                    subject = cursor.getString(
                        cursor.getColumnIndexOrThrow("subject")
                    ),

                    title = cursor.getString(
                        cursor.getColumnIndexOrThrow("title")
                    ),

                    date = cursor.getString(
                        cursor.getColumnIndexOrThrow("date")
                    ),

                    time = cursor.getString(
                        cursor.getColumnIndexOrThrow("time")
                    ),

                    location = cursor.getString(
                        cursor.getColumnIndexOrThrow("location")
                    )
                )

                examList.add(exam)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return examList
    }

    // DELETE EXAM
    fun deleteExam(id: Int): Boolean {

        val db = writableDatabase

        val result = db.delete(
            "exams",
            "id=?",
            arrayOf(id.toString())
        )

        return result > 0
    }
}