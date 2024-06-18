package com.example.mastermind.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import kotlinx.coroutines.launch


class CreateQuizScreenViewModel : ViewModel() {
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getInstance()

    fun createQuiz(name: String): Int {
        val id = quizRepo.createQuiz(name)
        return id
    }

    fun createMultipleChoiceQuestion(
        quizId: Int,
        choicesTrue: List<String>,
        choicesFalse: List<String>,
        text: String
    ) {
        quizRepo.createMultipleChoiceQuestion(quizId, choicesTrue, choicesFalse, text)
    }

    fun createTrueFalseQuestion(quizId: Int, answer: Boolean, text: String) {
        quizRepo.createTrueFalseQuestion(quizId, answer, text)
    }
}