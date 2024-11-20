package com.example.todoappsecond.data.model

import com.example.todoappsecond.data.dto.TodoItemRequestDto
import com.google.gson.annotations.SerializedName

data class TodoItemRequest(
    @SerializedName("element") val element: TodoItemRequestDto
)
