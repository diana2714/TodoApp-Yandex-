package com.example.todoappsecond.ui

import androidx.lifecycle.*
import com.example.todoappsecond.data.model.TodoItem
import com.example.todoappsecond.data.repository.TodoItemsRepository
import com.example.todoappsecond.network.handle
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoItemsRepository) : ViewModel() {

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> = _todoList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadTodos()
    }

    private var currentRevision: Int = 0

    fun loadTodos() {
        viewModelScope.launch {
            repository.getTodoItems(currentRevision).handle(
                onSuccess = { data ->
                    _todoList.value = data
                    _errorMessage.value = null
                },
                onError = { exception ->
                    _errorMessage.value = exception.message
                }
            )
        }
    }

    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.addTodoItem(todoItem, currentRevision).handle( // Передаем currentRevision
                onSuccess = {
                    loadTodos()
                    _errorMessage.value = null
                },
                onError = { exception ->
                    _errorMessage.value = exception.message
                }
            )
        }
    }

    fun deleteTodoItem(itemId: String) {
        viewModelScope.launch {
            val revision = currentRevision
            repository.deleteTodoItem(itemId, revision).handle(
                onSuccess = {
                    loadTodos()
                    _errorMessage.value = null
                },
                onError = { exception ->
                    _errorMessage.value = exception.message
                }
            )
        }
    }
}
