package com.example.todoappsecond.data.model

import com.google.gson.annotations.SerializedName

data class TodoItemRequest(
    @SerializedName("element") val element: TodoItemRequestDto
)

data class TodoItemRequestDto(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String, // Знач-я: "low", "basic", или "important"
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String?, // Опционально
    @SerializedName("created_at") val created_at: Long,
    @SerializedName("changed_at") val changed_at: Long,
    @SerializedName("last_updated_by") val last_updated_by: String
)
