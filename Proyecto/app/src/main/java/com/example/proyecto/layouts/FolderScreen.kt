package com.example.proyecto.layouts

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prueba 1") },
                navigationIcon = {
                    IconButton(onClick = { /* Accion de regresar */ }) {
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
            FloatingActionButton(onClick = { /* Accion de crear nuevo formulario o carpeta */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(modifier = androidx.compose.ui.Modifier.padding(it)) {
            SearchBar()
            SortOptions()
            FolderList()
        }
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = { /* Actualizar valor del campo */ },
        placeholder = { Text("Buscar...") },
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        }
    )
}

@Composable
fun SortOptions() {
    var expanded by remember { mutableStateOf(false) } // Estado para controlar si el menú está expandido o no

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = androidx.compose.ui.Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = { /* Acción de seleccionar todo */ }
        )
        Text(text = "Seleccionar todo", modifier = androidx.compose.ui.Modifier.padding(start = 8.dp))

        Spacer(modifier = androidx.compose.ui.Modifier.weight(1f))

        Text(text = "Ordenar por: ")

        Box {
            // Botón que al hacer clic muestra el menú desplegable
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }

            // Menú desplegable
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false } // Cerrar el menú cuando se hace clic fuera
            ) {
                DropdownMenuItem(
                    text = { Text("Fecha de modificación") }, // Aquí pasamos el texto como un parámetro
                    onClick = {
                        /* Ordenar por Fecha */
                        expanded = false // Cerrar el menú al seleccionar
                    }
                )
                DropdownMenuItem(
                    text = { Text("Nombre") }, // Aquí pasamos el texto como un parámetro
                    onClick = {
                        /* Ordenar por Nombre */
                        expanded = false // Cerrar el menú al seleccionar
                    }
                )
            }
        }
    }
}



@Composable
fun FolderList() {
    val items = listOf(
        FolderItem("Prueba 2", "08:39:06 p. m. Septiembre 22, 2024"),
        FolderItem("Formulario 2", "08:34:50 p. m. Septiembre 22, 2024"),
        FolderItem("Formulario 1", "08:32:56 p. m. Septiembre 22, 2024")
    )

    LazyColumn {
        items(items) { item ->
            FolderItemRow(item)
        }
    }
}

data class FolderItem(val title: String, val date: String)

@Composable
fun FolderItemRow(item: FolderItem) {
    Row(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .clickable { /* Acción de abrir carpeta/formulario */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.title.startsWith("Prueba")) {
            Icon(Icons.Default.Face, contentDescription = "Folder")
        } else {
            Icon(Icons.Default.List, contentDescription = "Formulario")
        }

        Column(modifier = androidx.compose.ui.Modifier.padding(start = 16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = item.date, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = androidx.compose.ui.Modifier.weight(1f))

        IconButton(onClick = { /* Acción de más opciones */ }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FolderScreenPreview() {
    FolderScreen()
}