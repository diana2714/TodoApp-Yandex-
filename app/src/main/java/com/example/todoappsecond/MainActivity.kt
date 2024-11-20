package com.example.to_doapp

import NewTaskScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_doapp.data.repository.TodoItemsRepository
import com.example.to_doapp.ui.TodoListScreen


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

    // Инициализируем репозиторий и получаем задачи
    val todoRepository = TodoItemsRepository()
    val todoItems = remember { mutableStateOf(todoRepository.generateSampleTodoItems()) } // Получаем задачи из репозитория

    NavHost(navController = navController, startDestination = "todo_list") {
        composable(route = "todo_list") {
            TodoListScreen(
                navController = navController,
                todoItems = todoItems.value,
                onTaskStatusChange = { todoItem, isChecked ->
                    todoItems.value = todoItems.value.map { it ->
                        if (it.id == todoItem.id) it.copy(isCompleted = isChecked) else it
                    }
                },
                onAddTaskClick = {
                    navController.navigate("new_task")
                }
            )
        }
        composable(route = "new_task") {
            NewTaskScreen(navController = navController, oneNewTodoItem = { newTask ->
                todoItems.value = todoItems.value + newTask
            }
            )
        }
    }
}
