package com.example.Proyecto.layouts.RegistrationScreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.Proyecto.layouts.RegistrationScreen.repository.FirebaseRegisterRepository
import com.example.Proyecto.layouts.RegistrationScreen.repository.RegisterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerRepository: RegisterRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterScreenState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )

        when (event) {
            is RegisterEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }
            is RegisterEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = event.password)
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _uiState.value = _uiState.value.copy(confirmPassword = event.confirmPassword)
            }
            RegisterEvent.RegisterClicked -> register()
        }
    }

    private fun register() {
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Las contraseñas no coinciden"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val success = registerRepository.register(_uiState.value.email, _uiState.value.password)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                successMessage = if (success) "Registro exitoso" else null,
                errorMessage = if (!success) "Hubo un error en el registro" else null,
                authStatus = success
            )
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val registerRepository = FirebaseRegisterRepository()
                RegisterViewModel(registerRepository)
            }
        }
    }
}
