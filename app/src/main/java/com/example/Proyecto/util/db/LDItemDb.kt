package com.example.Proyecto.util.db

import com.example.Proyecto.util.type.LDItem
import com.example.Proyecto.util.type.LDItemType.Folder
import com.example.Proyecto.util.type.LDItemType.Form

class LDItemDb{
    fun generateRandomLDItems(count: Int = 10): List<LDItem> {
        val types = listOf(Folder, Form)
        val folderNames = listOf("Documents", "Images", "Projects", "Reports", "Archives")
        val formNames = listOf("Registration", "Survey", "Feedback", "Application", "Questionnaire")
        var id = 0

        return List(count) {
            id += 1
            val type = types.random()
            val name = when (type) {
                Folder -> "${folderNames.random()} ${('A'..'Z').random()}"
                Form -> "${formNames.random()} ${(1..100).random()}"
            }
            LDItem(id, name, type)
        }
    }
}

