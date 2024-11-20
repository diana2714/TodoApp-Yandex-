package com.example.to_doapp.data.repository

import com.example.to_doapp.data.model.TodoItem
import com.example.to_doapp.data.model.Importance

class TodoItemsRepository {

    private val todoItems = mutableListOf<TodoItem>()

    init {
        todoItems.addAll(generateSampleTodoItems())
    }

    fun getTodoItems(): List<TodoItem> {
        return todoItems
    }

    fun addTodoItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun deleteTodoItem(itemId: String) {
        todoItems.removeAll { it.id == itemId }
    }

    fun generateSampleTodoItems(): List<TodoItem> {
        val items = mutableListOf<TodoItem>()
        for (i in 1..20) {
            items.add(
                TodoItem(
                    id = i.toString(),
                    text = "Задача $i",
                    importance = when (i % 3) {
                        0 -> Importance.LOW
                        1 -> Importance.NORMAL
                        else -> Importance.HIGH
                    },
                    isCompleted = false,
                    createdAt = java.util.Date(),
                    modifiedAt = null
                )
            )
        }
        return items
    }
}
