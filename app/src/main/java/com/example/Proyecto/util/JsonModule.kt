package com.example.Proyecto.util

import com.example.Proyecto.layouts.createFormScreen.FormState
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow

class JsonModule {
    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        // Formulario A JSON
        fun formToJson(_formState: MutableStateFlow<FormState>): String {
            return gson.toJson(_formState.value)
        }

        // Parsear de JSON a FormState
        fun jsonToForm(json: String): FormState {
            return try {
                gson.fromJson(json, FormState::class.java)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid form JSON format")
            }
        }
    }
}
