package com.example.proyecto.util

class LDItemDb{
    fun generateRandomLDItems(count: Int = 10): List<LDItem> {
        val types = listOf("Folder", "Form")
        val folderNames = listOf("Documents", "Images", "Projects", "Reports", "Archives")
        val formNames = listOf("Registration", "Survey", "Feedback", "Application", "Questionnaire")

        return List(count) {
            val type = types.random()
            val name = when (type) {
                "Folder" -> "${folderNames.random()} ${('A'..'Z').random()}"
                else -> "${formNames.random()} ${(1..100).random()}"
            }
            LDItem(name, type)
        }
    }
}

