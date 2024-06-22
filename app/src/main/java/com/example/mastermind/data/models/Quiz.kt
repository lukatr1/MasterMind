package com.example.mastermind.data.models

data class Quiz(
    val id: Int,
    val name: String,
    val questions: List<Question>,
    var bookmarked: Boolean = false
)
