package com.example.todoappsecond.data.dto

import com.google.gson.annotations.SerializedName

data class TodoItemResponseDto(
    @SerializedName("list") val list: List<TodoItemRequestDto>,
    @SerializedName("revision") val revision: Int,
    @SerializedName("status") val status: String
)
