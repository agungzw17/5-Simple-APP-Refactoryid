package com.agung.taskapp.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

import com.agung.taskapp.data.model.TaskModel

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun select(): MutableList<TaskModel>

    @Query("SELECT * FROM task WHERE task_date LIKE :date")
    fun selectDate(date: String): MutableList<TaskModel>

    @Query("SELECT COUNT(task_date) FROM task WHERE task_date LIKE :date")
    fun getDateCount(date: String):Int

    @Insert(onConflict = REPLACE)
    fun insert(vararg taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel?)

    @Update
    fun update(taskModel: TaskModel?)
}