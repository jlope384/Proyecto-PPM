package com.example.Proyecto.layouts.startScreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.Proyecto.R
import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.StartDropdownItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import com.example.Proyecto.util.type.FormDisplayItem


@Composable
fun StartScreenRoute(
    onProfileClick: () -> Unit,
    onFolderClick: (String) -> Unit,
    onFillFormClick: (String, String?) -> Unit,
    onEditFormClick: (String?, String?) -> Unit
) {
    val viewModel: StartScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by viewModel.startState.collectAsStateWithLifecycle()
    StartScreen(
        state = state,
        onProfileClick = onProfileClick,
        onFolderClick = onFolderClick,
        onFillFormClick = onFillFormClick,
        onEditFormClick = onEditFormClick,
        onRetry = { viewModel.retryLoading() },
        onFolderCreate = { title -> viewModel.createFolder(title) },
        onFormCreate = {onEditFormClick(null, null)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    state: StartScreenState,
    onProfileClick: () -> Unit,
    onFolderClick: (String) -> Unit,
    onFillFormClick: (String, String?) -> Unit,
    onEditFormClick: (String?, String?) -> Unit,
    onRetry: () -> Unit,
    onFolderCreate: (String) -> Unit,
    onFormCreate: () -> Unit
) {
    var showAddItemDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Inicio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddItemDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        println("El state es")
        println(state.isLoading)
        when (state.isLoading) {
            true -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                if (state.error != null) {
                    ErrorScreen(message = state.error!!, onRetry = onRetry)
                } else {
                    ContentScreen(
                        folders = state.folders,
                        forms = state.forms,
                        onFolderClick = onFolderClick,
                        onFillFormClick = onFillFormClick,
                        onEditFormClick = onEditFormClick,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onAddItem = { isFolder, title ->
                showAddItemDialog = false
                if (isFolder) {
                    onFolderCreate(title)
                    println("Added Folder: $title")
                } else {
                    onFormCreate()
                    println("Added Form: $title")
                }
            }
        )
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun ContentScreen(
    folders: List<FolderItem>,
    forms: List<FormDisplayItem>,
    onFolderClick: (String) -> Unit,
    onFillFormClick: (String, String?) -> Unit,
    onEditFormClick: (String?, String?) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            text = "Tus Archivos",
            style = MaterialTheme.typography.headlineLarge
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                folders.forEach { folder: FolderItem ->
                    item { ldFolder(onFolderClick = onFolderClick, id = folder.id, name = folder.title) }
                }
                forms.forEach { form: FormDisplayItem ->
                    item {
                        when(form.folderId){
                            null -> ldForms(
                                onFillFormClick = onFillFormClick,
                                onEditFormClick = onEditFormClick,
                                id = form.id,
                                name = form.title
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ldForms(
    onFillFormClick: (String, String?) -> Unit,
    onEditFormClick: (String?, String?) -> Unit,
    id: String,
    name: String)
{
    val dropdownItems: List<StartDropdownItem> = listOf(
        StartDropdownItem("Editar"),
        StartDropdownItem("Llenar")
    )

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    var position by remember {
        mutableStateOf(Offset(0F, 0F))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, )
    {
        Image(painter = painterResource(id = R.drawable.ldform), contentDescription = "Form",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(100.dp)
                .onGloballyPositioned { coordinates ->
                    position = coordinates.positionInWindow()
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        isContextMenuVisible = true
                        pressOffset = DpOffset(position.x.toDp(), position.y.toDp())
                        itemHeight = offset.y.toDp()
                    }
                }

        )
        Text(text = name, style = MaterialTheme.typography.labelLarge)
    }
    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = {
            isContextMenuVisible = false
        },
        offset = pressOffset.copy(
            y = pressOffset.y - itemHeight
        )
    ) {
        dropdownItems.forEach {
            DropdownMenuItem(onClick = {
                when(it.Option){
                    "Editar" -> onEditFormClick(id, null)
                    "Llenar" -> onFillFormClick(id, null)
                }
                isContextMenuVisible = false
            }, text = {Text(text = it.Option)})
        }
    }
}

@Composable
fun ldFolder(onFolderClick: (String) -> Unit, id: String, name: String) {
    Column(modifier = Modifier
        ,horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.ldfolder), contentDescription = "Folder",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(100.dp)
                .clickable { onFolderClick(id) }
        )
        Text(text = name, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (isFolder: Boolean, title: String) -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var title by remember { mutableStateOf("") }
    val options = listOf("Folder", "Form")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Item") },
        text = {
            Column {
                Text(text = "Select the type:")
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = option }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option }
                        )
                        Text(text = option)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedOption?.let { option ->
                        onAddItem(option == "Folder", title)
                    }
                },
                enabled = selectedOption != null && title.isNotBlank()
            ) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

