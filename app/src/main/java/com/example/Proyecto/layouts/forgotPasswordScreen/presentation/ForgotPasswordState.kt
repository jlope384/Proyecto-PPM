package com.example.Proyecto.layouts.forgotPasswordScreen.presentation

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)