package com.example.Proyecto.layouts.loginScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.Proyecto.layouts.loginScreen.presentation.LoginScreenRoute
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
