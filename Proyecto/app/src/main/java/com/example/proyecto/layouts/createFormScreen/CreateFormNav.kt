package com.example.proyecto.layouts.createFormScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object CreateFormDestination

fun NavGraphBuilder.createDestination(
    onCreateFormClick: () -> Unit
    ) {
    composable<CreateFormDestination> {
        CreateFormRoute(onCreateFormSuccess = onCreateFormClick)
    }
}