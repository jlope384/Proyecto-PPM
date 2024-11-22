package com.example.Proyecto.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.Proyecto.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(private val context: Context) {

    companion object {
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }
}