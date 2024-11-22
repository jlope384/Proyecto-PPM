package com.example.Proyecto.layouts.fillFormScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class FillFormDestination(val id: String, val folderId: String? = null)

fun NavController.navigateToFillFormScreen(
    id: String,
    folderId: String? = null,
    navOptions: NavOptions? = null
) {
    this.navigate(FillFormDestination(id = id, folderId = folderId), navOptions)
}

fun NavGraphBuilder.fillFormScreen(
    onBack: () -> Unit,
    onSubmit: (Map<String, String>) -> Unit
) {
    composable<FillFormDestination> { backStackEntry ->
        val backStackParams: FillFormDestination = backStackEntry.toRoute()
        FillFormRoute(
            onBack = onBack,
            onSubmit = onSubmit
        )
    }
}