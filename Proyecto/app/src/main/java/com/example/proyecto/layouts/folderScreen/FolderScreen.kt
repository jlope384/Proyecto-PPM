import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto.util.type.FolderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(onBack: () -> Unit, viewModel: FolderScreenViewModel = viewModel()) {
    val folderItems by viewModel.folderItems.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showNoResultsMessage by viewModel.showNoResultsMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prueba 1") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Accion de editar carpeta o formulario */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
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
                FloatingActionButton(onClick = { /* Navegar a la pantalla de crear formulario */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.searchItems(it) }
            )
            SortOptions(
                onSortByName = { viewModel.sortItemsByName() },
                onSortByDate = { viewModel.sortItemsByDate() }
            )
            if (showNoResultsMessage) {
                Text("No existe ningún formulario con ese nombre", modifier = Modifier.padding(16.dp))
            } else {
                FolderList(
                    items = folderItems,
                    selectedItems = selectedItems,
                    onItemSelect = { id, isSelected ->
                        viewModel.toggleSelectAll(isSelected)
                    },
                    onItemDelete = { id -> viewModel.deleteItem(id) },
                    onItemEdit = { id, newTitle -> viewModel.updateItemTitle(id, newTitle) }
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
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        }
    )
}

@Composable
fun SortOptions(onSortByName: () -> Unit, onSortByDate: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = { /* Acción de seleccionar todo */ }
        )
        Text(text = "Seleccionar todo", modifier = Modifier.padding(start = 8.dp))

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
                    text = { Text("Fecha de modificación") },
                    onClick = {
                        onSortByDate()
                        expanded = false
                    }
                )
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
fun FolderList(
    items: List<FolderItem>,
    selectedItems: Set<Int>,
    onItemSelect: (Int, Boolean) -> Unit,
    onItemDelete: (Int) -> Unit,
    onItemEdit: (Int, String) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            FolderItemRow(
                item = item,
                isSelected = selectedItems.contains(item.id),
                onSelect = { isSelected -> onItemSelect(item.id, isSelected) },
                onDelete = { onItemDelete(item.id) },
                onEdit = { newTitle -> onItemEdit(item.id, newTitle) }
            )
        }
    }
}

@Composable
fun FolderItemRow(
    item: FolderItem,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(item.title) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(!isSelected) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.title.startsWith("Prueba")) {
            Icon(Icons.Default.Face, contentDescription = "Folder")
        } else {
            Icon(Icons.Default.List, contentDescription = "Formulario")
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = item.date, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.weight(1f))

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
                    // Mostrar un diálogo para editar el nombre
                    // Aquí puedes implementar un diálogo para editar el nombre
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
}