package com.example.proyecto.layouts.RegistrationScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.proyecto.layouts.fillFormScreen.FillFormDestination
import com.example.proyecto.layouts.fillFormScreen.FillFormRoute
import com.example.proyecto.layouts.forgotPasswordScreen.ForgotPasswordDestination
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
