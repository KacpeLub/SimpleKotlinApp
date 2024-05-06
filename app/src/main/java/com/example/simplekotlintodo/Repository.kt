package com.example.simplekotlintodo

import android.content.Context
import com.example.simplekotlintodo.data.TodoDao
import com.example.simplekotlintodo.data.TodoDb
import com.example.simplekotlintodo.data.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Repository (context: Context): TodoDao {
    private val dao = TodoDb.getInstance(context).TodoDao()
    override suspend fun insert(todo: TodoItem) = withContext(Dispatchers.IO){
        dao.insert(todo)
    }

    override suspend fun insertAll(todos: List<TodoItem>) = withContext(Dispatchers.IO) {
        dao.insertAll(todos)
    }

    override suspend fun delete(todo: TodoItem) = withContext(Dispatchers.IO){
        dao.delete(todo)
    }

    override suspend fun update(todo: TodoItem) = withContext(Dispatchers.IO) {
        dao.update(todo)
    }

    override fun getAll(): Flow<List<TodoItem>> {
        return dao.getAll()
    }

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        dao.dropDatabase()
    }
}