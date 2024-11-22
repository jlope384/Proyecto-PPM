package com.example.Proyecto.layouts.createFormScreen.repository

import com.android.identity.util.UUID
import com.example.Proyecto.layouts.loginScreen.repository.FirebaseLoginRepository
import com.example.Proyecto.layouts.createFormScreen.FormState
import com.example.Proyecto.util.JsonModule.Companion.formToJson
import com.example.Proyecto.util.JsonModule.Companion.jsonToForm
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class FormsRepositoryImpl : FormsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val loginRepository = FirebaseLoginRepository()
    override suspend fun saveToFirestore(_formState: MutableStateFlow<FormState>): String {
        val formJson = formToJson(_formState = _formState)
        val formId = _formState.value.id ?: UUID.randomUUID().toString()

        val formData = hashMapOf(
            "id" to formId,
            "title" to _formState.value.title,
            "description" to _formState.value.description,
            "items" to _formState.value.items,
            "lastModified" to _formState.value.lastModified,
            "jsonData" to formJson,
            "folderId" to _formState.value.folderId
        )

        try {
            loginRepository.getCurrentUserId()?.let {
                firestore.collection("users")
                    .document(it)         // UserId de firebase
                    .collection("forms")
                    .document(formId)
                    .set(formData)
                    .await()
            }

            return formId
        } catch (e: Exception) {
            throw Exception("Failed to save to Firestore: ${e.message}")
        }
    }

    // Cargar formulario desde Firestore
    override suspend fun loadFromFirestore(formId: String): FormState {
        try {
            val document = firestore.collection("forms")
                .document(formId)
                .get()
                .await()

            if (document != null && document.exists()) {
                val jsonData = document.getString("jsonData")
                if (jsonData != null) {
                    val form = jsonToForm(jsonData)
                    return form
                }
            }
        } catch (e: Exception) {
            return FormState(error = "Failed to load form: ${e.message}")
        }
        return FormState(error = "Form not found")
    }
}