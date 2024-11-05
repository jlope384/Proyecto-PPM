package com.example.proyecto.navigation

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.example.proyecto.layouts.createFormScreen.CreateFormDestination
import com.example.proyecto.layouts.createFormScreen.createFormScreen
import com.example.proyecto.layouts.createFormScreen.navigateToCreateFormScreen
import com.example.proyecto.layouts.fillFormScreen.FillFormDestination
import com.example.proyecto.layouts.fillFormScreen.fillFormScreen
import com.example.proyecto.layouts.fillFormScreen.navigateToFillFormScreen
import com.example.proyecto.layouts.folderScreen.FolderDestination
import com.example.proyecto.layouts.folderScreen.folderScreen
import com.example.proyecto.layouts.folderScreen.navigateToFolderScreen
import com.example.proyecto.layouts.profileScreen.profileScreen
import com.example.proyecto.layouts.startScreen.StartDestination
import com.example.proyecto.layouts.startScreen.StartScreen
import com.example.proyecto.layouts.startScreen.startScreen
import com.example.proyecto.util.type.LDItemType

@Serializable
data object MainNavGraph

fun NavController.navigateToMainGraph(navOptions: NavOptions? = null) {
    this.navigate(MainNavGraph, navOptions)
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
    navigation<MainNavGraph>(
        startDestination = StartDestination
    ) {
        startScreen(
            onBack = navController::navigateUp,
            onFolderClick = navController::navigateToFolderScreen,
            onFillFormClick = navController::navigateToFillFormScreen,
            onEditFormClick = navController::navigateToCreateFormScreen
        )
        createFormScreen(
            onBack = navController::navigateUp,
            onCreateFormClick = {}
        )
        fillFormScreen(
            onBack = navController::navigateUp,
            onSubmit = {}
        )
        folderScreen(
            onBack = navController::navigateUp,
        )
        profileScreen(
            onBack = navController::navigateUp,
        )

    }
}