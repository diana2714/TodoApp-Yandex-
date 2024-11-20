package com.example.todoappsecond.data.repository

import com.example.todoappsecond.domain.TodoItem
import com.example.todoappsecond.domain.Importance
import com.example.todoappsecond.data.dto.TodoItemRequestDto
import com.example.todoappsecond.data.api.TodoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class TodoItemsRepository(private val apiService: TodoApiService) {

    private val todoItems = mutableListOf<TodoItem>()

    init {
        todoItems.addAll(generateSampleTodoItems())
    }

    // из DTO в TodoItem (для получения данных с сервера)
    private fun mapToTodoItem(dto: TodoItemRequestDto): TodoItem {
        return TodoItem(
            id = dto.id,
            text = dto.text,
            importance = Importance.valueOf(dto.importance.uppercase()),
            deadline = dto.deadline?.let { Date(it) },
            isCompleted = dto.done,
            createdAt = Date(dto.created_at),
            modifiedAt = dto.changed_at?.let { Date(it) }
        )
    }

    // из TodoItem в DTO (для отправки данных на сервер)
    private fun mapToTodoItemDto(item: TodoItem): TodoItemRequestDto {
        return TodoItemRequestDto(
            id = item.id,
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
    }

    // Получение списка задач
    suspend fun getTodoItems(revision: Int): Result<List<TodoItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTodoList(revision)
                if (response.isSuccessful) {
                    response.body()?.list?.let { list ->
                        todoItems.clear()
                        todoItems.addAll(list.map { mapToTodoItem(it) }) // DTO в TodoItem
                    }
                    Result.success(todoItems.toList())
                } else {
                    Result.failure(Exception("Failed to load data: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("An error occurred while fetching the todo items", e))
            }
        }
    }

    suspend fun addTodoItem(item: TodoItem, revision: Int): Result<TodoItem> {
        val requestDto = mapToTodoItemDto(item) // в DTO

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addTodoItem(revision, requestDto)
                if (response.isSuccessful) {
                    val addedDto = response.body()?.list?.firstOrNull() ?: requestDto
                    val addedItem = mapToTodoItem(addedDto) // обратно в TodoItem
                    todoItems.add(addedItem)
                    Result.success(addedItem)
                } else {
                    Result.failure(Exception("Failed to add item: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("An error occurred while adding the todo item", e))
            }
        }
    }

    suspend fun deleteTodoItem(itemId: String, revision: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteTodoItem(revision, itemId)
                if (response.isSuccessful) {
                    todoItems.removeAll { it.id == itemId }
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to delete item: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("An error occurred while deleting the todo item", e))
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
