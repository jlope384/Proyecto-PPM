package com.example.Proyecto.layouts.RegistrationScreen.presentation

data class RegisterScreenState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val authStatus: Boolean? = null
)