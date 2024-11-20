package com.example.todoappsecond.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoappsecond.data.repository.TodoItemsRepository
import com.example.todoappsecond.network.NetworkModule
import com.example.todoappsecond.ui.TodoViewModel
import com.example.todoappsecond.ui.TodoViewModelFactory
import com.example.todoappsecond.ui.newTaskScreen.NewTaskScreen
import com.example.todoappsecond.ui.todoListScreen.TodoListScreen

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val apiService = NetworkModule.provideTodoApiService()
    val repository = TodoItemsRepository(apiService)
    val todoViewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(repository))

    NavHost(navController = navController, startDestination = "todo_list") {
        composable(route = "todo_list") {
            TodoListScreen(
                navController = navController,
                onAddTaskClick = {
                    navController.navigate("new_task")
                },
                viewModel = todoViewModel
            )
        }
        composable(route = "new_task") {
            NewTaskScreen(
                navController = navController,
                oneNewTodoItem = { newTask ->
                    todoViewModel.addTodoItem(newTask)
                }
            )
        }
    }
}
