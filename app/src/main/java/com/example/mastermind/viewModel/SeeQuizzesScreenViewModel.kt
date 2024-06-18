package com.example.mastermind.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz

class SeeQuizzesScreenViewModel : ViewModel() {
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getInstance()

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

    init {
        getAllQuizzes()
    }

    fun getAllQuizzes() {
        _quizzes.value = quizRepo.getAllQuizzes()
    }

    fun updateQuizzes() {
        _quizzes.postValue(quizRepo.getAllQuizzes())
    }
}