package com.example.Proyecto.layouts.createFormScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


@Serializable
data class CreateFormDestination(val id: String?, val folderId: String?)

fun NavController.navigateToCreateFormScreen(
    id: String? = null,
    folderId: String? = null,
    navOptions: NavOptions? = null
) {
    this.navigate(CreateFormDestination(id = id, folderId = folderId), navOptions)
}

fun NavGraphBuilder.createFormScreen(
    onBack: () -> Unit,
    onCreateFormClick: () -> Unit

    ) {
    composable<CreateFormDestination> { backStackEntry ->
        val backStackParams: CreateFormDestination = backStackEntry.toRoute()
        CreateFormRoute(id = backStackParams.id, folderId = backStackParams.folderId, onCreateFormSuccess = onCreateFormClick, onBack = onBack)
    }
}