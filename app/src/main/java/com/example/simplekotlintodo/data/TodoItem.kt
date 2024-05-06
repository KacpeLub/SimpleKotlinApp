package com.example.simplekotlintodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    var taskName: String,
    var taskDescription: String,
    var isDone: Boolean = false
)
