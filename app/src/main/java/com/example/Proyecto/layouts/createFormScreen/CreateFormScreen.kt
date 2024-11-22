package com.example.Proyecto.layouts.createFormScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.Proyecto.util.type.FormItem
import com.example.Proyecto.util.type.FormItemType
import org.burnoutcrew.reorderable.*

@Composable
fun CreateFormRoute(
    onCreateFormSuccess: () -> Unit,
    id: String? = null,
    folderId: String? = null,
    onBack: () -> Unit
) {
    val viewModel: CreateFormViewModel = viewModel()
    val formState by viewModel.formState.collectAsState()
    val validationErrors by viewModel.validationErrors.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = id) {
        println("CreateFormRoute: id = $folderId")
        viewModel.loadFromFirestore(id.toString())
    }

    CreateFormScreen(
        formState = formState,
        validationErrors = validationErrors,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onSuccess = onCreateFormSuccess,
        onSave = viewModel::saveForm,
        folderId = folderId
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFormScreen(
    formState: FormState,
    validationErrors: List<String>,
    onEvent: (FormUIEvent) -> Unit,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onSave: (String?) -> Unit,
    folderId: String? = null
) {
    var showAddItemDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
        onEvent(FormUIEvent.ItemMoved(from.index, to.index))
    })

    LaunchedEffect(formState.isSaved) {
        if (formState.isSaved) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Crear Formulario",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Last modified: ${formState.lastModified}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        println("folder id es en ui")
                        println(folderId)
                        onSave(folderId)
                    }) {
                        Icon(Icons.Default.Done, "Save")
                    }
                    IconButton(onClick = { onEvent(FormUIEvent.ExportForm) }) {
                        Icon(Icons.Default.Share, "Export")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddItemDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, "Add Question")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Form Header
            OutlinedTextField(
                value = formState.title,
                onValueChange = { onEvent(FormUIEvent.TitleChanged(it)) },
                label = { Text("Form Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = formState.description,
                onValueChange = { onEvent(FormUIEvent.DescriptionChanged(it)) },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Validation Errors
            AnimatedVisibility(
                visible = validationErrors.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Porfavor corrija los siguientes errores:",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.titleSmall
                        )
                        validationErrors.forEach { error ->
                            Text(
                                "• $error",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Form Items
            LazyColumn(
                state = reorderState.listState,
                modifier = Modifier
                    .weight(1f)
                    .reorderable(reorderState)
            ) {
                itemsIndexed(
                    items = formState.items,
                    key = { _, item -> item.id }
                ) { index, item ->
                    ReorderableItem(reorderState, key = item.id) { isDragging ->
                        FormItemComponent(
                            item = item,
                            onDelete = { onEvent(FormUIEvent.ItemRemoved(item.id)) },
                            onUpdate = { question, options ->
                                onEvent(FormUIEvent.ItemUpdated(item.id, question, options))
                            },
                            isDragging = isDragging
                        )
                    }
                }
            }
        }

        if (showAddItemDialog) {
            AddItemDialog(
                onDismiss = { showAddItemDialog = false },
                onAddItem = { type, question ->
                    onEvent(FormUIEvent.ItemAdded(type, question))
                    showAddItemDialog = false
                }
            )
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Seguro que desea salir?") },
                text = { Text("Asegurese de haber guardado su formulario.") },
                confirmButton = {
                    TextButton(onClick = onBack) {
                        Text("Salir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormItemComponent(
    item: FormItem,
    onDelete: () -> Unit,
    onUpdate: (String, List<String>?) -> Unit,
    isDragging: Boolean
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = if (isDragging) 2.dp else 0.dp,
                color = if (isDragging) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragging) 8.dp else 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Drag to reorder",
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "${item.type.name}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, "Edit question")
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, "More options")
                    }
                }
            }

            Text(
                text = item.question,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            when (item.type) {
                FormItemType.ShortAnswer -> OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Ejemplo de respuesta corta") },
                    modifier = Modifier.fillMaxWidth()
                )

                FormItemType.LongAnswer -> OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Ejemplo de respuesta larga") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                FormItemType.MultipleChoice -> {
                    item.options?.forEach { option ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = false,
                                onClick = {},
                                enabled = false
                            )
                            Text(
                                text = option,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                FormItemType.Scale -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        item.options?.forEach { value ->
                            FilledTonalButton(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text(value)
                            }
                        }
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        expanded = false
                        showEditDialog = true
                    },
                    leadingIcon = { Icon(Icons.Default.Edit, null) }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    onClick = {
                        expanded = false
                        onDelete()
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, null) }
                )
                if (item.type == FormItemType.MultipleChoice) {
                    DropdownMenuItem(
                        text = { Text("Editar Opciones") },
                        onClick = {
                            expanded = false
                            showEditDialog = true
                        },
                        leadingIcon = { Icon(Icons.Default.List, null) }
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        EditItemDialog(
            item = item,
            onDismiss = { showEditDialog = false },
            onUpdate = { question, options ->
                onUpdate(question, options)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditItemDialog(
    item: FormItem,
    onDismiss: () -> Unit,
    onUpdate: (String, List<String>?) -> Unit
) {
    var question by remember { mutableStateOf(item.question) }
    var options by remember { mutableStateOf(item.options ?: emptyList()) }
    var newOption by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Pregunta") },
        text = {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Pregunta") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (item.type == FormItemType.MultipleChoice) {
                    Text(
                        text = "Options",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    options.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = option,
                                onValueChange = { newValue ->
                                    options = options.toMutableList().also {
                                        it[index] = newValue
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    options = options.toMutableList().also {
                                        it.removeAt(index)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Delete, "Remove option")
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newOption,
                            onValueChange = { newOption = it },
                            label = { Text("Nueva Opcion") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (newOption.isNotBlank()) {
                                    options = options + newOption
                                    newOption = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, "Add option")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(question, if (item.type == FormItemType.MultipleChoice) options else null)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (FormItemType, String) -> Unit
) {
    var selectedType by remember { mutableStateOf(FormItemType.ShortAnswer) }
    var question by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar nueva Pregunta") },
        text = {
            Column {
                OutlinedTextField(
                    value = question,
                    onValueChange = {
                        question = it
                        error = ""
                    },
                    label = { Text("Pregunta") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    supportingText = if (error.isNotEmpty()) {
                        { Text(error) }
                    } else null
                )

                Text(
                    "Tipo de pregunta:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                FormItemType.entries.forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType = type }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == selectedType,
                            onClick = { selectedType = type }
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(type.name)
                            Text(
                                when (type) {
                                    FormItemType.ShortAnswer -> "Pregunta corta"
                                    FormItemType.LongAnswer -> "Pregunta larga"
                                    FormItemType.MultipleChoice -> "Opciones multiples"
                                    FormItemType.Scale -> "Escala (1-5)"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (question.isBlank()) {
                        error = "Pregunta no puede estar vacia"
                        return@Button
                    }
                    onAddItem(selectedType, question)
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}