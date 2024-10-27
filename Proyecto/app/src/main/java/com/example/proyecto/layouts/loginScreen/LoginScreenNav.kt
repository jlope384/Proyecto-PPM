package com.example.proyecto.layouts.loginScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen(
    onLoginClick: () -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
) {
    composable<LoginDestination> {
        LoginScreenRoute(
            onLoginClick = onLoginClick,
            onForgotPassword = onForgotPassword,
            onRegister = onRegister
        )
    }
}