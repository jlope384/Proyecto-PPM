package com.example.Proyecto.util.db

import com.example.Proyecto.util.type.FormItem
import com.example.Proyecto.util.type.FormItemType
import kotlin.random.Random

class FormItemDb {
    fun generateRandomFormItems(count: Int): List<FormItem> {
        val questionTemplates = listOf(
            "How would you rate %s?",
            "What is your opinion about %s?",
            "Please describe your experience with %s",
            "How satisfied are you with %s?",
            "Would you recommend %s?"
        )

        val subjects = listOf(
            "our service",
            "the product",
            "customer support",
            "the website",
            "the application",
            "your recent purchase",
            "our communication",
            "the delivery process"
        )

        return List(count) { index ->
            FormItem(
                id = index + 1,
                type = FormItemType.entries[Random.nextInt(FormItemType.entries.size)],
                question = questionTemplates[Random.nextInt(questionTemplates.size)]
                    .format(subjects[Random.nextInt(subjects.size)])
            )
        }
    }
}