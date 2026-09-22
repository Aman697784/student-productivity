package com.example.studentproductivitysmartstudyplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentproductivitysmartstudyplanner.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExamAdapter(
    private var examList: ArrayList<Exam>,
    private val onDelete: (Exam) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    class ExamViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val status: TextView =
            itemView.findViewById(R.id.txtExamStatus)

        val subject: TextView =
            itemView.findViewById(R.id.txtExamSubject)

        val title: TextView =
            itemView.findViewById(R.id.txtExamTitle)

        val date: TextView =
            itemView.findViewById(R.id.txtExamDate)

        val time: TextView =
            itemView.findViewById(R.id.txtExamTime)

        val location: TextView =
            itemView.findViewById(R.id.txtExamLocation)

        val delete: Button =
            itemView.findViewById(R.id.btnDeleteExam)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExamViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_exam,
                    parent,
                    false
                )

        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ExamViewHolder,
        position: Int
    ) {

        val exam =
            examList[position]

        holder.subject.text =
            exam.subject

        holder.title.text =
            exam.title

        holder.date.text =
            "📅 ${formatDate(exam.date)}"

        holder.time.text =
            "⏰ ${exam.time}"

        if (exam.location.isEmpty()) {

            holder.location.visibility =
                View.GONE

        } else {

            holder.location.visibility =
                View.VISIBLE

            holder.location.text =
                "📍 ${exam.location}"
        }

        // DAYS LEFT

        val daysLeft =
            calculateDaysLeft(exam.date)

        when {

            daysLeft < 0 -> {

                holder.status.text =
                    "⚪ COMPLETED"

            }

            daysLeft == 0L -> {

                holder.status.text =
                    "🔴 EXAM TODAY"

            }

            daysLeft == 1L -> {

                holder.status.text =
                    "🔴 1 DAY LEFT"

            }

            daysLeft <= 7L -> {

                holder.status.text =
                    "🟠 $daysLeft DAYS LEFT"

            }

            else -> {

                holder.status.text =
                    "🟢 $daysLeft DAYS LEFT"
            }
        }

        // DELETE

        holder.delete.setOnClickListener {

            onDelete(exam)
        }
    }

    override fun getItemCount(): Int =
        examList.size

    fun updateList(
        newList: ArrayList<Exam>
    ) {

        examList =
            newList

        notifyDataSetChanged()
    }

    // -----------------------------
    // CALCULATE DAYS
    // -----------------------------

    private fun calculateDaysLeft(
        date: String
    ): Long {

        return try {

            val format =
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.ENGLISH
                )

            val examDate =
                format.parse(date)

            val today =
                Calendar.getInstance()

            today.set(
                Calendar.HOUR_OF_DAY,
                0
            )

            today.set(
                Calendar.MINUTE,
                0
            )

            today.set(
                Calendar.SECOND,
                0
            )

            today.set(
                Calendar.MILLISECOND,
                0
            )

            val examCalendar =
                Calendar.getInstance()

            examCalendar.time =
                examDate!!

            examCalendar.set(
                Calendar.HOUR_OF_DAY,
                0
            )

            examCalendar.set(
                Calendar.MINUTE,
                0
            )

            examCalendar.set(
                Calendar.SECOND,
                0
            )

            examCalendar.set(
                Calendar.MILLISECOND,
                0
            )

            val difference =
                examCalendar.timeInMillis -
                        today.timeInMillis

            difference /
                    (1000 * 60 * 60 * 24)

        } catch (e: Exception) {

            0
        }
    }

    // -----------------------------
    // FORMAT DATE
    // -----------------------------

    private fun formatDate(
        date: String
    ): String {

        return try {

            val input =
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.ENGLISH
                )

            val output =
                SimpleDateFormat(
                    "dd MMMM yyyy",
                    Locale.ENGLISH
                )

            val parsed =
                input.parse(date)

            output.format(parsed!!)

        } catch (e: Exception) {

            date
        }
    }
}