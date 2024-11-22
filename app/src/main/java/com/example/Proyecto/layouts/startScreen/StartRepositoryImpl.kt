package com.example.Proyecto.layouts.startScreen

import com.example.Proyecto.layouts.loginScreen.repository.FirebaseLoginRepository
import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.FormDisplayItem
import com.example.Proyecto.util.type.FormItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StartRepositoryImpl: StartRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val loginRepository = FirebaseLoginRepository()

    override suspend fun getFolders(): List<FolderItem> {
        val foldersGot = mutableListOf<FolderItem>()
        try {
            val forms = loginRepository.getCurrentUserId()?.let {
                firestore.collection("users")
                    .document(it)
                    .collection("folders")
                    .get()
                    .await()
                    .documents
            }

            forms?.mapNotNull { document ->
                try {
                    val id = document.getString("id")
                    val title = document.getString("title") ?: ""
                    val date = document.getString("date") ?: ""

                    if (id != null) {
                        foldersGot += FolderItem(id, title, date)

                    } else {
                        throw Exception("Folder id no existente")
                    }
                } catch (e: Exception) {
                    throw Exception("Documentos de Folders mal formados")
                }
            }
        } catch (e: Exception) {
            throw Exception("Error al getear folders: ${e.message}")
        }
        return foldersGot
    }

    override suspend fun getForms(): List<FormDisplayItem> {
        val formsGot = mutableListOf<FormDisplayItem>()
        try {
            val forms = loginRepository.getCurrentUserId()?.let {
                firestore.collection("users")
                    .document(it)
                    .collection("forms")
                    .get()
                    .await()
                    .documents
            }

            forms?.mapNotNull { document ->
                try {
                    val id = document.getString("id")
                    val title = document.getString("title") ?: ""
                    val folderId = document.getString("folderId")?.toInt()

                    if (id != null) {
                        formsGot += FormDisplayItem(id, title, folderId)

                    } else {
                        throw Exception("Form id no existente")
                    }
                } catch (e: Exception) {
                    throw Exception("Documentos de Folders mal formados")
                }
            }
        } catch (e: Exception) {
            throw Exception("Error al getear forms: ${e.message}")
        }
        return formsGot
    }

    override suspend fun createFolder(folderItem: FolderItem): Boolean {
        try {
            val userId = loginRepository.getCurrentUserId()
            if (userId != null) {
                firestore.collection("users")
                    .document(userId)
                    .collection("folders")
                    .document(folderItem.id.toString())
                    .set(folderItem)
                    .await()
                return true
            } else {
                throw Exception("No hay usuario logeado")
            }
        } catch (e: Exception) {
            throw Exception("Error al crear folder: ${e.message}")
        }
    }
}
