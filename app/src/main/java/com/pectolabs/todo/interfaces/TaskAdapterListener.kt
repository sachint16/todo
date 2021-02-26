package com.pectolabs.todo.interfaces

import com.pectolabs.todo.db.Task

interface TaskAdapterListener {
    fun onTaskClickListener(task:Task)

    fun onRemoveItem(task: Task,position:Int)
    fun onUpdateItem(task: Task, position: Int)
}