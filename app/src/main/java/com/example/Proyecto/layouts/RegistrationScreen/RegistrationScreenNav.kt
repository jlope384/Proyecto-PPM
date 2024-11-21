package com.example.Proyecto.layouts.RegistrationScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object RegistrationDestination

fun NavController.navigateToRegistrationScreen(navOptions: NavOptions? = null) {
    this.navigate(RegistrationDestination, navOptions)
}
fun NavGraphBuilder.registrationScreen(
    onBack: () -> Unit
) {
    composable<RegistrationDestination> {
        RegisterRoute(
            onBack = onBack
        )
    }
}
