package com.example.mastermind.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Quiz

class BookmarkedScreenViewModel : ViewModel() {
    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance()

    private val _bookmarkedQuizzes = MutableLiveData<List<Quiz>>()
    val bookmarkedQuizzes: LiveData<List<Quiz>>
        get() = _bookmarkedQuizzes

    fun getBookmarkedQuizzes() {
        _bookmarkedQuizzes.value = quizRepo.getBookmarkedQuizzes()
    }

    /*fun unbookmarkQuiz(quiz: Quiz) {
        quiz.bookmarked = false
        quizRepo.updateQuiz(quiz)
        getBookmarkedQuizzes()
    }*/

    fun unbookmarkQuiz(quiz: Quiz) {
        quizRepo.unbookmarkQuiz(quiz)
        getBookmarkedQuizzes()
    }
}