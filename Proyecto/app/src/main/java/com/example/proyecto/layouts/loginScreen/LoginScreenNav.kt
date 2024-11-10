package com.example.proyecto.layouts.loginScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
) {
    composable<LoginDestination> {
        LoginScreenRoute(
            onLoginSuccess = onLoginSuccess,
            onForgotPassword = onForgotPassword,
            onRegister = onRegister
        )
    }
}
