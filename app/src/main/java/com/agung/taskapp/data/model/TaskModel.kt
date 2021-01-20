package com.agung.taskapp.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task")
data class TaskModel(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        @ColumnInfo(name = "id_task")
        var id_task: Long = 0,

        @ColumnInfo(name = "task_tittle")
        var task_tittle: String,

        @ColumnInfo(name = "task_description")
        var task_description: String,

        @ColumnInfo(name = "task_date")
        var task_date: String
)