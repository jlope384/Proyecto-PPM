package com.example.Proyecto.layouts.profileScreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.Proyecto.layouts.profileScreen.repository.FirebaseProfileRepository
import com.example.Proyecto.layouts.profileScreen.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )

        when (event) {
            is ProfileEvent.LogoutClicked -> logout(event.onLogout)
        }
    }

    private fun logout(onLogout: () -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                profileRepository.logout()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Logout exitoso"
                )
                onLogout()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Logout fallido: ${e.message}"
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val profileRepository: ProfileRepository = FirebaseProfileRepository()
                ProfileViewModel(profileRepository)
            }
        }
    }
}
