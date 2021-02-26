package com.pectolabs.todo.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "task_table")
@Parcelize
data class Task(
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "scheduled_at") val scheduledAt: Date,
        @ColumnInfo(name = "is_completed") var isCompleted: Boolean

):Parcelable
