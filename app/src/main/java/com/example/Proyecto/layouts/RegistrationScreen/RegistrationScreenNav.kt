package com.example.Proyecto.layouts.RegistrationScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.Proyecto.layouts.RegistrationScreen.presentation.RegisterRoute
import com.example.Proyecto.layouts.RegistrationScreen.presentation.RegisterViewModel
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
        val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
        RegisterRoute(
            viewModel = viewModel,
            onBack = onBack
        )
    }
}

