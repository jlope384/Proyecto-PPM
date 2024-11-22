package com.example.Proyecto.layouts.startScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object StartDestination

fun NavGraphBuilder.startScreen(
    onBack: () -> Unit,
    onFolderClick: (String) -> Unit,
    onFillFormClick: (String, String?) -> Unit,
    onEditFormClick: (String?, String?) -> Unit
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