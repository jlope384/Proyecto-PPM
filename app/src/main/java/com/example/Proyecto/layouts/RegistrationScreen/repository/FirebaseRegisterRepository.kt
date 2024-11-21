package com.example.Proyecto.layouts.RegistrationScreen.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseRegisterRepository: RegisterRepository {
    override suspend fun register(email: String, password: String): Boolean {
        return try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()
            authResult.user != null
        } catch (e: Exception) {
            false
        }
    }
}

