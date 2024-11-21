package com.example.Proyecto.layouts.loginScreen.repository

interface LoginRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun createUser(email: String, password: String): Boolean
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getCurrentUserId(): String?
    suspend fun logout()
}