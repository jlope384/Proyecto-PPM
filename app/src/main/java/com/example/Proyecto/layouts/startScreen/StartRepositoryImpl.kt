package com.example.Proyecto.layouts.startScreen

import com.example.Proyecto.layouts.loginScreen.repository.FirebaseLoginRepository
import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.FormItem
import com.google.firebase.firestore.FirebaseFirestore

class StartRepositoryImpl: StartRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val loginRepository = FirebaseLoginRepository()

    override suspend fun getFolders(): List<FolderItem> {
        // Simular consulta a base de datos
        return listOf(

        )
    }

    override suspend fun getForms(): List<FormItem> {
        // Simular coznsulta a base de datos
        return listOf(

        )
    }

    override suspend fun createFolder(): Boolean {
        TODO("Not yet implemented")
    }
}
