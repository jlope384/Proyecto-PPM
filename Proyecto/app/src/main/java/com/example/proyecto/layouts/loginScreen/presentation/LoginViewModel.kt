package com.example.proyecto.layouts.loginScreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyecto.layouts.loginScreen.data.repository.FirebaseLoginRepository
import com.example.proyecto.layouts.loginScreen.domain.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

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

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                successMessage = if (success) "Account created successfully" else null,
                errorMessage = if (!success) "Failed to create account" else null,
                authStatus = success
            )
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isLoggedIn = loginRepository.isUserLoggedIn()
            _uiState.value = _uiState.value.copy(
                authStatus = isLoggedIn,
                successMessage = if (isLoggedIn) "User is logged in" else null,
                errorMessage = if (!isLoggedIn) "User is not logged in" else null
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val loginRepository = FirebaseLoginRepository()
                LoginViewModel(loginRepository)
            }
        }
    }
}