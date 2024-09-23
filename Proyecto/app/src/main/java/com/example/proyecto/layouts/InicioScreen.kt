package com.example.proyecto.layouts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto.R
import com.example.proyecto.ui.theme.Black
import com.example.proyecto.ui.theme.ProyectoTheme
import com.example.proyecto.util.LDItemDb


private val ldItemDb = LDItemDb()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = "Inicio",
                    fontWeight = FontWeight.Bold
                ) },
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
            FloatingActionButton(onClick = { /* Accion de crear nuevo formulario o carpeta */ },
                containerColor = MaterialTheme.colorScheme.secondary
                ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            Text(modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                ,text = "Tus Archivos", style = MaterialTheme.typography.headlineLarge)
            Box(modifier = Modifier
                .fillMaxSize()
            ){
                LazyVerticalGrid(horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(ldItemDb.generateRandomLDItems()) {item ->
                        when(item.type){
                            "Form" -> ldForms(name = item.name)
                            "Folder" -> ldFolder(name = item.name)
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ldForms(name: String){
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.ldform), contentDescription = "Form",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(100.dp)

        )
        Text(text = name, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ldFolder(name: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        ,horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.ldfolder), contentDescription = "Folder",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(100.dp)
        )
        Text(text = name, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    ProyectoTheme {
        InicioScreen()
    }
}
