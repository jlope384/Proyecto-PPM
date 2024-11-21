package com.example.Proyecto.layouts.startScreen

import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.FormItem

interface StartRepository {
    suspend fun getFolders(): List<FolderItem>
    suspend fun getForms(): List<FormItem>
    suspend fun createFolder(): Boolean
}
