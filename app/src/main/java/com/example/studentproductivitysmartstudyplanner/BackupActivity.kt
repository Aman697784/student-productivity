package com.example.studentproductivitysmartstudyplanner

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupActivity : AppCompatActivity() {

    private lateinit var btnCreateBackup: Button
    private lateinit var btnRestoreBackup: Button

    private val createBackupCode = 100
    private val restoreBackupCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_backup)

        btnCreateBackup =
            findViewById(R.id.btnCreateBackup)

        btnRestoreBackup =
            findViewById(R.id.btnRestoreBackup)

        btnCreateBackup.setOnClickListener {
            createBackup()
        }

        btnRestoreBackup.setOnClickListener {
            restoreBackup()
        }
    }

    // ---------------------------------------
    // CREATE BACKUP
    // ---------------------------------------

    private fun createBackup() {

        val date =
            SimpleDateFormat(
                "yyyy-MM-dd_HH-mm",
                Locale.ENGLISH
            ).format(Date())

        val fileName =
            "StudentPlanner_Backup_$date.json"

        val intent =
            Intent(Intent.ACTION_CREATE_DOCUMENT).apply {

                type = "application/json"

                putExtra(
                    Intent.EXTRA_TITLE,
                    fileName
                )
            }

        startActivityForResult(
            intent,
            createBackupCode
        )
    }

    // ---------------------------------------
    // RESTORE BACKUP
    // ---------------------------------------

    private fun restoreBackup() {

        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {

                type = "application/json"

                addCategory(
                    Intent.CATEGORY_OPENABLE
                )
            }

        startActivityForResult(
            intent,
            restoreBackupCode
        )
    }

    // ---------------------------------------
    // FILE RESULT
    // ---------------------------------------

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            resultCode != Activity.RESULT_OK ||
            data?.data == null
        ) {
            return
        }

        val uri =
            data.data!!

        if (requestCode == createBackupCode) {

            saveBackupFile(uri)

        } else if (requestCode == restoreBackupCode) {

            readBackupFile(uri)
        }
    }

    // ---------------------------------------
    // SAVE BACKUP FILE
    // ---------------------------------------

    private fun saveBackupFile(uri: Uri) {

        try {

            val backupData =
                createBackupJson()

            contentResolver
                .openOutputStream(uri)
                ?.use { outputStream ->

                    outputStream.write(
                        backupData.toByteArray()
                    )
                }

            Toast.makeText(
                this,
                "Backup created successfully 💾",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Backup failed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ---------------------------------------
    // CREATE JSON
    // ---------------------------------------

    private fun createBackupJson(): String {

        val scheduleDatabase =
            DatabaseHelperSchedule(this)

        val schedules =
            scheduleDatabase.getAllSchedules()

        val json =
            StringBuilder()

        json.append("{")

        json.append("\"app\":\"Student Productivity Planner\",")
        json.append("\"version\":1,")

        json.append("\"backupDate\":\"")
        json.append(
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH
            ).format(Date())
        )
        json.append("\",")

        json.append("\"schedules\":[")

        schedules.forEachIndexed { index, schedule ->

            json.append("{")

            json.append(
                "\"id\":${schedule.id},"
            )

            json.append(
                "\"subject\":\"${escapeJson(schedule.subject)}\","
            )

            json.append(
                "\"topic\":\"${escapeJson(schedule.topic)}\","
            )

            json.append(
                "\"day\":\"${escapeJson(schedule.day)}\","
            )

            json.append(
                "\"startTime\":\"${escapeJson(schedule.startTime)}\","
            )

            json.append(
                "\"endTime\":\"${escapeJson(schedule.endTime)}\""
            )

            json.append("}")

            if (index < schedules.size - 1) {
                json.append(",")
            }
        }

        json.append("]")

        json.append("}")

        return json.toString()
    }

    // ---------------------------------------
    // JSON ESCAPE
    // ---------------------------------------

    private fun escapeJson(
        text: String
    ): String {

        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
    }

    // ---------------------------------------
    // READ BACKUP
    // ---------------------------------------

    private fun readBackupFile(uri: Uri) {

        try {

            val content =
                contentResolver
                    .openInputStream(uri)
                    ?.use { inputStream ->

                        BufferedReader(
                            InputStreamReader(
                                inputStream
                            )
                        ).readText()
                    }

            if (
                content.isNullOrEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Backup file is empty",
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            if (
                !content.contains(
                    "Student Productivity Planner"
                )
            ) {

                Toast.makeText(
                    this,
                    "Invalid backup file ❌",
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            Toast.makeText(
                this,
                "Backup file found successfully 📥",
                Toast.LENGTH_LONG
            ).show()

            /*
             * Restore parsing will be connected
             * to your database in the next step.
             */

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Restore failed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}