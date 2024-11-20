package com.example.todoappsecond.data.repository

import com.example.todoappsecond.data.model.TodoItem
import com.example.todoappsecond.data.model.Importance
import com.example.todoappsecond.data.model.TodoItemRequest
import com.example.todoappsecond.data.model.TodoItemRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.todoappsecond.network.Result
import com.example.todoappsecond.network.TodoApiService
import java.util.UUID
import java.util.Date

class TodoItemsRepository(private val apiService: TodoApiService) {

    private val todoItems = mutableListOf<TodoItem>()

    init {
        todoItems.addAll(generateSampleTodoItems())
    }

    private fun mapToTodoItem(request: TodoItemRequestDto): TodoItem {
        return TodoItem(
            id = request.id,
            text = request.text,
            importance = Importance.valueOf(request.importance.uppercase()),
            deadline = request.deadline?.let { Date(it) },
            isCompleted = request.done,
            createdAt = Date(request.created_at),
            modifiedAt = request.changed_at?.let { Date(it) }
        )
    }


    // Получение списка задач
    suspend fun getTodoItems(revision: Int): Result<List<TodoItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTodoList(revision)
                if (response.isSuccessful) {
                    response.body()?.list?.let { list ->
                        todoItems.clear()
                        todoItems.addAll(list.map { mapToTodoItem(it.element) })
                    }
                    Result.Success(todoItems.toList())
                } else {
                    Result.Error(Exception("Failed to load data: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(Exception("An error occurred while fetching the todo items", e))
            }
        }
    }

    // Добавление новой задачи
    suspend fun addTodoItem(item: TodoItem, revision: Int): Result<TodoItem> {
        val requestItemDto = TodoItemRequestDto(
            id = UUID.randomUUID().toString(),
            text = item.text,
            importance = when (item.importance) {
                Importance.LOW -> "low"
                Importance.NORMAL -> "basic"
                Importance.HIGH -> "important"
            },
            deadline = item.deadline?.time,
            done = item.isCompleted,
            color = "#FFFFFF",
            created_at = item.createdAt.time,
            changed_at = System.currentTimeMillis(),
            last_updated_by = "device_id"
        )

        val requestItem = TodoItemRequest(element = requestItemDto)

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addTodoItem(revision, requestItem)
                if (response.isSuccessful) {
                    val addedItemRequest = response.body()?.list?.firstOrNull()?.element ?: requestItemDto
                    val addedItem = mapToTodoItem(addedItemRequest)
                    todoItems.add(addedItem)
                    Result.Success(addedItem)
                } else {
                    Result.Error(Exception("Failed to add item: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(Exception("An error occurred while adding the todo item", e))
            }
        }
    }


    // Удаление задачи по ID
    suspend fun deleteTodoItem(itemId: String, revision: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteTodoItem(revision, itemId)
                if (response.isSuccessful) {
                    todoItems.removeAll { it.id == itemId }
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Failed to delete item: ${response.message()}"))
                }
            } catch (e: Exception) {
                todoItems.removeAll { it.id == itemId }
                Result.Error(Exception("An error occurred while deleting the todo item", e))
            }
        }
    }

    private fun generateSampleTodoItems(): List<TodoItem> {
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
                    createdAt = Date(),
                    modifiedAt = null
                )
            )
        }
        return items
    }
}
