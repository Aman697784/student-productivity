package com.example.studentproductivitysmartstudyplanner

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FocusActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var txtMode: TextView
    private lateinit var txtTaskName: TextView
    private lateinit var txtSessionInfo: TextView

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timer: CountDownTimer? = null

    private var timeLeftInMillis =
        25 * 60 * 1000L

    private val focusTime =
        25 * 60 * 1000L

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_focus
        )

        txtTimer =
            findViewById(R.id.txtTimer)

        txtMode =
            findViewById(R.id.txtMode)

        txtTaskName =
            findViewById(R.id.txtTaskName)

        txtSessionInfo =
            findViewById(R.id.txtSessionInfo)

        btnStart =
            findViewById(R.id.btnStart)

        btnPause =
            findViewById(R.id.btnPause)

        btnReset =
            findViewById(R.id.btnReset)

        // -----------------------------
        // GET SCHEDULE DATA
        // -----------------------------

        val subject =
            intent.getStringExtra(
                "subject"
            )

        val topic =
            intent.getStringExtra(
                "topic"
            )

        if (!subject.isNullOrEmpty()) {

            txtTaskName.text =
                "$subject\n$topic"

        } else {

            txtTaskName.text =
                "Focus Session 🧠"
        }

        updateTimerText()

        // -----------------------------
        // BUTTONS
        // -----------------------------

        btnStart.setOnClickListener {

            startTimer()
        }

        btnPause.setOnClickListener {

            pauseTimer()
        }

        btnReset.setOnClickListener {

            resetTimer()
        }
    }

    // -----------------------------
    // START TIMER
    // -----------------------------

    private fun startTimer() {

        timer?.cancel()

        timer =
            object : CountDownTimer(
                timeLeftInMillis,
                1000
            ) {

                override fun onTick(
                    millisUntilFinished: Long
                ) {

                    timeLeftInMillis =
                        millisUntilFinished

                    updateTimerText()
                }

                override fun onFinish() {

                    timeLeftInMillis =
                        0

                    updateTimerText()

                    txtMode.text =
                        "SESSION COMPLETE 🎉"

                    txtSessionInfo.text =
                        "Great work! Time for a short break ☕"

                    Toast.makeText(
                        this@FocusActivity,
                        "Focus session completed! 🎉",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }.start()

        txtMode.text =
            "FOCUSING 🧠"

        txtSessionInfo.text =
            "Stay focused. You've got this! 💪"
    }

    // -----------------------------
    // PAUSE
    // -----------------------------

    private fun pauseTimer() {

        timer?.cancel()

        txtMode.text =
            "PAUSED ⏸"

        txtSessionInfo.text =
            "Take a small breath and continue when ready."
    }

    // -----------------------------
    // RESET
    // -----------------------------

    private fun resetTimer() {

        timer?.cancel()

        timeLeftInMillis =
            focusTime

        updateTimerText()

        txtMode.text =
            "FOCUS SESSION"

        txtSessionInfo.text =
            "Focus for 25 minutes • Then take a break ☕"
    }

    // -----------------------------
    // UPDATE TIMER
    // -----------------------------

    private fun updateTimerText() {

        val minutes =
            (timeLeftInMillis / 1000) / 60

        val seconds =
            (timeLeftInMillis / 1000) % 60

        txtTimer.text =
            String.format(
                "%02d:%02d",
                minutes,
                seconds
            )
    }

    // -----------------------------
    // DESTROY
    // -----------------------------

    override fun onDestroy() {

        timer?.cancel()

        super.onDestroy()
    }
}