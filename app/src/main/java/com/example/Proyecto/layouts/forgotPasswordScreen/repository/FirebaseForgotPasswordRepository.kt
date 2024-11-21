package com.example.Proyecto.layouts.forgotPasswordScreen.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseForgotPasswordRepository : ForgotPasswordRepository {
    override suspend fun forgotPassword(email: String): Boolean {
        try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}