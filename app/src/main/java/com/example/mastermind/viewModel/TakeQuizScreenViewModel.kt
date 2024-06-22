package com.example.mastermind.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz

class TakeQuizScreenViewModel(private var context: Context) : ViewModel() {
    private fun getContext () : Context {
        return context
    }
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(getContext())

    private val _quiz = MutableLiveData<Quiz>()
    val quiz: LiveData<Quiz>
        get() = _quiz

    fun loadQuiz(quizId: Int) {
        _quiz.value = quizRepo.getQuizById(quizId)
    }
}