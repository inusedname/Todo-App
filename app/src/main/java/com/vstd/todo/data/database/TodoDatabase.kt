package com.vstd.todo.data.database

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import com.vstd.todo.data.Tag
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.crossReference.TaskTagCrossRef

@RequiresApi(Build.VERSION_CODES.O)
@Database(
    entities = [
        Tag::class,
        Task::class,
        Workspace::class,
        TaskTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract val todoDAO: TodoDao

    companion object {

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {

            // Just only one thread can execute code block at the same time.
            // Prevents more than one threads create database simultaneously.
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}