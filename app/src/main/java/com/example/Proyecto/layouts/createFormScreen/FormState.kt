package com.example.Proyecto.layouts.createFormScreen
import com.example.Proyecto.util.type.FormItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public data class FormState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val items: List<FormItem> = emptyList(),
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val error: String? = null,
    val lastModified: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
    val shareUrl: String? = null,
    val exportPath: String? = null,
    val folderId: Int? = null
)
