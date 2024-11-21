package com.example.Proyecto.layouts.loginScreen.repository

import com.example.Proyecto.layouts.loginScreen.repository.LoginRepository
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
            println("EL ERROR ES: $e")
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

    override suspend fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    override suspend fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}