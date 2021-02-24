package com.pectolabs.todo.repositories

import com.pectolabs.todo.db.Task
import com.pectolabs.todo.db.TaskDAO
import javax.inject.Inject

class MainRepository @Inject constructor(val taskDAO: TaskDAO) {
    suspend fun insertTask(task: Task) = taskDAO.insertTask(task)
    suspend fun deleteTask(task: Task) = taskDAO.deleteTask(task)
    fun getAllTasks() = taskDAO.getAllTasks()

    fun getAllPendingTasks() = taskDAO.getAllPendingTasks()
    fun getAllCompletedTasks() = taskDAO.getAllCompletedTasks()
}