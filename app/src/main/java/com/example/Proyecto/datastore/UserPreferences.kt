package com.example.Proyecto.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    val USER_ID_KEY = stringPreferencesKey("user_id")
    val USER_LOGGED_IN_KEY = booleanPreferencesKey("user_logged_in")
    val USER_EMAIL_KEY = stringPreferencesKey("user_email")

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveUserLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN_KEY] = loggedIn
        }
    }

    suspend fun saveUserEmail(userEmail: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = userEmail
        }
    }

    fun getUserId(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    fun getUserLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[USER_LOGGED_IN_KEY] ?: false
    }

    fun getUserEmail(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }
}