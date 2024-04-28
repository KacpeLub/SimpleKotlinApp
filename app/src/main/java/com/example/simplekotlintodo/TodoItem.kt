package com.example.simplekotlintodo

import java.util.UUID

data class TodoItem(
    val id: UUID = UUID.randomUUID(),
    var taskName: String,
    var taskDescription: String,
    var isDone: Boolean = false
)
