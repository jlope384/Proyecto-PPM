import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.Proyecto.layouts.fillFormScreen.FillFormState
import com.example.Proyecto.layouts.fillFormScreen.FillFormUIEvent
import com.example.Proyecto.layouts.fillFormScreen.FillFormViewModel
import com.example.Proyecto.util.type.FormItemType

@Composable
fun FillFormRoute(
    onBack: () -> Unit,
    id: String
) {
    val viewModel: FillFormViewModel = viewModel()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = id) {
        viewModel.loadFromFirestore(id)
        println("trying to load")
    }

    FillFormScreen(
        formState = formState,
        onEvent = viewModel::onEvent,
        onExport = viewModel::exportResponses,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillFormScreen(
    formState: FillFormState,
    onEvent: (FillFormUIEvent) -> Unit,
    onExport: (Context) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = formState.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onExport(context) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Send, "Exportar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (formState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        if (formState.description.isNotEmpty()) {
                            Text(
                                text = formState.description,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    items(formState.items) { item ->
                        FormQuestionItem(
                            item = item,
                            response = formState.responses[item.id] ?: "",
                            onResponseChanged = { response ->
                                onEvent(FillFormUIEvent.ResponseChanged(item.id, response))
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = formState.error != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                formState.error?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            if (formState.isSubmitted) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Form compartido") },
                    text = { Text("Gracias por responder!") },
                    confirmButton = {
                        TextButton(onClick = onBack) {
                            Text("Cerrar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FormQuestionItem(
    item: com.example.Proyecto.util.type.FormItem,
    response: String,
    onResponseChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.question,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (item.type) {
                FormItemType.ShortAnswer -> {
                    OutlinedTextField(
                        value = response,
                        onValueChange = onResponseChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Su respuesta") }
                    )
                }
                FormItemType.LongAnswer -> {
                    OutlinedTextField(
                        value = response,
                        onValueChange = onResponseChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Su respuesta") },
                        minLines = 3
                    )
                }
                FormItemType.MultipleChoice -> {
                    item.options?.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = response == option,
                                onClick = { onResponseChanged(option) }
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
                                onClick = { onResponseChanged(value) },
                                modifier = Modifier.size(40.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (response == value)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text(value)
                            }
                        }
                    }
                }
            }
        }
    }
}