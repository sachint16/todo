package com.pectolabs.todo.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pectolabs.todo.R
import com.pectolabs.todo.databinding.ItemTaskBinding
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.interfaces.TaskAdapterListener

class TaskAdapter(private val taskAdapterListener: TaskAdapterListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val tasks = ArrayList<Task>()

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    inner class TaskDiffCallback(
        private val oldTaskList: List<Task>,
        private val newTaskList: List<Task>
    ) : DiffUtil.Callback() {


        override fun getOldListSize(): Int {
            return oldTaskList.size
        }

        override fun getNewListSize(): Int {
            return newTaskList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTaskList[oldItemPosition].id === newTaskList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTaskList[oldItemPosition].id === newTaskList[newItemPosition].id
        }

    }

    fun submitList(newList: List<Task>) {
        val diffCallback = TaskDiffCallback(oldTaskList = tasks, newTaskList = newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        tasks.clear()
        tasks.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val tvTitle = holder.binding.tvTitle
        val tvDescription = holder.binding.tvDescription
        tvTitle.text = task.title
        tvDescription.text = task.description

        if (task.isCompleted) {

            tvTitle.apply {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                setTextColor(this.resources.getColor(R.color.wild_watermelon))
            }
            tvDescription.apply {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                setTextColor(this.resources.getColor(R.color.wild_watermelon))

            }
        } else {
            tvTitle.apply {
                paintFlags = 0
                setTextColor(this.resources.getColor(R.color.white))
            }
            tvDescription.apply {
                paintFlags = 0
                setTextColor(this.resources.getColor(R.color.white))

            }
        }

        holder.itemView.setOnClickListener {
            taskAdapterListener.onTaskClickListener(task)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}