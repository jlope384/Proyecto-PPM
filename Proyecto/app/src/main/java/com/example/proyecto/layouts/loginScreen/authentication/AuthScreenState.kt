package com.example.proyecto.layouts.loginScreen.authentication

sealed class AuthState {
    object Idle : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
    object Loading : AuthState()
}
