package com.example.proyecto.layouts.folderScreen

import FolderScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyecto.layouts.fillFormScreen.FillFormDestination
import com.example.proyecto.layouts.fillFormScreen.FillFormRoute
import kotlinx.serialization.Serializable

@Serializable
data class FolderDestination(val id: Int)

fun NavController.navigateToFolderScreen(
    id: Int,
    navOptions: NavOptions? = null
) {
    this.navigate(FolderDestination(id = id), navOptions)
}

fun NavGraphBuilder.folderScreen(
    onBack: () -> Unit
) {
    composable<FolderDestination> {  backStackEntry ->
        val backStackParams: FolderDestination = backStackEntry.toRoute()
        FolderScreen(
            onBack = onBack
        )
    }
}