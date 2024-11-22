package com.example.Proyecto.layouts.RegistrationScreen.repository

interface RegisterRepository {
    suspend fun register(email: String, password: String): Boolean
}