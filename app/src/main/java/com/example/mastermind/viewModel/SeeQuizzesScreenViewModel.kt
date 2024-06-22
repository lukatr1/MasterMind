package com.example.mastermind.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz
import kotlinx.coroutines.launch

class SeeQuizzesScreenViewModel(private var context: Context) : ViewModel() {
    private fun getContext () : Context {
        return context
    }
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(getContext())

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

    init {
        getAllQuizzes()
    }

    fun createQuiz(name: String): Int {
        viewModelScope.launch {
            return quizRepo.createQuiz(name)
        }
    }

    fun getAllQuizzes() {
        _quizzes.value = quizRepo.getAllQuizzes()
    }

    fun updateQuizzes() {
        viewModelScope.launch {
            _quizzes.postValue(quizRepo.getAllQuizzes())
        }
    }

    fun deleteQuiz(id: Int) {
        viewModelScope.launch {
            quizRepo.deleteQuiz(id)
            updateQuizzes()
        }
    }

    fun editQuiz(id: Int, newName: String) {
        val quiz = quizRepo.getQuizById(id)
        val updatedQuiz = quiz.copy(name = newName)
        // Remove the old quiz and add the updated one
        deleteQuiz(id)
        quizRepo.createQuiz(updatedQuiz.name)
        updateQuizzes()
    }

    fun getBookmarkedQuizzes(): List<Quiz> {
        return quizRepo.getBookmarkedQuizzes()
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