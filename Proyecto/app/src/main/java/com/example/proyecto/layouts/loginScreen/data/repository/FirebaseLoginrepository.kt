package com.example.proyecto.layouts.loginScreen.data.repository

import com.example.proyecto.layouts.loginScreen.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.tasks.await

class FirebaseLoginRepository : LoginRepository {
    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()
            authResult.user != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun createUser(email: String, password: String): Boolean {
        return try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()
            authResult.user != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override suspend fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}