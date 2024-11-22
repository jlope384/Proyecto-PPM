package com.example.Proyecto.layouts.profileScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.Proyecto.layouts.profileScreen.presentation.ProfileScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data object ProfileDestination

fun NavController.navigateToProfileScreen(navOptions: NavOptions? = null) {
    this.navigate(ProfileDestination, navOptions)
}

fun NavGraphBuilder.profileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    composable<ProfileDestination> {
        ProfileScreenRoute(
            onBack = onBack,
            onLogout = onLogout
        )
    }
}