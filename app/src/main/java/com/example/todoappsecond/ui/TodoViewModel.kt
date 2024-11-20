package com.example.todoappsecond.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappsecond.domain.TodoItem
import com.example.todoappsecond.data.repository.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoItemsRepository) : ViewModel() {

    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage: SharedFlow<String?> = _errorMessage

    private var currentRevision: Int = 0

    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            val result = repository.getTodoItems(currentRevision)
            result.onSuccess { data ->
                _todoList.value = data
            }.onFailure { exception ->
                _errorMessage.emit("Ошибка загрузки задач: ${exception.message}")
            }
        }
    }

    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            val result = repository.addTodoItem(todoItem, currentRevision)
            result.onSuccess {
                loadTodos()
            }.onFailure { exception ->
                _errorMessage.emit("Ошибка добавления задачи: ${exception.message}")
            }
        }
    }

    fun deleteTodoItem(itemId: String) {
        viewModelScope.launch {
            val result = repository.deleteTodoItem(itemId, currentRevision)
            result.onSuccess {
                loadTodos()
            }.onFailure { exception ->
                _errorMessage.emit("Ошибка удаления задачи: ${exception.message}")
            }
        }
    }
}
