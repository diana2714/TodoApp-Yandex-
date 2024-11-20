package com.example.todoappsecond.network

import com.example.todoappsecond.domain.TodoItem

data class TodoListResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)

data class TodoItemResponse(
    val status: String,
    val element: TodoItem,
    val revision: Int
)