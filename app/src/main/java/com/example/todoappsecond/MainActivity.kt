
package com.example.todoappsecond

import NewTaskScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoappsecond.data.repository.TodoItemsRepository
import com.example.todoappsecond.network.NetworkModule
import com.example.todoappsecond.ui.TodoListScreen
import com.example.todoappsecond.ui.TodoViewModel
import com.example.todoappsecond.ui.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    // Создаем экземпляры API-сервиса и репозитория
    val apiService = NetworkModule.provideTodoApiService()
    val repository = TodoItemsRepository(apiService) // Передаем apiService в конструктор репозитория

    // Инициализируем TodoViewModel с использованием фабрики
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
