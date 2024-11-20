package com.example.todoappsecond.data.model


data class TodoItemResponse(
    val status: String,
    val list: List<TodoItemRequest>,
    val revision: Int
)
