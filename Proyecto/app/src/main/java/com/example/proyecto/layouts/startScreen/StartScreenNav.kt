package com.example.proyecto.layouts.startScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto.layouts.fillFormScreen.FillFormDestination
import com.example.proyecto.layouts.fillFormScreen.FillFormRoute
import com.example.proyecto.util.type.LDItemType
import kotlinx.serialization.Serializable

@Serializable
data object StartDestination

fun NavGraphBuilder.startScreen(
    onBack: () -> Unit,
    onFolderClick: (Int) -> Unit,
    onFillFormClick: (Int) -> Unit,
    onEditFormClick: (Int?) -> Unit
) {
    composable<StartDestination> {
        StartScreenRoute(
            onBack = onBack,
            onFolderClick = onFolderClick,
            onFillFormClick = onFillFormClick,
            onEditFormClick = onEditFormClick
        )
    }
}