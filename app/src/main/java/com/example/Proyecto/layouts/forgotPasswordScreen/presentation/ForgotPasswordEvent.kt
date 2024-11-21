package com.example.Proyecto.layouts.forgotPasswordScreen.presentation

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent
    object SendEmailClicked : ForgotPasswordEvent
}