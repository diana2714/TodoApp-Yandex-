package com.example.to_doapp.data.model

import java.util.Date

data class TodoItem(
    val id: String,                 // Идентификатор задания
    val text: String,               // Описание задания
    val importance: Importance,     // Важность задания
    val deadline: Date? = null,     // Срок выполнения (опциональный)
    val isCompleted: Boolean,       // Флаг выполнения
    val createdAt: Date,            // Дата создания
    val modifiedAt: Date? = null    // Дата изменения (опциональный)
)

enum class Importance(val value: String) {
    LOW("низкая"),
    NORMAL("обычная"),
    HIGH("срочная")
}

