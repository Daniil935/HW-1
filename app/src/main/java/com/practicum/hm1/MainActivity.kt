package com.practicum.hm1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                UserForm()
            }
        }
    }
}

@Composable
fun UserForm() {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("male") }
    var isNewsletterChecked by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }

    var showResult by remember { mutableStateOf(false) }

    // ✅ переменные для зафиксированных данных после отправки
    var submittedName by remember { mutableStateOf("") }
    var submittedAge by remember { mutableStateOf("") }
    var submittedGender by remember { mutableStateOf("male") }
    var submittedNewsletter by remember { mutableStateOf(false) }

    val accentColor = Color(0xFF4CAF50)
    val backgroundColor = Color(0xFFF1F8E9)
    val errorColor = Color(0xFFD32F2F)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()), // ✅ добавлена прокрутка
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Анкета пользователя",
            fontSize = 24.sp,
            color = accentColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Имя
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") },
            isError = nameError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError.isNotEmpty()) {
            Text(text = nameError, color = errorColor, fontSize = 12.sp)
        }

        // Возраст
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Возраст") },
            isError = ageError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (ageError.isNotEmpty()) {
            Text(text = ageError, color = errorColor, fontSize = 12.sp)
        }

        // Пол
        Text("Пол:", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .selectable(
                        selected = selectedGender == "male",
                        onClick = { selectedGender = "male" }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedGender == "male",
                    onClick = { selectedGender = "male" },
                    colors = RadioButtonDefaults.colors(selectedColor = accentColor)
                )
                Text(text = "Мужской", modifier = Modifier.padding(start = 4.dp))
            }

            Row(
                modifier = Modifier
                    .selectable(
                        selected = selectedGender == "female",
                        onClick = { selectedGender = "female" }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedGender == "female",
                    onClick = { selectedGender = "female" },
                    colors = RadioButtonDefaults.colors(selectedColor = accentColor)
                )
                Text(text = "Женский", modifier = Modifier.padding(start = 4.dp))
            }
        }

        // Подписка
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isNewsletterChecked,
                onCheckedChange = { isNewsletterChecked = it },
                colors = CheckboxDefaults.colors(checkedColor = accentColor)
            )
            Text(text = "Хочу получать новости", modifier = Modifier.padding(start = 8.dp))
        }

        // Кнопка отправки
        Button(
            onClick = {
                nameError = ""
                ageError = ""
                var hasErrors = false

                if (name.isBlank()) {
                    nameError = "Имя обязательно"
                    hasErrors = true
                }

                if (age.isBlank()) {
                    ageError = "Возраст обязателен"
                    hasErrors = true
                } else {
                    val ageValue = age.toIntOrNull()
                    if (ageValue == null || ageValue < 1 || ageValue > 100) {
                        ageError = "Введите корректный возраст (1–100)"
                        hasErrors = true
                    }
                }

                if (!hasErrors) {
                    // ✅ сохраняем данные только при успешной отправке
                    submittedName = name
                    submittedAge = age
                    submittedGender = selectedGender
                    submittedNewsletter = isNewsletterChecked
                    showResult = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text("Отправить", color = Color.White, fontSize = 16.sp)
        }

        // Отображение результата
        if (showResult) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Результат анкеты",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )

                    val ageValue = submittedAge.toIntOrNull() ?: 0

                    Text("Имя: $submittedName")
                    Text("Возраст: $ageValue")
                    Text(
                        text = if (submittedGender == "male") {
                            "Пол: Мужской"
                        } else {
                            "Пол: Женский"
                        }
                    )

                    Text(
                        text = if (submittedNewsletter) {
                            "Подписка: да ✅"
                        } else {
                            "Подписка: нет ❌"
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserForm() {
    MaterialTheme {
        UserForm()
    }
}