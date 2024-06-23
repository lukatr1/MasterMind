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

    suspend fun getBookmarkedQuizzes() {
        _bookmarkedQuizzes.value = quizRepo.getBookmarkedQuizzes()
    }

    /*fun unbookmarkQuiz(quiz: Quiz) {
        quiz.bookmarked = false
        quizRepo.updateQuiz(quiz)
        getBookmarkedQuizzes()
    }*/

    suspend fun unbookmarkQuiz(quiz: Quiz) {
        viewModelScope.launch {
        quizRepo.unbookmarkQuiz(quiz)}
        getBookmarkedQuizzes()
    }
}