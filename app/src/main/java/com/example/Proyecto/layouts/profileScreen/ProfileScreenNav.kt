package com.example.Proyecto.layouts.profileScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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