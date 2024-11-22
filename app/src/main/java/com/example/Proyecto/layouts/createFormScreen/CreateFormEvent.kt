package com.example.Proyecto.layouts.createFormScreen

import android.content.Context
import com.example.Proyecto.util.type.FormItemType

public sealed class FormUIEvent {
    data class TitleChanged(val title: String) : FormUIEvent()
    data class DescriptionChanged(val description: String) : FormUIEvent()
    data class ItemAdded(val type: FormItemType, val question: String) : FormUIEvent()
    data class ItemRemoved(val id: String) : FormUIEvent()
    data class ItemMoved(val fromIndex: Int, val toIndex: Int) : FormUIEvent()
    data class ItemUpdated(val id: String, val question: String, val options: List<String>? = null) : FormUIEvent()
    data object ExportForm : FormUIEvent()
    data class ImportForm(val jsonString: String) : FormUIEvent()
    data class ShareForm(val context: Context) : FormUIEvent()
}