package com.example.simplekotlintodo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplekotlintodo.data.TodoItem
import androidx.compose.runtime.collectAsState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TodoApp(todoViewModel = viewModel { TodoViewModel(application) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoApp(todoViewModel: TodoViewModel) {
    // Przechwytywanie danych z ViewModel
    val allTasks by todoViewModel.allTasks.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    // Layout aplikacji
    Scaffold(
        topBar = { TopAppBar(title = { Text("Todo List") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) {
        if (showDialog) {
            TaskDialog(
                todoViewModel,
                onDismissRequest = { showDialog = false } // Zamknij dialog na żądanie
            )
        }

        TodoList(tasks = allTasks, todoViewModel)
    }
}

@Composable
fun TaskDialog(todoViewModel: TodoViewModel, onDismissRequest: () -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = { onDismissRequest() },
        title = { Text("Add New Task") },
        text = {
            Column {
                TextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") }
                )
                TextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Task Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskName.isNotEmpty()) {
                    todoViewModel.addTask(taskName, taskDescription)
                    onDismissRequest()
                    }
                }
            ) {
            Text("Add Task")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun TodoList(tasks: List<TodoItem>, todoViewModel: TodoViewModel) {
    Column(modifier = Modifier.padding(8.dp)) {
        LazyColumn(modifier = Modifier.padding(8.dp)){
            items(tasks) { task ->
                TaskRow(task, todoViewModel)
            }
        }
    }
}

@Composable
fun TaskRow(task: TodoItem, todoViewModel: TodoViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {

        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)) {

            Text(
                text = task.taskName,
                style = if (task.isDone) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
            )
            Text(text = task.taskDescription)
        }

        Checkbox(
            checked = task.isDone,
            onCheckedChange = {isChecked ->
                todoViewModel.updateTask(task.copy(isDone = isChecked))
            } )

        IconButton(onClick = { todoViewModel.deleteTask(task) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}



