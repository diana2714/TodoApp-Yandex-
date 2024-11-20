package com.example.todoappsecond.ui.newTaskScreen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoappsecond.R
import java.text.SimpleDateFormat
import com.example.todoappsecond.domain.TodoItem
import java.util.*

@Composable
fun NewTaskScreen(
    navController: NavController,
    oneNewTodoItem: (TodoItem) -> Unit,
    model: NewTaskScreenModel = remember { NewTaskScreenModel() }
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Заголовок и кнопка "Сохранить"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Закрыть",
                    modifier = Modifier.size(14.dp)
                )
            }
            TextButton(
                onClick = {
                    val newTask = TodoItem(
                        id = UUID.randomUUID().toString(),
                        text = model.taskText.value,
                        isCompleted = false,
                        importance = model.taskImportance,
                        createdAt = Date(),
                        deadline = model.selectedDate.value
                    )
                    oneNewTodoItem(newTask)
                    model.reset() // Сбрасываем модель
                    navController.popBackStack()
                },
                enabled = model.taskText.value.isNotBlank()
            ) {
                Text(
                    text = stringResource(R.string.saveTask),
                    color = if (model.taskText.value.isNotBlank()) Color(0xFF007AFF) else Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для ввода текста
        OutlinedTextField(
            value = model.taskText.value,
            onValueChange = { model.taskText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = stringResource(R.string.NeedSmthTodo),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Блок "Важность" с выпадающим меню
        Text(text = stringResource(R.string.Importance), fontSize = 16.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { model.expanded.value = true }
                .padding(top = 4.dp)
        ) {
            Text(
                text = model.importance.value,
                fontSize = 16.sp,
                color = if (model.importance.value == "!! Высокий") Color.Red else Color.Gray
            )

            DropdownMenu(
                expanded = model.expanded.value,
                onDismissRequest = { model.expanded.value = false },
                offset = DpOffset(x = 0.dp, y = 8.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.No)) },
                    onClick = {
                        model.importance.value = "Нет"
                        model.expanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.Low)) },
                    onClick = {
                        model.importance.value = "Низкий"
                        model.expanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.High), color = Color.Red) },
                    onClick = {
                        model.importance.value = "!! Высокий"
                        model.expanded.value = false
                    }
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Блок "Сделать до" с переключателем и выбранной датой
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = stringResource(R.string.DoUntill), fontSize = 16.sp)
                model.selectedDate.value?.let {
                    Text(
                        text = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it),
                        color = Color(0xFF007AFF),
                        fontSize = 16.sp
                    )
                }
            }
            Switch(
                checked = model.isDeadlineSet.value,
                onCheckedChange = {
                    model.isDeadlineSet.value = it
                    if (it) {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                                calendar.set(year, month, dayOfMonth)
                                model.selectedDate.value = calendar.time
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()
                    } else {
                        model.selectedDate.value = null
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.Gray,
                    checkedTrackColor = Color(0xFF007AFF),
                    uncheckedTrackColor = Color(0x33007AFF)
                )
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Кнопка "Удалить"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { model.reset() }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val deleteColor = if (model.taskText.value.isEmpty()) Color.Gray else Color.Red
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Удалить",
                tint = deleteColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.Delete),
                color = deleteColor,
                fontSize = 20.sp,
            )
        }
    }
}