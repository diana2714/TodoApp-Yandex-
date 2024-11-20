package com.example.todoappsecond.data.model

import com.example.todoappsecond.data.dto.TodoItemRequestDto

data class TodoItemResponse(
    val list: List<TodoItemRequestDto>,
    val revision: Int,
    val status: String
)