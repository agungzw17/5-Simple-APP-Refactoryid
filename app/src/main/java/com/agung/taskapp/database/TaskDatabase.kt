package com.agung.taskapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agung.taskapp.data.model.TaskModel

@Database(entities = arrayOf(TaskModel::class), version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {
//    abstract fun taskDao(): TaskDao
//
//    private lateinit var INSTANCE: TaskDatabase
//
//    open fun createDatabase(context: Context): TaskDatabase? {
//        synchronized(TaskDatabase::class) {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context, TaskDatabase::class.java, "db_siswa")
//                        .allowMainThreadQueries()
//                        .fallbackToDestructiveMigration()
//                        .build()
//            }
//        }
//        return INSTANCE
//    }

    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase? {
            if (INSTANCE == null) {
                synchronized(TaskDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase::class.java, "task.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }


    }

}