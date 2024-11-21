package com.example.Proyecto.layouts.startScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartScreenViewModel : ViewModel() {

    private val repository = StartRepositoryImpl()

    private val _startState = MutableStateFlow(StartScreenState())
    val startState: StateFlow<StartScreenState> = _startState.asStateFlow()

    // Carga inicial de datos
    init {
        loadFoldersAndForms()
    }

    private fun loadFoldersAndForms() {
        viewModelScope.launch {
            _startState.update { it.copy() }
            try {
                val folders = repository.getFolders()
                val forms = repository.getForms()
                _startState.update {
                    it.copy(
                        isLoading = false,
                        folders = folders,
                        forms = forms,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _startState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar datos: ${e.message}"
                    )
                }
            }
        }
    }

    fun retryLoading() {
        loadFoldersAndForms()
    }
}
