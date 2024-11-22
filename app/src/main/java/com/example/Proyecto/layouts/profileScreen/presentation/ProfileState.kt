package com.example.Proyecto.layouts.profileScreen.presentation

data class ProfileState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)