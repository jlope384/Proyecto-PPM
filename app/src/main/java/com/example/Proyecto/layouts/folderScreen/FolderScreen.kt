package com.example.Proyecto.layouts.folderScreen

import FormDisplayViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.Proyecto.util.type.FormDisplayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDisplayScreen(
    onBack: () -> Unit,
    id: String,
    onCreateForm: (String?, String?) -> Unit,
    onFillForm: (String, String?) -> Unit
) {
    val viewModel: FormDisplayViewModel = viewModel()
    val formItems by viewModel.formItems.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showNoResultsMessage by viewModel.showNoResultsMessage.collectAsState()
    val folderTitle by viewModel.folderTitle.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(folderTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedItems.isNotEmpty()) {
                FloatingActionButton(onClick = { viewModel.deleteSelectedItems() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            } else {
                FloatingActionButton(onClick = { onCreateForm(null, id) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.searchItems(it) }
            )
            SortOptions(
                onSortByName = { viewModel.sortItemsByName() }
            )
            if (showNoResultsMessage) {
                Text(
                    "No hay ningún formulario para mostrar",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                FormList(
                    items = formItems,
                    selectedItems = selectedItems,
                    onItemSelect = { id, isSelected -> viewModel.toggleItemSelection(id, isSelected) },
                    onItemDelete = { id -> viewModel.deleteItem(id) },
                    onItemEdit = { id, newTitle -> viewModel.updateItemTitle(id, newTitle) },
                    onItemClick = { formId -> onFillForm(formId, id) },
                    currentFolderId = id
                )
            }
        }
    }
}

@Composable
fun FormList(
    items: List<FormDisplayItem>,
    selectedItems: Set<String>,
    onItemSelect: (String, Boolean) -> Unit,
    onItemDelete: (String) -> Unit,
    onItemEdit: (String, String) -> Unit,
    onItemClick: (String) -> Unit,
    currentFolderId : String
) {
    LazyColumn {
        items(items) { item ->
            if (item.folderId.equals(currentFolderId)) {
                FormItemRow(
                    item = item,
                    isSelected = selectedItems.contains(item.id),
                    onSelect = { isSelected -> onItemSelect(item.id, isSelected) },
                    onDelete = { onItemDelete(item.id) },
                    onEdit = { newTitle -> onItemEdit(item.id, newTitle) },
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        singleLine = true
    )
}

@Composable
fun SortOptions(onSortByName: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Ordenar por: ")

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Nombre") },
                    onClick = {
                        onSortByName()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FormItemRow(
    item: FormDisplayItem,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit,
    onClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(item.title) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onSelect(it) }
        )

        Icon(
            Icons.Default.List,
            contentDescription = "Form",
            modifier = Modifier.padding(start = 8.dp)
        )

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        IconButton(onClick = { showMenu = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Modificar nombre") },
                onClick = {
                    showMenu = false
                    showEditDialog = true
                }
            )
            DropdownMenuItem(
                text = { Text("Eliminar") },
                onClick = {
                    showMenu = false
                    onDelete()
                }
            )
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Modificar nombre") },
            text = {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Nuevo nombre") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onEdit(newTitle)
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}