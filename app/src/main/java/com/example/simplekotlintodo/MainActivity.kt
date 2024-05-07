package com.example.simplekotlintodo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


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
    var taskToEdit by remember { mutableStateOf<TodoItem?>(null) }

    // Layout aplikacji
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TASKS",
                        modifier = Modifier.fillMaxWidth(), // Wypełnienie maksymalnej szerokości
                        style = TextStyle(
                            fontStyle = FontStyle.Italic, // Ustawienie tekstu na kursywę
                            fontSize = 20.sp, // Możesz dostosować rozmiar czcionki
                            textAlign = TextAlign.Center  // Wypośrodkowanie tekstu
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) {
        TodoList(tasks = allTasks, todoViewModel, onTaskLongClick = { task ->
            taskToEdit = task
            showDialog = true
        })

        if (showDialog) {
            TaskDialog(
                todoViewModel,
                taskToEdit,
                onDismissRequest = {
                    showDialog = false
                    taskToEdit = null // resetujemy edytowany obiekt po zamknięciu dialogu
                } // Zamknij dialog na żądanie
            )
        }
    }
}

@Composable
fun TodoList(tasks: List<TodoItem>, todoViewModel: TodoViewModel, onTaskLongClick: (TodoItem) -> Unit) {
    Column(modifier = Modifier.padding(top = 45.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)) {
        LazyColumn(modifier = Modifier.padding(8.dp)){
            items(tasks) { task ->
                TaskRow(task, todoViewModel, onTaskLongClick)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskRow(task: TodoItem, todoViewModel: TodoViewModel, onTaskLongClick: (TodoItem) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)
            .combinedClickable(
                onClick = { isExpanded = !isExpanded }, // Możesz tutaj zdefiniować puste kliknięcie, jeśli nie chcesz żadnej akcji przy standardowym kliknięciu
                onLongClick = { onTaskLongClick(task) } // Obsługa długiego kliknięcia
            )
        ){

            Text(
                text = task.taskName,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    //color = Color.Blue, // zmiana koloru czcionki
                    letterSpacing = 0.5.sp, // odstęp między literami
                    lineHeight = 24.sp, // wysokość linii
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None)
            )
            Text(
                text = task.taskDescription,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(all = 4.dp),
                maxLines = if(isExpanded) Int.MAX_VALUE else 1
            )
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

@Composable
fun TaskDialog(todoViewModel: TodoViewModel, taskToEdit: TodoItem? = null, onDismissRequest: () -> Unit) {
    var taskName by remember(taskToEdit) { mutableStateOf(taskToEdit?.taskName ?: "") }
    var taskDescription by remember(taskToEdit) { mutableStateOf(taskToEdit?.taskDescription ?: "") }
    val isNewTask = taskToEdit == null

    AlertDialog(onDismissRequest = { onDismissRequest() },
        title = { Text(if (isNewTask) "Add New Task" else "Edit Task") },
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
                        if (isNewTask) {
                            todoViewModel.addTask(taskName, taskDescription)
                        } else {
                            todoViewModel.updateTask(taskToEdit!!.copy(taskName = taskName, taskDescription = taskDescription))
                        }
                        onDismissRequest()
                    }
                }
            ) {
                Text(if (isNewTask) "Add Task" else "Save Changes")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        }
    )
}








