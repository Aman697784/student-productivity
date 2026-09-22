package com.example.studentproductivitysmartstudyplanner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "StudentPlannerDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                description TEXT,
                subject TEXT,
                category TEXT,
                dueDate TEXT,
                priority TEXT,
                status TEXT
            )
        """.trimIndent()

        db.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS tasks")

        onCreate(db)
    }

    fun addTask(task: Task): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("title", task.title)
        values.put("description", task.description)
        values.put("subject", task.subject)
        values.put("category", task.category)
        values.put("dueDate", task.dueDate)
        values.put("priority", task.priority)
        values.put("status", task.status)

        val result = db.insert(
            "tasks",
            null,
            values
        )

        return result != -1L
    }

    fun getAllTasks(): ArrayList<Task> {

        val taskList = ArrayList<Task>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM tasks ORDER BY id DESC",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val task = Task(

                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),

                    title = cursor.getString(
                        cursor.getColumnIndexOrThrow("title")
                    ),

                    description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description")
                    ),

                    subject = cursor.getString(
                        cursor.getColumnIndexOrThrow("subject")
                    ),

                    category = cursor.getString(
                        cursor.getColumnIndexOrThrow("category")
                    ),

                    dueDate = cursor.getString(
                        cursor.getColumnIndexOrThrow("dueDate")
                    ),

                    priority = cursor.getString(
                        cursor.getColumnIndexOrThrow("priority")
                    ),

                    status = cursor.getString(
                        cursor.getColumnIndexOrThrow("status")
                    )
                )

                taskList.add(task)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return taskList
    }

    fun updateTask(task: Task): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("title", task.title)
        values.put("description", task.description)
        values.put("subject", task.subject)
        values.put("category", task.category)
        values.put("dueDate", task.dueDate)
        values.put("priority", task.priority)
        values.put("status", task.status)

        val result = db.update(
            "tasks",
            values,
            "id=?",
            arrayOf(task.id.toString())
        )

        return result > 0
    }

    fun deleteTask(id: Int): Boolean {

        val db = writableDatabase

        val result = db.delete(
            "tasks",
            "id=?",
            arrayOf(id.toString())
        )

        return result > 0
    }
}