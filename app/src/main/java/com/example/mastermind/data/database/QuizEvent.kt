package com.example.mastermind.data.database

sealed interface QuizEvent {
    object SaveQuiz : QuizEvent
    data class SetQuizName(val name: String) : QuizEvent
    data class SetQuizQuestion(val question: String) : QuizEvent
    data class SetQuizAnswer(val answer: String) : QuizEvent
}