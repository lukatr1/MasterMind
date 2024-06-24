package com.example.mastermind.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.QuestionMultipleChoice
import com.example.mastermind.data.models.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class CreateQuestionViewModel(context: Context, private val quizId: Int) : ViewModel() {

    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(context)
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questionsList = quizRepo.getQuestionsByQuizId(quizId = quizId)
                _questions.value = questionsList
                log("CreateQuestionViewModel", "Loaded ${questionsList.size} questions")
                log(questionsList[1])
            } catch (e: Exception) {
                log("CreateQuestionViewModel", "Error loading questions: ${e.message}")
            }
        }
    }

    fun createMultipleChoiceQuestion(
        quizId: Int,
        choicesTrue: List<String>,
        choicesFalse: List<String>,
        text: String,
        onCreateSuccess: (Int) -> Unit
    ) {
        viewModelScope.launch {
            val id = quizRepo.createMultipleChoiceQuestion(quizId, choicesTrue, choicesFalse, text)
            onCreateSuccess(id)
            loadQuestions()
        }
    }

    suspend fun getQuestionById(questionId: Int): Question? {
        return flow {
            _questions.collect { questionsList ->
                val question = questionsList.find { it.id == questionId }
                emit(question)
            }
        }.singleOrNull() // Return null if no question found
    }
    fun createTrueFalseQuestion(
        quizId: Int,
        answer: Boolean,
        text: String,
        onCreateSuccess: (Int) -> Unit
    ) {
        viewModelScope.launch {
            val questionId = quizRepo.createTrueFalseQuestion(quizId, answer, text)
            onCreateSuccess(questionId)
            loadQuestions()
        }
    }

    fun updateQuestion(
        quizId: Int,
        questionId: Int,
        newText: String,
        onUpdateSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val success = quizRepo.updateQuestion(quizId, questionId, newText)
            if (success) {
                onUpdateSuccess()
                loadQuestions()
            }
        }
    }
}



class QuestionsOverviewViewModel(context: Context, private val quizId: Int) : ViewModel() {

    private val quizRepo: QuizRepo = GetQuizRepoProvider().getsInstance(context)
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _questions.value = quizRepo.getQuestionsByQuizId(quizId)
        }
    }

    private fun refreshQuestions() {
        viewModelScope.launch {
            _questions.value = quizRepo.getQuestionsByQuizId(quizId)
        }
    }

    fun getQuestionById(questionId: Int): Question? {
        return _questions.value.find { it.id == questionId }
    }

    fun deleteQuestion(questionId: Int) {
        viewModelScope.launch {
            quizRepo.removeQuestionFromQuiz(questionId)
            refreshQuestions()
        }
    }
}


