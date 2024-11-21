package com.example.Proyecto.layouts.forgotPasswordScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.Proyecto.layouts.forgotPasswordScreen.presentation.ForgotPasswordRoute
import kotlinx.serialization.Serializable

@Serializable
data object ForgotPasswordDestination

fun NavController.navigateToForgotPasswordScreen(navOptions: NavOptions? = null) {
    this.navigate(ForgotPasswordDestination, navOptions)
}

fun NavGraphBuilder.forgotPasswordScreen(
    onBack: () -> Unit
) {
    composable<ForgotPasswordDestination> {
        ForgotPasswordRoute(
            onBack = onBack
        )
    }
}