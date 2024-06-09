package com.example.mastermind.data

import com.example.mastermind.data.Question


interface QuizRepo {
    fun getAllQuizzes(): List<DataQuiz>
    fun getQuizById(id: Int): DataQuiz
    fun createQuiz(name: String):Int
    fun deleteQuiz(id: Int)
    fun removeQuestionFromQuiz(quizId: Int, questionId: Int)
    fun createMultipleChoiceQuestion(choicesTrue: List<String>, id: Int, choicesFalse: List<String>, text: String)
    fun createTrueFalseQuestion(answer: Boolean, id: Int, text: String)
    fun getQuestionsByQuizId(quizId: Int): List<Question>
    fun uniqueId(): Int
}

class GetQuizRepoProvider() {
    private var instance: QuizRepo? = null
    fun getInstance(): QuizRepo {
        if (instance == null) {
            instance = QuizRepoImpl()
        }
        return instance!!
    }
}

class QuizRepoImpl : QuizRepo {

    private val allQuizzes = mutableListOf<DataQuiz>()

    override fun getAllQuizzes(): List<DataQuiz>{
        return allQuizzes
    }
    override fun getQuizById(id: Int): DataQuiz {
        return allQuizzes.first { it.id == id }
    }
    override fun createQuiz(name: String): Int {
        val specificID = uniqueId()
        allQuizzes.add(DataQuiz(specificID, name, emptyList()))
        return specificID
    }
    override fun deleteQuiz(id: Int) {
        allQuizzes.removeIf { it.id == id }
    }
    override fun removeQuestionFromQuiz(quizId: Int, questionId: Int) {
    }
    override fun createMultipleChoiceQuestion(choicesTrue: List<String>, id: Int, choicesFalse: List<String>, text: String) {

    }
    override fun createTrueFalseQuestion(answer: Boolean, id: Int, text: String) {
    }
    override fun getQuestionsByQuizId(quizId: Int): List<Question> {
        return allQuizzes.first { it.id == quizId }.questions
    }
    override fun uniqueId(): Int {
        var id = 1
        while (!allQuizzes.none { it.id == id}) {
            id++
        }
        return id
    }
}
