package com.example.Proyecto.layouts.startScreen

import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.FormItem

data class StartScreenState(
    var isLoading: Boolean = true,
    var error: String? = null,
    var folders: List<FolderItem> = emptyList(),
    var forms: List<FormItem> = emptyList()
)