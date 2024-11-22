package com.example.Proyecto.layouts.fillFormScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Proyecto.layouts.createFormScreen.repository.FormsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files.createFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FillFormViewModel : ViewModel() {
    private val formsRepository = FormsRepositoryImpl()
    private val _formState = MutableStateFlow(FillFormState())
    val formState: StateFlow<FillFormState> = _formState.asStateFlow()

    fun loadFromFirestore(formId: String) {
        if (formId.isBlank()) {
            _formState.update { it.copy(isLoading = false, error = "Invalid form ID") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                val form = formsRepository.loadFromFirestore(formId)
                println("loaded something from firestore")
                println(form)
                _formState.update {
                    it.copy(
                        id = formId,
                        title = form.title,
                        description = form.description,
                        items = form.items,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar form: ${e.message}"
                    )
                }
            }
        }
    }

    fun onEvent(event: FillFormUIEvent) {
        when (event) {
            is FillFormUIEvent.ResponseChanged -> {
                _formState.update {
                    it.copy(
                        responses = it.responses + (event.questionId to event.response)
                    )
                }
            }
        }
    }

    private fun validateResponses(): List<String> {
        val errors = mutableListOf<String>()
        _formState.value.items.forEach { item ->
            if (!_formState.value.responses.containsKey(item.id)) {
                errors.add("Por favor responda la pregunta: ${item.question}")
            }
        }
        return errors
    }


    fun exportResponses(context: Context) {
        println("Exporting responses...")
        val errors = validateResponses()
        if (errors.isNotEmpty()) {
            _formState.update { it.copy(error = errors.joinToString("\n")) }
            return
        }
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            try {
                // Build the CSV content
                val csvContent = buildString {
                    append("Pregunta,Respuesta\n")
                    _formState.value.items.forEach { item ->
                        val response = _formState.value.responses[item.id] ?: ""
                        append("\"${item.question.replace("\"", "\"\"")}\",\"${response.replace("\"", "\"\"")}\"\n")
                    }
                }

                // Create a temporary file in the cache directory
                val fileName = "responses_${_formState.value.id}_${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                }.csv"
                val tempFile = createTempFile(context, fileName)
                println(fileName)

                // Write CSV content to the temporary file
                tempFile.outputStream().use { outputStream ->
                    outputStream.write(csvContent.toByteArray())
                }
                println("CSV file written to: ${tempFile.absolutePath}")
                println(context.packageName)
                println("${context.packageName}.fileprovider")
                // Create a shareable URI
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempFile
                )
                println("URI: $uri")

                // Create and launch a sharing intent
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                println("Sharing intent created")
                val chooserIntent = Intent.createChooser(shareIntent, "Export Responses")
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)

                println("Sharing intent started")
                _formState.update {
                    it.copy(
                        isLoading = false,
                        isSubmitted = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                println("Error exporting responses: ${e.message}")
                _formState.update {
                    it.copy(isLoading = false, error = "Failed to export responses: ${e.message}")
                }
            }
        }
    }

    private fun createTempFile(context: Context, fileName: String): File {
        val tempFile = File(context.cacheDir, fileName)
        if (!tempFile.exists()) {
            tempFile.createNewFile()
        }
        return tempFile
    }

}

