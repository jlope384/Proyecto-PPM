package com.example.proyecto.layouts.createFormScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


@Serializable
data class CreateFormDestination(val id: Int?)

fun NavController.navigateToCreateFormScreen(
    id: Int? = null,
    navOptions: NavOptions? = null
) {
    this.navigate(CreateFormDestination(id = id), navOptions)
}

fun NavGraphBuilder.createFormScreen(
    onBack: () -> Unit,
    onCreateFormClick: () -> Unit

    ) {
    composable<CreateFormDestination> { backStackEntry ->
        val backStackParams: CreateFormDestination = backStackEntry.toRoute()
        CreateFormRoute(id = backStackParams.id, onCreateFormSuccess = onCreateFormClick, onBack = onBack)
    }
}