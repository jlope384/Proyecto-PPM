package com.example.proyecto.layouts.loginScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto.layouts.loginScreen.authentication.AuthState
import com.example.proyecto.layouts.loginScreen.loginScreenViewModel.AuthViewModel
import com.example.proyecto.util.screens.LoadingScreen


@Composable
fun LoginScreenRoute(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val authState by viewModel.authState.collectAsState()

    LoginScreen(
        authState = authState,
        onLoginClick = { email, password ->
            viewModel.login(email, password)
        },
        onForgotPassword = onForgotPassword,
        onRegister = onRegister,
        resetAuthState = viewModel::resetState
    )

    if (authState is AuthState.Success) {
        viewModel.resetState()
        onLoginSuccess()
    }
}



@Composable
fun LoginScreen(
    authState: AuthState,
    onLoginClick: (String, String) -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
    resetAuthState: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Inicia sesión en LabDocs", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is AuthState.Error -> Text(
                text = authState.message,
                color = MaterialTheme.colorScheme.error
            )
            AuthState.Loading -> CircularProgressIndicator()
            AuthState.Idle -> Unit
            AuthState.Success -> LoadingScreen()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                resetAuthState()
                onLoginClick(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        ClickableText(
            text = AnnotatedString("¿Olvidaste tu contraseña?"),
            onClick = { onForgotPassword() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ClickableText(
            text = AnnotatedString("¿Nuevo usuario? Regístrate"),
            onClick = { onRegister() }
        )
    }
}