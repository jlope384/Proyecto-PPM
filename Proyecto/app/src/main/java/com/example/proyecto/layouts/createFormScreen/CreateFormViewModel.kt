package com.example.proyecto.layouts.createFormScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.proyecto.util.type.FormItem
import com.example.proyecto.util.type.FormItemType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CreateFormViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
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

    // Convertir a JSON
    private fun formToJson(): String {
        return gson.toJson(_formState.value)
    }

    // Parsear de JSON a FormState
    private fun jsonToForm(json: String): FormState {
        return try {
            gson.fromJson(json, FormState::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid form JSON format")
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
                    id = (_formState.value.items.maxOfOrNull { it.id } ?: 0) + 1,
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

    // Guardar en Firestore el formulario
    private suspend fun saveToFirestore(): String {
        val formJson = formToJson()
        val formId = _formState.value.id?.toString() ?: UUID.randomUUID().toString()

        val formData = hashMapOf(
            "id" to formId,
            "title" to _formState.value.title,
            "description" to _formState.value.description,
            "items" to _formState.value.items,
            "lastModified" to _formState.value.lastModified,
            "jsonData" to formJson
        )

        try {
            firestore.collection("users")
                .document("userId")         // UserId de firebase
                .collection("forms")
                .document(formId)
                .set(formData)
                .await()

            return formId
        } catch (e: Exception) {
            throw Exception("Failed to save to Firestore: ${e.message}")
        }
    }

    // Cargar formulario desde Firestore
    suspend fun loadFromFirestore(formId: String) {
        try {
            val document = firestore.collection("forms")
                .document(formId)
                .get()
                .await()

            if (document != null && document.exists()) {
                val jsonData = document.getString("jsonData")
                if (jsonData != null) {
                    val form = jsonToForm(jsonData)
                    _formState.update { form }
                }
            }
        } catch (e: Exception) {
            _formState.update { it.copy(error = "Failed to load form: ${e.message}") }
        }
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
                val formId = saveToFirestore()
                _formState.update {
                    it.copy(
                        id = formId.toInt(),
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
                val jsonString = formToJson()
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
                val formId = saveToFirestore() // Guardar formulario en Firestore para el ID
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