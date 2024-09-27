package com.example.proyecto.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.theme.ProyectoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Perfil") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ProfileItem(label = "Email", value = "example@example.com")
            ProfileItem(label = "Nombre", value = "Juan Carlos")
            ProfileItem(label = "Apellido", value = "Durini")
            ProfileItem(label = "Contraseña", value = "***********", isPassword = true)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, isPassword: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Asegura que la Box ocupe todo el ancho disponible
            .padding(vertical = 8.dp) // Añade un poco de padding vertical para separación
            .background(
                color = Color.LightGray,
                shape = MaterialTheme.shapes.medium
            ) // Añade un fondo gris claro a cada ítem
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium) // Añade un borde gris claro a cada ítem
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // Padding interno para cada ítem
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start) // Alinea el texto a la izquierda dentro del Column
            )
            if (isPassword) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = value,
                        onValueChange = {}, // No cambiamos el valor
                        readOnly = true, // Hace que sea de solo lectura
                        visualTransformation = PasswordVisualTransformation(), // Oculta el texto
                        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier
                                .padding(horizontal = 4.dp),
                                contentAlignment = Alignment.CenterStart) {
                                if (value.isEmpty()) {
                                    Text("Ingrese una contraseña", style = MaterialTheme.typography.bodyMedium)
                                } else {
                                    innerTextField() // Muestra el textField modificado
                                }
                            }
                        }
                    )
                    Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary) ,onClick = { /* Handle change password */ }) {
                        Text("Cambiar")
                    }
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start) // Alinea el texto a la izquierda dentro del Column
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProyectoTheme {
        ProfileScreen()
    }
}
