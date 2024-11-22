package com.example.Proyecto.layouts.forgotPasswordScreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.Proyecto.layouts.forgotPasswordScreen.repository.FirebaseForgotPasswordRepository
import com.example.Proyecto.layouts.forgotPasswordScreen.repository.ForgotPasswordRepository
import com.example.Proyecto.layouts.loginScreen.presentation.LoginViewModel
import com.example.Proyecto.layouts.loginScreen.repository.FirebaseLoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val forgotPasswordRepository: ForgotPasswordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )

        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }
            ForgotPasswordEvent.SendEmailClicked -> sendPasswordResetEmail()
        }
    }

    private fun sendPasswordResetEmail() {
        viewModelScope.launch {
            val email = _uiState.value.email
            if (email.isBlank()) {
                _uiState.value = _uiState.value.copy(errorMessage = "El email no puede estar vacío.")
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)
            val success = forgotPasswordRepository.forgotPassword(email)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                successMessage = if (success) "Correo de recuperación enviado con éxito." else null,
                errorMessage = if (!success) "No se pudo enviar el correo. Intente de nuevo." else null
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val forgotPasswordRepository = FirebaseForgotPasswordRepository()
                ForgotPasswordViewModel(forgotPasswordRepository)
            }
        }
    }
}