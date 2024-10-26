package com.example.proyecto.layouts.fillFormScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto.layouts.createFormScreen.FormItem
import com.example.proyecto.layouts.createFormScreen.FormItemType
import com.example.proyecto.ui.theme.ProyectoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillFormScreen(formItems: List<FormItem>, onSubmit: (Map<String, String>) -> Unit) {
    val responses = remember { mutableStateMapOf<String, String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Llenar Formulario", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp),
                text = "Mi Formulario",
                style = MaterialTheme.typography.headlineLarge
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(formItems) { item ->
                    FillFormItemComponent(
                        item = item,
                        response = responses[item.question] ?: "",
                        onResponseChanged = { response ->
                            responses[item.question] = response
                        }
                    )
                }
            }
            Button(
                onClick = { onSubmit(responses.toMap()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Enviar Respuestas")
            }
        }
    }
}

@Composable
fun FillFormItemComponent(
    item: FormItem,
    response: String,
    onResponseChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.question, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            when (item.type) {
                FormItemType.ShortAnswer -> OutlinedTextField(
                    value = response,
                    onValueChange = onResponseChanged,
                    label = { Text("Respuesta corta") },
                    modifier = Modifier.fillMaxWidth()
                )
                FormItemType.LongAnswer -> OutlinedTextField(
                    value = response,
                    onValueChange = onResponseChanged,
                    label = { Text("Respuesta larga") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                FormItemType.MultipleChoice -> {
                    Column {
                        listOf("Opción 1", "Opción 2", "Opción 3").forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = response == option,
                                    onClick = { onResponseChanged(option) }
                                )
                                Text(option)
                            }
                        }
                    }
                }
                FormItemType.Scale -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        (1..5).forEach { number ->
                            Button(
                                onClick = { onResponseChanged(number.toString()) },
                                modifier = Modifier.size(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (response == number.toString()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(number.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FillFormScreenPreview() {
    val sampleFormItems = listOf(
        FormItem(FormItemType.ShortAnswer, "¿Cuál es tu nombre?"),
        FormItem(FormItemType.LongAnswer, "Describe tu experiencia con nuestra app"),
        FormItem(FormItemType.MultipleChoice, "¿Cuál es tu color favorito?"),
        FormItem(FormItemType.Scale, "¿Qué tan satisfecho estás con nuestro servicio?")
    )

    ProyectoTheme {
        FillFormScreen(formItems = sampleFormItems, onSubmit = {})
    }
}