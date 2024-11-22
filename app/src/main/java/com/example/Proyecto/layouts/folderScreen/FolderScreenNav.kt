package com.example.Proyecto.layouts.folderScreen

import FolderScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class FolderDestination(val id: String)

fun NavController.navigateToFolderScreen(
    id: String,
    navOptions: NavOptions? = null
) {
    this.navigate(FolderDestination(id = id))
}

fun NavGraphBuilder.folderScreen(
    onBack: () -> Unit,
    onCreateForm: (String?, String?) -> Unit,
    onFillForm: (String, String?) -> Unit
) {
    composable<FolderDestination> {  backStackEntry ->
        val backStackParams: FolderDestination = backStackEntry.toRoute()
        FolderScreen(
            onBack = onBack,
            onCreateForm = onCreateForm,
            onFillForm = onFillForm,
            id = backStackParams.id
        )
    }
}