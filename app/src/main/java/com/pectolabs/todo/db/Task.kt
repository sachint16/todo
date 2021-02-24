package com.pectolabs.todo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(

        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "scheduled_at") val scheduledAt: Date,
        @ColumnInfo(name = "is_completed") val isCompleted: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
