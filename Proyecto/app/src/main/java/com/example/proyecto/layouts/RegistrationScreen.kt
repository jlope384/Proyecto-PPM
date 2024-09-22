package com.example.proyecto.layouts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Visibility
import com.example.proyecto.ui.theme.ProyectoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    // Controla si se muestra la contraseña

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Registrar",
                    style = MaterialTheme.typography.titleLarge
                ) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "backstack button"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rodea el contenido en un Box para agregar bordes y padding
            Box(
                modifier = Modifier
                    .padding(16.dp)  // Padding de la box
                    .border(
                        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)  // Padding del contenido de la box
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)  // Alinea el Box a la izquierda
                    ) {
                        Text(text = "Email", style = MaterialTheme.typography.bodyLarge)
                    }
                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("Ingrese su email") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Username field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)  // Alinea el Box a la izquierda
                    ) {
                        Text(text = "Usuario", style = MaterialTheme.typography.bodyLarge)
                    }
                    // Email field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        placeholder = { Text("Ingrese su usuario") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)  // Alinea el Box a la izquierda
                    ) {
                        Text(text = "Contraseña", style = MaterialTheme.typography.bodyLarge)
                    }

// Campo de contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text(text = "Contraseña")
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { }  // Cambiar el estado de visibilidad
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Cambiar el icono cuando sea funcional la pantalla e implementar logica
                                    contentDescription = "visibility"
                                )
                            }
                        },

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Register button
                    Button(
                        onClick = { /* TODO: Acción de registro */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Registrarme", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ProyectoTheme {
        RegisterScreen()
    }
}
