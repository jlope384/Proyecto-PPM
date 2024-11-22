package com.example.Proyecto.util.type

import java.util.Date

public data class FormItem(
    val id: String,
    val type: FormItemType,
    val question: String,
    val options: List<String>? = null
)