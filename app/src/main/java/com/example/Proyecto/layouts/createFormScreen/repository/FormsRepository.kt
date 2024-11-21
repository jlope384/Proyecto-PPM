package com.example.Proyecto.layouts.createFormScreen.repository

import com.example.Proyecto.layouts.createFormScreen.FormState
import kotlinx.coroutines.flow.MutableStateFlow


interface FormsRepository {
    suspend fun saveToFirestore(_formState : MutableStateFlow<FormState>): String
    suspend fun loadFromFirestore(formId: String): FormState
}


