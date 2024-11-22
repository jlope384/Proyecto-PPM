package com.example.Proyecto.layouts.RegistrationScreen.presentation

sealed interface RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent
    data object RegisterClicked : RegisterEvent
}