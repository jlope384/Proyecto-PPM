package com.example.Proyecto.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UserViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun saveFirebaseUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid
            val userEmail = it.email

            viewModelScope.launch {
                userPreferences.saveUserId(userId)
                userPreferences.saveUserEmail(userEmail ?: "No email")
            }
        }
    }
}
