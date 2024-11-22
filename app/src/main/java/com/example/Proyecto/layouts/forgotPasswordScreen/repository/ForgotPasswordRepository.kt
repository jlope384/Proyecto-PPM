package com.example.Proyecto.layouts.forgotPasswordScreen.repository

interface ForgotPasswordRepository {
    suspend fun forgotPassword(email: String): Boolean
}