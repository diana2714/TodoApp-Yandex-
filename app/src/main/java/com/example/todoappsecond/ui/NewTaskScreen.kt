import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoappsecond.R
import java.text.SimpleDateFormat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.todoappsecond.data.model.Importance
import com.example.todoappsecond.data.model.TodoItem
import java.util.*

@Composable
fun NewTaskScreen(navController: NavController, oneNewTodoItem: (TodoItem) -> Unit) {

    val taskText = remember { mutableStateOf("") }
    val importance = remember { mutableStateOf("Нет") }
    val isDeadlineSet = remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val taskImportance = when (importance.value) {
        "Нет" -> Importance.NORMAL
        "Низкий" -> Importance.LOW
        "!! Высокий" -> Importance.HIGH
        else -> Importance.NORMAL
    }

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
            IconButton(onClick = { navController.popBackStack() }) { // Закрыть экран
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Закрыть",
                    modifier = Modifier.size(14.dp)
                )
            }
            TextButton(onClick = {
                val newTask = TodoItem(
                    id = UUID.randomUUID().toString(),
                    text = taskText.value,
                    isCompleted = false,
                    importance = taskImportance,
                    createdAt = Date(),
                    deadline = selectedDate
                )
                oneNewTodoItem(newTask)
                navController.popBackStack() // Вернуться на предыдущий экран
            }) {
                Text(text = stringResource(R.string.saveTask), color = Color(0xFF007AFF), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для ввода текста
        OutlinedTextField(
            value = taskText.value,
            onValueChange = { newText ->
                taskText.value = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .shadow(
                    elevation = 4.dp, // Высота тени
                    shape = RoundedCornerShape(8.dp) // Скругленные углы
                )
                .background(Color.White, shape = RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = stringResource(R.string.NeedSmthTodo),
                    color = Color.Gray, // Цвет текста для placeholder
                    fontSize = 16.sp // Размер шрифта для placeholder
                )
            },

            )



        Spacer(modifier = Modifier.height(16.dp))

        // Блок "Важность" с выпадающим меню
        Text(text = stringResource(R.string.Importance), fontSize = 16.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(top = 4.dp)
        ) {
            Text(
                text = importance.value,
                fontSize = 16.sp,
                color = if (importance.value == "!! Высокий") Color.Red else Color.Gray
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 0.dp, y = 8.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.No)) },
                    onClick = {
                        importance.value = "Нет"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.Low)) },
                    onClick = {
                        importance.value = "Низкий"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.High), color = Color.Red) },
                    onClick = {
                        importance.value = "!! Высокий"
                        expanded = false
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
            Column(
                modifier = Modifier.padding(bottom = 50.dp) // Добавляем нижний отступ
            ) {
                Text(text = stringResource(R.string.DoUntill), fontSize = 16.sp)
                selectedDate?.let {
                    Text(
                        text = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(it),
                        color = Color(0xFF007AFF),
                        fontSize = 16.sp
                    )
                }
            }
            Switch(
                checked = isDeadlineSet.value,
                onCheckedChange = {
                    isDeadlineSet.value = it
                    if (it) {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                                calendar.set(year, month, dayOfMonth)
                                selectedDate = calendar.time
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()
                    } else {
                        selectedDate = null // сбросить выбранную дату, если переключатель выключен
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.Gray,
                    checkedTrackColor = Color(0xFF007AFF),
                    uncheckedTrackColor = Color(0x33007AFF)
                ),
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Кнопка "Удалить"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Action to delete the task */ }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val deleteColor = if (taskText.value.isEmpty()) Color.Gray else Color.Red
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

