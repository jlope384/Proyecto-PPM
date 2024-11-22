package com.example.Proyecto.layouts.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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

@Composable
fun ProfileScreenRoute(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    ProfileScreen(
        onBack = onBack,
        onLogout = onLogout
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            ProfileItem(label = "Contraseña", value = "***********", isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLogout) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, isPassword: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(color = Color.LightGray, shape = MaterialTheme.shapes.medium)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            if (isPassword) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier
                                .padding(horizontal = 4.dp),
                                contentAlignment = Alignment.CenterStart) {
                                if (value.isEmpty()) {
                                    Text("Ingrese una contraseña", style = MaterialTheme.typography.bodyMedium)
                                } else {
                                    innerTextField()
                                }
                            }
                        }
                    )
                    Button(onClick = {  }) {
                        Text("Cambiar")
                    }
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MaterialTheme {
        ProfileScreen(onBack = {}, onLogout = {})
    }
}