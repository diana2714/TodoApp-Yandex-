package com.example.todoappsecond.ui.newTaskScreen

import androidx.compose.runtime.mutableStateOf
import com.example.todoappsecond.domain.Importance
import java.util.Date

class NewTaskScreenModel {
    val taskText = mutableStateOf("")
    val importance = mutableStateOf("Нет")
    val isDeadlineSet = mutableStateOf(false)
    var selectedDate = mutableStateOf<Date?>(null)
    var expanded = mutableStateOf(false)

    val taskImportance: Importance
        get() = when (importance.value) {
            "Нет" -> Importance.NORMAL
            "Низкий" -> Importance.LOW
            "!! Высокий" -> Importance.HIGH
            else -> Importance.NORMAL
        }

    fun reset() {
        taskText.value = ""
        importance.value = "Нет"
        isDeadlineSet.value = false
        selectedDate.value = null
        expanded.value = false
    }
}