package com.example.proyecto.layouts.profileScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto.layouts.fillFormScreen.FillFormDestination
import com.example.proyecto.layouts.fillFormScreen.FillFormRoute
import kotlinx.serialization.Serializable

@Serializable
data object ProfileDestination

fun NavGraphBuilder.profileScreen(
    onBack: () -> Unit
) {
    composable<ProfileDestination> {
        ProfileRoute(
            onBack = onBack
        )
    }
}