package com.example.proyecto.layouts.forgotPasswordScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.proyecto.layouts.fillFormScreen.FillFormDestination
import com.example.proyecto.layouts.fillFormScreen.FillFormRoute
import com.example.proyecto.navigation.MainNavGraph
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