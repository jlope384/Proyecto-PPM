package com.example.Proyecto.layouts.startScreen

import android.icu.text.CaseMap.Fold
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.Proyecto.util.type.FolderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StartScreenViewModel : ViewModel() {

    private val repository = StartRepositoryImpl()

    private val _startState = MutableStateFlow(StartScreenState())
    val startState: StateFlow<StartScreenState> = _startState.asStateFlow()

    // Carga inicial de datos
    init {
        loadFoldersAndForms()
    }

    fun loadFoldersAndForms() {
        viewModelScope.launch {
            // Explicitly set `isLoading` to true
            _startState.update { it.copy(isLoading = true, error = null) }

            try {
                val folders = repository.getFolders()
                val forms = repository.getForms()
                _startState.update {
                    it.copy(
                        isLoading = false, // Reset `isLoading` once data is loaded
                        folders = folders,
                        forms = forms,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _startState.update {
                    it.copy(
                        isLoading = false, // Reset `isLoading` even on error
                        error = "Error al cargar datos: ${e.message}"
                    )
                }
            }
        }
    }


    fun createFolder(title: String) {
        viewModelScope.launch {
            try {
                repository.createFolder(FolderItem(id = UUID.randomUUID().toString(), title = title, date = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),))
                loadFoldersAndForms()
            } catch (e: Exception) {
                _startState.update {
                    it.copy(
                        error = "Error al crear carpeta: ${e.message}"
                    )
                }
            }
        }
    }

    fun retryLoading() {
        loadFoldersAndForms()
    }
}
