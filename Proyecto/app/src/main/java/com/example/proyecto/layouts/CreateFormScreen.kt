package com.example.proyecto.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.theme.ProyectoTheme

enum class FormItemType {
    ShortAnswer,
    LongAnswer,
    MultipleChoice,
    Scale
}

data class FormItem(
    val type: FormItemType,
    val question: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFormScreen() {
    var formItems by remember { mutableStateOf(listOf<FormItem>()) }
    var showAddItemDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Creación de Formulario",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "backstack button"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary ,onClick = { showAddItemDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
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
                    FormItemComponent(item)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Button(onClick = { /* Save form logic */ }) {
                    Text("Guardar")
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(onClick = { /* Export form logic */ }) {
                    Text("Exportar")
                }
            }
        }
    }

    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onAddItem = { type, question ->
                formItems = formItems + FormItem(type, question)
                showAddItemDialog = false
            }
        )
    }
}

@Composable
fun FormItemComponent(item: FormItem) {
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
                    value = "",
                    onValueChange = {},
                    label = { Text("Respuesta corta") },
                    modifier = Modifier.fillMaxWidth()
                )
                FormItemType.LongAnswer -> OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Respuesta larga") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                FormItemType.MultipleChoice -> {
                    // Simplified multiple choice for demonstration
                    listOf("Opción 1", "Opción 2", "Opción 3").forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = false, onClick = {})
                            Text(option)
                        }
                    }
                }
                FormItemType.Scale -> {
                    // Simplified scale for demonstration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        (1..5).forEach { number ->
                            Button(
                                onClick = {},
                                modifier = Modifier.size(40.dp)
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

@Composable
fun AddItemDialog(onDismiss: () -> Unit, onAddItem: (FormItemType, String) -> Unit) {
    var selectedType by remember { mutableStateOf(FormItemType.ShortAnswer) }
    var question by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar nuevo elemento") },
        text = {
            Column {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Pregunta") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tipo de respuesta:")
                FormItemType.values().forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = type == selectedType,
                            onClick = { selectedType = type }
                        )
                        Text(type.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onAddItem(selectedType, question) }) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CreateFormScreenPreview() {
    ProyectoTheme {
        CreateFormScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun FormItemComponentPreview() {
    ProyectoTheme {
        Column {
            FormItemComponent(FormItem(FormItemType.ShortAnswer, "What is your name?"))
            FormItemComponent(FormItem(FormItemType.LongAnswer, "Describe your experience"))
            FormItemComponent(FormItem(FormItemType.MultipleChoice, "Choose your favorite color"))
            FormItemComponent(FormItem(FormItemType.Scale, "Rate your satisfaction"))
        }
    }
}