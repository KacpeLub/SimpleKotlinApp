package com.example.simplekotlintodo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TodoViewModel: ViewModel() {
    var todos = mutableStateListOf<TodoItem>()
        private set
    //var showDialog = mutableStateOf(false)


    fun toggleTaskDone(todoItem: TodoItem, isDone: Boolean) {
        // Znajdź zadanie w liście i zaktualizuj jego stan
        val taskIndex = todos.indexOfFirst { it.id == todoItem.id }
        if (taskIndex != -1) {
            todos[taskIndex] = todoItem.copy(isDone = isDone)
        }
    }
    fun removeTask(todoItem: TodoItem) {
        todos.remove(todoItem)
    }
    fun addTask(taskName: String, taskDescription: String) {
        val newTask = TodoItem(taskName = taskName, taskDescription = taskDescription)
        todos.add(newTask)
        //showDialog.value = false
    }
}