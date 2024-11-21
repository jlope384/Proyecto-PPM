package com.example.Proyecto.layouts.loginScreen.presentation

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val authStatus: Boolean? = null
)
