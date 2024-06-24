package com.example.mastermind.data.database

import com.example.mastermind.data.models.Quiz

data class QuizState(
    val quiz: List<Quiz> = emptyList(),
    val name: String = "",
    val question: String = "",
    val answer: String = ""
)