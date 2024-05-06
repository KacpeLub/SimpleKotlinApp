package com.example.simplekotlintodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: TodoItem)

    @Insert
    suspend fun insertAll(todos: List<TodoItem>)

    @Delete
    suspend fun delete (todo: TodoItem)

    @Update
    suspend fun update (todo: TodoItem)

    @Query("SELECT * FROM task_table")
    fun getAll(): Flow<List<TodoItem>>

    @Query("DELETE FROM task_table")
    suspend fun dropDatabase()
}