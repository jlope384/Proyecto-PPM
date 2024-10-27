package com.example.proyecto.util.db

import com.example.proyecto.util.type.FolderItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class FolderItemDb {
    fun generateRandomFolderItems(count: Int): List<FolderItem> {
        val folderPrefixes = listOf("Project", "Report", "Document", "Analysis", "Meeting", "Plan")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return List(count) { index ->
            val randomDaysAgo = Random.nextInt(0, 365)
            val randomDate = LocalDate.now().minusDays(randomDaysAgo.toLong())

            FolderItem(
                id = index + 1,
                title = "${folderPrefixes[Random.nextInt(folderPrefixes.size)]} ${index + 1}",
                date = randomDate.format(formatter)
            )
        }
    }
}