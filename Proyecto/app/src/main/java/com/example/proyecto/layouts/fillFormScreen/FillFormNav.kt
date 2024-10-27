package com.example.proyecto.layouts.fillFormScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyecto.layouts.createFormScreen.CreateFormDestination
import com.example.proyecto.layouts.loginScreen.LoginDestination
import com.example.proyecto.layouts.loginScreen.LoginScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data class FillFormDestination(val id: Int)

fun NavController.navigateToFillFormScreen(
    id: Int,
    navOptions: NavOptions? = null
) {
    this.navigate(FillFormDestination(id = id), navOptions)
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