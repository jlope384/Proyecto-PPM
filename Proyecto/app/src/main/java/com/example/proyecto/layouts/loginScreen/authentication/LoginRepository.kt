package com.example.proyecto.layouts.loginScreen.authentication

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
}
