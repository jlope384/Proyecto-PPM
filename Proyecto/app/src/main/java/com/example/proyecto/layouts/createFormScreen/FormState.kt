package com.example.proyecto.layouts.createFormScreen
import com.example.proyecto.util.type.FormItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FormState(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val items: List<FormItem> = emptyList(),
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val error: String? = null,
    val lastModified: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
    val shareUrl: String? = null,
    val exportPath: String? = null
)
