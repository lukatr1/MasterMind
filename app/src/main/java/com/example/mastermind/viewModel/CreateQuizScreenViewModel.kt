package com.example.mastermind.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz
import com.example.mastermind.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch


class CreateQuizScreenViewModel(private val context: Context) : ViewModel() {

    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(context)

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

    @SuppressLint("SuspiciousIndentation")
    suspend fun createQuiz(name: String): Int {
        var author = "Guest"
            if (SharedPreferencesHelper.isLoggedIn(context)) {
            author = SharedPreferencesHelper.getUsername(context).toString()
            }
        val id = quizRepo.createQuiz(name = name, author = author)
        return id
    }
}