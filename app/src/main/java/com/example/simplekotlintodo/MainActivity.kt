package com.example.simplekotlintodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel()){
    var showDialog by remember { mutableStateOf(false) }

    Surface (modifier = Modifier.fillMaxSize()){
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                if (showDialog) {
                    TaskDialog(
                        onAddTask = { taskName, taskDescription ->
                            todoViewModel.addTask(taskName, taskDescription)
                            showDialog = false // Zamknij dialog po dodaniu zadania
                        },
                        onDismissRequest = { showDialog = false } // Zamknij dialog na żądanie
                    )
                }
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(items = todoViewModel.todos) { todo ->
                        TodoItemRow(
                            todo,
                            todoViewModel::toggleTaskDone,
                            todoViewModel::removeTask
                        )
                    }
                }
            }
            FloatingActionButton(
                onClick = { showDialog = true }, // Pokaż okno dialogowe
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    }
}

@Composable
fun TaskDialog(onAddTask: (String, String) -> Unit, onDismissRequest: () -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
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
                        onAddTask(taskName, taskDescription)
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
fun TodoItemRow(
    todo: TodoItem,
    onItemCheckedChange: (TodoItem, Boolean) -> Unit,
    onDeleteItem: (TodoItem) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)) {
            Text(
                text = todo.taskName,
                //style = MaterialTheme.typography.headlineLarge,
                style = if (todo.isDone) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
            )
            Text(text = todo.taskDescription)
        }
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { isChecked ->
                onItemCheckedChange(todo, isChecked)
            }
        )
        IconButton(onClick = { onDeleteItem(todo) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete task")
        }
    }
}



