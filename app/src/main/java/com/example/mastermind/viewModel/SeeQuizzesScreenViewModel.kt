package com.example.mastermind.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz

class SeeQuizzesScreenViewModel : ViewModel() {
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance()

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

    init {
        getAllQuizzes()
    }

    fun createQuiz(name: String): Int {
        return quizRepo.createQuiz(name)
    }

    fun getAllQuizzes() {
        _quizzes.value = quizRepo.getAllQuizzes()
    }

    fun updateQuizzes() {
        _quizzes.postValue(quizRepo.getAllQuizzes())
    }

    fun deleteQuiz(id: Int) {
        quizRepo.deleteQuiz(id)
        updateQuizzes()
    }

    fun editQuiz(id: Int, newName: String) {
        val quiz = quizRepo.getQuizById(id)
        val updatedQuiz = quiz.copy(name = newName)
        // Remove the old quiz and add the updated one
        deleteQuiz(id)
        quizRepo.createQuiz(updatedQuiz.name)
        updateQuizzes()
    }

    fun createMultipleChoiceQuestion(
        quizId: Int,
        choicesTrue: List<String>,
        choicesFalse: List<String>,
        text: String
    ) {
        quizRepo.createMultipleChoiceQuestion(quizId, choicesTrue, choicesFalse, text)
    }
}