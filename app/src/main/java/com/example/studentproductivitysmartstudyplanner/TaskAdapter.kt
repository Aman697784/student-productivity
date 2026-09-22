package com.example.studentproductivitysmartstudyplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var taskList: ArrayList<Task>,
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
    private val onComplete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val category: TextView =
            itemView.findViewById(R.id.txtCategory)

        val title: TextView =
            itemView.findViewById(R.id.txtTitle)

        val subject: TextView =
            itemView.findViewById(R.id.txtSubject)

        val description: TextView =
            itemView.findViewById(R.id.txtDescription)

        val dueDate: TextView =
            itemView.findViewById(R.id.txtDueDate)

        val priority: TextView =
            itemView.findViewById(R.id.txtPriority)

        val status: TextView =
            itemView.findViewById(R.id.txtStatus)

        val complete: Button =
            itemView.findViewById(R.id.btnComplete)

        val edit: Button =
            itemView.findViewById(R.id.btnEdit)

        val delete: Button =
            itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_task,
                parent,
                false
            )

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        val task = taskList[position]

        holder.category.text =
            "🏷️ ${task.category}"

        holder.title.text =
            task.title

        holder.subject.text =
            "📚 ${task.subject}"

        holder.description.text =
            task.description

        holder.dueDate.text =
            "📅 Due Date: ${task.dueDate}"

        holder.priority.text =
            "⭐ Priority: ${task.priority}"

        holder.status.text =
            if (task.status == "Completed")
                "✅ Status: Completed"
            else
                "⏳ Status: Pending"

        holder.complete.setOnClickListener {
            onComplete(task)
        }

        holder.edit.setOnClickListener {
            onEdit(task)
        }

        holder.delete.setOnClickListener {
            onDelete(task)
        }
    }

    override fun getItemCount(): Int =
        taskList.size

    fun updateList(
        newList: ArrayList<Task>
    ) {

        taskList = newList

        notifyDataSetChanged()
    }
}