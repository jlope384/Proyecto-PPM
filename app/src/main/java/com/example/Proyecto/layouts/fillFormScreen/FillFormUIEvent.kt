package com.example.Proyecto.layouts.fillFormScreen

sealed class FillFormUIEvent {
    data class ResponseChanged(val questionId: String, val response: String) : FillFormUIEvent()
}