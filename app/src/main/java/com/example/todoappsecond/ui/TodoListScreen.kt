package com.example.to_doapp.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.to_doapp.R
import com.example.to_doapp.data.model.Importance
import com.example.to_doapp.data.model.TodoItem
import java.util.Date

@Composable
fun TodoListScreen(
    navController: NavController,
    todoItems: List<TodoItem>,
    onTaskStatusChange: (TodoItem, Boolean) -> Unit,
    onAddTaskClick: () -> Unit
) {
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

//            Surface(
//            color = MaterialTheme.colorScheme.surface,
//            shape = MaterialTheme.shapes.medium,
//            shadowElevation = 9.dp,
//            modifier = Modifier
//                .heightIn(min = 0.dp, max = (todoItems.size * 60).dp)
//                .fillMaxWidth()
//
//        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(
                        elevation = 4.dp, // Высота тени
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.White, shape = RoundedCornerShape(8.dp)) // Цвет фона и форма
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
                                onTaskStatusChange(todoItem, isChecked)
                            }
                        )
                    }
                }
            }
            //}
        }
        FloatingActionButton(
            onClick = { navController.navigate("new_task") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp),
            containerColor = Color(0xFF007AFF), // Задаем синий цвет фона
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить задачу")
        }
    }
}


//@Preview
//@Composable
//fun PreviewTodoListScreen() {
//    val sampleTasks = listOf(
//        TodoItem(id = "1", text = "Купить что-то", isCompleted = false, importance = Importance.NORMAL, createdAt = Date()),
//        TodoItem(id = "2", text = "Сделать что-то", isCompleted = true, importance = Importance.HIGH, createdAt = Date())
//    )
//    TodoListScreen(
//        todoItems = sampleTasks,
//        onTaskStatusChange = { _, _ -> },
//        onAddTaskClick = {} // Добавлен пустой обработчик
//    )
//}
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
        todoItems = sampleTasks,
        onTaskStatusChange = { _, _ -> }, // Пустая функция для предпросмотра
        onAddTaskClick = {}
    )
}
