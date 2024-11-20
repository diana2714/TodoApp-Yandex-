package com.example.todoappsecond.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.to_doapp.ui.TodoItemCell
import com.example.todoappsecond.R
import com.example.todoappsecond.data.model.Importance
import com.example.todoappsecond.data.model.TodoItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun TodoListScreen(
    navController: NavController,
    onAddTaskClick: () -> Unit,
    viewModel: TodoViewModel
) {
    val todoItems by viewModel.todoList.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.asFlow().collectLatest {
            scope.launch {
                snackbarHostState.showSnackbar(it.orEmpty())
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("new_task") },
                modifier = Modifier
//                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 16.dp),
                containerColor = Color(0xFF007AFF),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить задачу")
            }
        }
    ) { padding ->
        Box {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFFF7F6F2))
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.MyDoings),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                )

                Text(
                    text = stringResource(R.string.Done, todoItems.count { it.isCompleted }),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(todoItems) { todoItem ->
                            TodoItemCell(
                                todoItem = todoItem,
                                onCheckedChange = { isChecked ->
                                    viewModel.addTodoItem(todoItem.copy(isCompleted = isChecked))
                                }
                            )
                        }
                    }
                }

                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoListScreen() {
    val sampleTasks = listOf(
        TodoItem(
            id = "1",
            text = "Купить что-то",
            isCompleted = false,
            importance = Importance.NORMAL,
            createdAt = Date()
        ),
        TodoItem(
            id = "2",
            text = "Сделать что-то",
            isCompleted = true,
            importance = Importance.HIGH,
            createdAt = Date()
        )
    )
    TodoListScreen(
        navController = rememberNavController(),
        onAddTaskClick = {},
        viewModel = viewModel()
    )
}
