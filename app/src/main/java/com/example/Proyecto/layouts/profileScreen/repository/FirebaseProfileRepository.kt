package com.example.Proyecto.layouts.profileScreen.repository

import com.google.firebase.auth.FirebaseAuth

class FirebaseProfileRepository : ProfileRepository {
    override suspend fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}