package com.example.proyecto.layouts.loginScreen.loginScreenViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto.layouts.loginScreen.authentication.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Por favor, ingresa ambos campos")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            if (email == "test@example.com" && password == "password123") {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Correo o contraseña incorrectos")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
