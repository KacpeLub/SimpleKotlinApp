package com.example.simplekotlintodo


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplekotlintodo.data.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(context: Context) : ViewModel() {
    // Tworzenie instancji repozytorium
    private val repository: Repository = Repository(context)

    // Pobieranie wszystkich zadań jako LiveData
    val allTasks: Flow<List<TodoItem>> = repository.getAll()

    // Funkcja dodająca nowe zadanie
    fun addTask(taskName: String, taskDescription: String, isDone: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(TodoItem(taskName = taskName, taskDescription = taskDescription, isDone = isDone))
        }
    }

    // Funkcja usuwająca zadanie
    fun deleteTask(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(todoItem)
        }
    }

    // Funkcja aktualizująca zadanie
    fun updateTask(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(todoItem)
        }
    }

    // Funkcja do "czyszczenia" całej bazy danych
    fun dropDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.dropDatabase()
        }
    }
}