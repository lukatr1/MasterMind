package com.example.mastermind.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz
import kotlinx.coroutines.launch


class CreateQuizScreenViewModel : ViewModel() {
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance()

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

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

    fun updateQuizzes() {
        _quizzes.postValue(quizRepo.getAllQuizzes())
    }

}