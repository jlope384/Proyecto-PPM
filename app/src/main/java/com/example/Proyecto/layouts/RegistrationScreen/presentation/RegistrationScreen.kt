package com.example.Proyecto.layouts.RegistrationScreen.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    RegisterScreen(
        uiState = uiState,
        onBack = onBack,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    uiState: RegisterScreenState,
    onBack: () -> Unit,
    onEvent: (RegisterEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .border(
                        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Email Field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    ) {
                        Text(text = "Email", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                        label = { Text("Email") },
                        placeholder = { Text("Ingrese su email") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    ) {
                        Text(text = "Contraseña", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                        placeholder = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (uiState.password.isNotEmpty()) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password Field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    ) {
                        Text(text = "Confirmar contraseña", style = MaterialTheme.typography.bodyLarge)
                    }
                    OutlinedTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                        placeholder = { Text("Confirmar contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (uiState.confirmPassword.isNotEmpty()) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Register Button
                    Button(
                        onClick = { onEvent(RegisterEvent.RegisterClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Registrarme", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    // Loading Indicator or Messages
                    if (uiState.isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Cargando...", style = MaterialTheme.typography.bodyMedium)
                    }
                    uiState.successMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                    uiState.errorMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        uiState = RegisterScreenState(),
        onBack = {},
        onEvent = {}
    )
}
