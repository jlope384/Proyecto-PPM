package com.example.Proyecto.layouts.createFormScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.Proyecto.layouts.createFormScreen.repository.FormsRepositoryImpl
import com.example.Proyecto.util.JsonModule.Companion.formToJson
import com.example.Proyecto.util.JsonModule.Companion.jsonToForm
import com.example.Proyecto.util.type.FormItem
import com.example.Proyecto.util.type.FormItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CreateFormViewModel : ViewModel() {

    private val formsRepository = FormsRepositoryImpl()
    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    private val _validationErrors = MutableStateFlow<List<String>>(emptyList())
    val validationErrors: StateFlow<List<String>> = _validationErrors.asStateFlow()

    // funcion placeholder
    private fun loadForm() {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        title = "New Form",
                        description = "Form Description"
                    )
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load form: ${e.message}"
                    )
                }
            }
        }
    }

    fun loadFromFirestore(formId: String) {
        if (formId.isBlank())
        {
            _formState.update {
                it.copy(
                    isLoading = false
                )
            }
            return
        }
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                val form = formsRepository.loadFromFirestore(formId)
                _formState.update { form }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load form: ${e.message}"
                    )
                }
            }
        }
    }

    // Manejar eventos
    fun onEvent(event: FormUIEvent) {
        when (event) {
            is FormUIEvent.TitleChanged -> {
                _formState.update { it.copy(title = event.title) }
            }
            is FormUIEvent.DescriptionChanged -> {
                _formState.update { it.copy(description = event.description) }
            }
            is FormUIEvent.ItemAdded -> {
                val newItem = FormItem(
                    id = UUID.randomUUID().toString(),
                    type = event.type,
                    question = event.question,
                    options = when (event.type) {
                        FormItemType.MultipleChoice -> listOf("Option 1", "Option 2", "Option 3")
                        FormItemType.Scale -> listOf("1", "2", "3", "4", "5")
                        else -> null
                    }
                )
                _formState.update {
                    it.copy(
                        items = it.items + newItem,
                        lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                }
            }
            is FormUIEvent.ItemRemoved -> {
                _formState.update {
                    it.copy(
                        items = it.items.filterNot { item -> item.id == event.id },
                        lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                }
            }
            is FormUIEvent.ItemMoved -> {
                val items = _formState.value.items.toMutableList()
                val item = items.removeAt(event.fromIndex)
                items.add(event.toIndex, item)
                _formState.update {
                    it.copy(
                        items = items,
                        lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                }
            }
            is FormUIEvent.ItemUpdated -> {
                _formState.update {
                    it.copy(
                        items = it.items.map { item ->
                            if (item.id == event.id) {
                                item.copy(
                                    question = event.question,
                                    options = event.options ?: item.options
                                )
                            } else item
                        },
                        lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                }
            }
            FormUIEvent.SaveForm -> saveForm()
            FormUIEvent.ExportForm -> exportForm()
            is FormUIEvent.ImportForm -> importForm(event.jsonString)
            is FormUIEvent.ShareForm -> shareForm(event.context)
        }
    }

    private fun validateForm(): List<String> {
        val errors = mutableListOf<String>()

        if (_formState.value.title.isBlank()) {
            errors.add("Title is required")
        }

        if (_formState.value.items.isEmpty()) {
            errors.add("Form must contain at least one question")
        }

        _formState.value.items.forEach { item ->
            if (item.question.isBlank()) {
                errors.add("Question ${item.id} cannot be empty")
            }
            if (item.type == FormItemType.MultipleChoice && item.options.isNullOrEmpty()) {
                errors.add("Multiple choice question ${item.id} must have options")
            }
        }

        return errors
    }

    private fun saveForm() {
        viewModelScope.launch {
            val errors = validateForm()
            if (errors.isNotEmpty()) {
                _validationErrors.value = errors
                return@launch
            }

            _formState.update { it.copy(isLoading = true) }
            try {
                _formState.update { it.copy(
                    lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                ) }
                val formId = formsRepository.saveToFirestore(_formState)
                _formState.update {
                    it.copy(
                        id = formId,
                        isLoading = false,
                        isSaved = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to save form: ${e.message}"
                    )
                }
            }
        }
    }

    // Exportar formulario a JSON
    private fun exportForm() {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                val jsonString = formToJson(_formState)
                val fileName = "form_${_formState.value.id ?: UUID.randomUUID()}.json"

                // Logica para guardar el archivo en el dispositivo
                _formState.update {
                    it.copy(
                        isLoading = false,
                        exportPath = fileName,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to export form: ${e.message}"
                    )
                }
            }
        }
    }

    // Compartir formulario
    private fun shareForm(context: Context) {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                val formId = formsRepository.saveToFirestore(_formState) // Guardar formulario en Firestore para el ID
                val shareUrl = "yourapp://forms/$formId" // Ejemplo de un Deeplink

                _formState.update {
                    it.copy(
                        isLoading = false,
                        shareUrl = shareUrl,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to share form: ${e.message}"
                    )
                }
            }
        }
    }

    // Importar formulario desde JSON
    private fun importForm(jsonString: String) {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                val importedForm = jsonToForm(jsonString)
                _formState.update {
                    importedForm.copy(
                        id = null, // Resetar ID para un nuevo formulario
                        isLoading = false,
                        isSaved = false,
                        error = null,
                        lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to import form: ${e.message}"
                    )
                }
            }
        }
    }
}