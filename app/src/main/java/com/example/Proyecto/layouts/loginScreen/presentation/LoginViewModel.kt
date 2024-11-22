package com.example.Proyecto.layouts.loginScreen.presentation

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.Proyecto.datastore.UserPreferences
import com.example.Proyecto.layouts.loginScreen.repository.FirebaseLoginRepository
import com.example.Proyecto.layouts.loginScreen.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun onEvent(event: LoginScreenEvent) {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )

        when (event) {
            is LoginScreenEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }

            is LoginScreenEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = event.password)
            }

            LoginScreenEvent.LoginClicked -> login()
            LoginScreenEvent.LogoutClicked -> logout()
            LoginScreenEvent.CreateAccountClicked -> createAccount()
            LoginScreenEvent.CheckAuthStatusClicked -> checkAuthStatus()
        }
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val success = loginRepository.login(_uiState.value.email, _uiState.value.password)

            if (success) {
                val userId = loginRepository.getCurrentUserId()
                userId?.let {
                    userPreferences.saveUserId(it)
                    userPreferences.saveUserLoggedIn(true)
                    userPreferences.saveUserEmail(_uiState.value.email)
                }
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                successMessage = if (success) "Login successful" else null,
                errorMessage = if (!success) "Login failed" else null,
                authStatus = success
            )
        }
    }

    private fun createAccount() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val success = loginRepository.createUser(_uiState.value.email, _uiState.value.password)

            if (success) {
                val userId = loginRepository.getCurrentUserId()
                userId?.let {
                    userPreferences.saveUserId(it)
                    userPreferences.saveUserLoggedIn(true)
                    userPreferences.saveUserEmail(_uiState.value.email)
                }
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                successMessage = if (success) "Account created successfully" else null,
                errorMessage = if (!success) "Failed to create account" else null,
                authStatus = success
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            userPreferences.saveUserLoggedIn(false)
            userPreferences.saveUserId("")
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isLoggedIn = userPreferences.getUserLoggedIn().first()
            val email = userPreferences.getUserEmail().first() ?: ""
            _uiState.value = _uiState.value.copy(
                authStatus = isLoggedIn,
                email = email,
                successMessage = if (isLoggedIn) "User is logged in" else null,
                errorMessage = if (!isLoggedIn) "User is not logged in" else null
            )
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val loginRepository = FirebaseLoginRepository()
                val userPreferences = UserPreferences(context.dataStore)
                LoginViewModel(loginRepository, userPreferences)
            }
        }
    }
}