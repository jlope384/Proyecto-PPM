package com.example.Proyecto.util.type

public data class FormItem(
    val id: Int,
    val type: FormItemType,
    val question: String,
    val options: List<String>? = null
)