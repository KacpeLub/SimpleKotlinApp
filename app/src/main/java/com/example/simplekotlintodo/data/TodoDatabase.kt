package com.example.simplekotlintodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoItem::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun TodoDao(): TodoDao
}

object TodoDb{
    private var db: TodoDatabase? = null
    fun getInstance(context: Context): TodoDatabase {
        if(db == null){
            db = Room.databaseBuilder(
                context,
                TodoDatabase::class.java,
                "todo-database"
            ).build()
        }
        return db!!
    }
}