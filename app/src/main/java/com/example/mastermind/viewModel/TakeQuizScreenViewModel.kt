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

class TakeQuizScreenViewModel(context: Context) : ViewModel() {

    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(context)

    private val _quiz = MutableLiveData<Quiz>()
    val quiz: LiveData<Quiz>
        get() = _quiz

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch {
        _quiz.value = quizRepo.getQuizById(quizId)
    }}
}