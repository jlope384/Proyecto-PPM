package com.example.Proyecto.layouts.fillFormScreen

import com.example.Proyecto.util.type.FormItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FillFormState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val items: List<FormItem> = emptyList(),
    val responses: Map<String, String> = emptyMap(), // Map of question ID to response
    val isLoading: Boolean = true,
    val isSubmitted: Boolean = false,
    val error: String? = null,
    val lastModified: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
)