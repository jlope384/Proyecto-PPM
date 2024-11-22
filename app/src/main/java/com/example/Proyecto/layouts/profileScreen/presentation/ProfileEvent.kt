package com.example.Proyecto.layouts.profileScreen.presentation

sealed interface ProfileEvent {
    data class LogoutClicked(val onLogout: () -> Unit) : ProfileEvent
}