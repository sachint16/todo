package com.pectolabs.todo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE is_completed=0")
    fun getAllPendingTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE is_completed=1")
    fun getAllCompletedTasks(): LiveData<List<Task>>

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task:Task)


}