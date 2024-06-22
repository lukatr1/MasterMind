package com.example.mastermind.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditQuizScreenViewModel(private var context: Context) : ViewModel() {
    private fun getContext () : Context {
        return context
    }
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(getContext())

    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz: StateFlow<Quiz?> get() = _quiz

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch {
            _quiz.value = quizRepo.getQuizById(quizId)
        }
    }

    fun updateQuestion(updatedQuestion: Question) {
        val quiz = _quiz.value
        if (quiz != null) {
            val updatedQuestions = quiz.questions.map {
                if (it.id == updatedQuestion.id) updatedQuestion else it
            }
            _quiz.value = quiz.copy(questions = updatedQuestions)
        }
    }

    fun saveChanges() {
        _quiz.value?.let { quiz ->
            quiz.questions.forEach { question ->
                quizRepo.updateQuestion(quiz.id, question.id, question.text)
            }
        }
    }
}