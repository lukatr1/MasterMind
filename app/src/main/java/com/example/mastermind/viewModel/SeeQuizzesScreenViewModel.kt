package com.example.mastermind.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz
import kotlinx.coroutines.launch

class SeeQuizzesScreenViewModel(context: Context) : ViewModel() {

    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(context)

    private val _quizzes = MutableLiveData<List<Quiz>>()

    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes


    init {
        getAllQuizzes()
    }


    fun getAllQuizzes() {
        viewModelScope.launch {
            _quizzes.value = quizRepo.getAllQuizzes()
        }
    }

    fun updateQuizzes() {
        viewModelScope.launch {
            _quizzes.postValue(quizRepo.getAllQuizzes())
        }
    }

    fun deleteQuiz(id: Int) {
        viewModelScope.launch {
            quizRepo.deleteQuiz(id)
            quizRepo.getQuestionsByQuizId(id).forEach { question ->
                quizRepo.removeQuestionFromQuiz(id)
            }
            updateQuizzes()
        }
    }

    fun editQuiz(id: Int, newName: String) {
        viewModelScope.launch {
            val quiz = quizRepo.getQuizById(id)
            val updatedQuiz = quiz.copy(name = newName)
            // Remove the old quiz and add the updated one
            deleteQuiz(id)
            quizRepo.createQuiz(updatedQuiz.name)
            updateQuizzes()
        }
    }

    fun bookmarkQuiz(quiz: Quiz) {
        viewModelScope.launch {
            quiz.isBookmarked = true
            quizRepo.bookmarkQuiz(quiz)
            updateQuizzes()
        }
    }

    fun unbookmarkQuiz(quiz: Quiz) {
        viewModelScope.launch {
            quiz.isBookmarked = false
            quizRepo.unbookmarkQuiz(quiz)
            updateQuizzes()
        }
    }
}
