package com.example.studentproductivitysmartstudyplanner

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleAdapter(
    private var scheduleList: ArrayList<StudySchedule>,
    private val selectedDay: String = "",
    private val onDelete: (StudySchedule) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val status: TextView =
            itemView.findViewById(R.id.txtScheduleStatus)

        val day: TextView =
            itemView.findViewById(R.id.txtScheduleDay)

        val subject: TextView =
            itemView.findViewById(R.id.txtScheduleSubject)

        val topic: TextView =
            itemView.findViewById(R.id.txtScheduleTopic)

        val time: TextView =
            itemView.findViewById(R.id.txtScheduleTime)

        val focus: Button =
            itemView.findViewById(R.id.btnStartFocus)

        val delete: Button =
            itemView.findViewById(R.id.btnDeleteSchedule)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)

        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ScheduleViewHolder,
        position: Int
    ) {

        val schedule = scheduleList[position]

        holder.day.text = "📅 ${schedule.day}"
        holder.subject.text = schedule.subject
        holder.topic.text = "📖 ${schedule.topic}"
        holder.time.text =
            "⏰ ${schedule.startTime} - ${schedule.endTime}"

        // -----------------------------
        // CHECK SESSION STATUS
        // -----------------------------

        val status = getSessionStatus(schedule)

        holder.status.text = status

        when (status) {

            "🟢 NOW" -> {
                holder.status.alpha = 1.0f
            }

            "🔵 UPCOMING" -> {
                holder.status.alpha = 0.8f
            }

            "⚪ COMPLETED" -> {
                holder.status.alpha = 0.5f
            }
        }

        // -----------------------------
        // START FOCUS
        // -----------------------------

        holder.focus.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                FocusActivity::class.java
            )

            intent.putExtra(
                "subject",
                schedule.subject
            )

            intent.putExtra(
                "topic",
                schedule.topic
            )

            holder.itemView.context.startActivity(intent)
        }

        // -----------------------------
        // DELETE
        // -----------------------------

        holder.delete.setOnClickListener {
            onDelete(schedule)
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    fun updateList(
        newList: ArrayList<StudySchedule>
    ) {

        scheduleList = newList

        notifyDataSetChanged()
    }

    // -----------------------------
    // SESSION STATUS
    // -----------------------------

    private fun getSessionStatus(
        schedule: StudySchedule
    ): String {

        val today = SimpleDateFormat(
            "EEEE",
            Locale.ENGLISH
        ).format(Calendar.getInstance().time)

        // If selected day is not today
        if (!schedule.day.equals(today, ignoreCase = true)) {
            return "🔵 UPCOMING"
        }

        val start = timeToMinutes(schedule.startTime)
        val end = timeToMinutes(schedule.endTime)

        val calendar = Calendar.getInstance()

        val currentMinutes =
            calendar.get(Calendar.HOUR_OF_DAY) * 60 +
                    calendar.get(Calendar.MINUTE)

        return when {

            currentMinutes in start until end ->
                "🟢 NOW"

            currentMinutes < start ->
                "🔵 UPCOMING"

            else ->
                "⚪ COMPLETED"
        }
    }

    // -----------------------------
    // TIME TO MINUTES
    // -----------------------------

    private fun timeToMinutes(
        time: String
    ): Int {

        return try {

            val format = SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            )

            val date = format.parse(time)

            val calendar = Calendar.getInstance()

            calendar.time = date!!

            calendar.get(Calendar.HOUR_OF_DAY) * 60 +
                    calendar.get(Calendar.MINUTE)

        } catch (e: Exception) {

            0
        }
    }
}