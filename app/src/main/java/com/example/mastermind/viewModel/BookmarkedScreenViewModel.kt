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

class BookmarkedScreenViewModel(context: Context) : ViewModel() {

    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(context)

    private val _bookmarkedQuizzes = MutableLiveData<List<Quiz>>()
    val bookmarkedQuizzes: LiveData<List<Quiz>>
        get() = _bookmarkedQuizzes

    fun getBookmarkedQuizzes() {
        viewModelScope.launch {
            _bookmarkedQuizzes.value = quizRepo.getBookmarkedQuizzes()
        }
    }

    fun unbookmarkQuiz(quiz: Quiz) {
        viewModelScope.launch {
            quizRepo.unbookmarkQuiz(quiz)
            getBookmarkedQuizzes()
        }
    }
}
