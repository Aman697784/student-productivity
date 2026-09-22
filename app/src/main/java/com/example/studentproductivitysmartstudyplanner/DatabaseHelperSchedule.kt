package com.example.studentproductivitysmartstudyplanner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelperSchedule(context: Context) :
    SQLiteOpenHelper(context, "ScheduleDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = """
            CREATE TABLE schedules (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                subject TEXT NOT NULL,
                topic TEXT NOT NULL,
                day TEXT NOT NULL,
                startTime TEXT NOT NULL,
                endTime TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS schedules")
        onCreate(db)
    }

    // ADD NEW STUDY SCHEDULE
    fun addSchedule(schedule: StudySchedule): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("subject", schedule.subject)
        values.put("topic", schedule.topic)
        values.put("day", schedule.day)
        values.put("startTime", schedule.startTime)
        values.put("endTime", schedule.endTime)

        val result = db.insert(
            "schedules",
            null,
            values
        )

        return result != -1L
    }

    // GET ALL SCHEDULES
    fun getAllSchedules(): ArrayList<StudySchedule> {

        val scheduleList = ArrayList<StudySchedule>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM schedules ORDER BY id DESC",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val schedule = StudySchedule(

                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),

                    subject = cursor.getString(
                        cursor.getColumnIndexOrThrow("subject")
                    ),

                    topic = cursor.getString(
                        cursor.getColumnIndexOrThrow("topic")
                    ),

                    day = cursor.getString(
                        cursor.getColumnIndexOrThrow("day")
                    ),

                    startTime = cursor.getString(
                        cursor.getColumnIndexOrThrow("startTime")
                    ),

                    endTime = cursor.getString(
                        cursor.getColumnIndexOrThrow("endTime")
                    )
                )

                scheduleList.add(schedule)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return scheduleList
    }

    // DELETE SCHEDULE
    fun deleteSchedule(id: Int): Boolean {

        val db = writableDatabase

        val result = db.delete(
            "schedules",
            "id=?",
            arrayOf(id.toString())
        )

        return result > 0
    }
}