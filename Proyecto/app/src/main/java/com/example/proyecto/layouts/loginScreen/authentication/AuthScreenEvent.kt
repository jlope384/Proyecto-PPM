package com.example.proyecto.layouts.loginScreen.authentication

sealed interface AuthScreenEvent {
    data class EmailChange(val email: String): AuthScreenEvent
    data class PasswordChange(val password: String): AuthScreenEvent
    data object LoginClick : AuthScreenEvent
}
