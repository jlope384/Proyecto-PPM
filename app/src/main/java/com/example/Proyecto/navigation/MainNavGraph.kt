package com.example.Proyecto.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.example.Proyecto.layouts.createFormScreen.createFormScreen
import com.example.Proyecto.layouts.createFormScreen.navigateToCreateFormScreen
import com.example.Proyecto.layouts.fillFormScreen.fillFormScreen
import com.example.Proyecto.layouts.fillFormScreen.navigateToFillFormScreen
import com.example.Proyecto.layouts.folderScreen.folderScreen
import com.example.Proyecto.layouts.folderScreen.navigateToFolderScreen
import com.example.Proyecto.layouts.loginScreen.LoginDestination
import com.example.Proyecto.layouts.loginScreen.loginScreen
import com.example.Proyecto.layouts.profileScreen.navigateToProfileScreen
import com.example.Proyecto.layouts.profileScreen.profileScreen
import com.example.Proyecto.layouts.startScreen.StartDestination
import com.example.Proyecto.layouts.startScreen.startScreen

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
            onProfileClick = navController::navigateToProfileScreen,
            onFolderClick = navController::navigateToFolderScreen,
            onFillFormClick = navController::navigateToFillFormScreen,
            onEditFormClick = navController::navigateToCreateFormScreen
        )

        profileScreen(
            onBack = navController::navigateUp,
            onLogout = { navController.navigate(LoginDestination) }
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
            onCreateForm = navController::navigateToCreateFormScreen,
            onFillForm = navController::navigateToFillFormScreen
        )

    }
}

