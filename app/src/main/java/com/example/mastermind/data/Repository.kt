package com.example.mastermind.data

import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.QuestionMultipleChoice
import com.example.mastermind.data.models.QuestionTrueFalse
import com.example.mastermind.data.models.Quiz


interface QuizRepo {
    fun getAllQuizzes(): List<Quiz>
    fun getQuizById(id: Int): Quiz
    fun createQuiz(name: String):Int
    fun deleteQuiz(id: Int)
    fun removeQuestionFromQuiz(quizId: Int, questionId: Int)
    fun createMultipleChoiceQuestion(quizId: Int, choicesTrue: List<String>, choicesFalse: List<String>, text: String):Int
    fun createTrueFalseQuestion(quizId: Int, answer: Boolean, text: String):Int
    fun getQuestionsByQuizId(quizId: Int): List<Question>
    fun getQuestionByQuizAndQuestionId(quizId: Int, questionId: Int) : Question?
    fun updateQuestion(quizId: Int, questionId: Int, newText: String): Boolean
}

class GetQuizRepoProvider {
    private var instance: QuizRepo? = null

    fun getInstance(): QuizRepo {
        if (instance == null) {
            instance = QuizRepoImpl()
        }
        return instance!!
    }

    fun setInstance(repo: QuizRepo) {
        instance = repo
    }
}

class QuizRepoImpl : QuizRepo {

    private val allQuizzes = mutableListOf<Quiz>()

    override fun getAllQuizzes(): List<Quiz>{
        return allQuizzes
    }
    override fun getQuizById(id: Int): Quiz {
        return allQuizzes.first { it.id == id }
    }
    override fun createQuiz(name: String): Int {
        val specificID = uniqueId()
        allQuizzes.add(Quiz(specificID, name, emptyList()))
        return specificID
    }
    override fun deleteQuiz(id: Int) {
        allQuizzes.removeIf { it.id == id }
    }
    override fun removeQuestionFromQuiz(quizId: Int, questionId: Int) {
        val quiz = allQuizzes.find { it.id == quizId }
        if (quiz != null) {
            val updatedQuestions = quiz.questions.filterNot { it.id == questionId }
            allQuizzes[allQuizzes.indexOf(quiz)] = quiz.copy(questions = updatedQuestions)
        }
    }

    override fun createMultipleChoiceQuestion(quizId: Int, choicesTrue: List<String>, choicesFalse: List<String>, text: String): Int {
        val questionId = uniqueQuestionId(quizId)
        val question = QuestionMultipleChoice(choicesTrue, choicesFalse, questionId, text)
        val quiz = allQuizzes.find { it.id == quizId }
        if (quiz != null) {
            val updatedQuestions = quiz.questions + question
            allQuizzes[allQuizzes.indexOf(quiz)] = quiz.copy(questions = updatedQuestions)
        }
        return questionId
    }

    override fun createTrueFalseQuestion(quizId: Int, answer: Boolean, text: String): Int {
        val questionId = uniqueQuestionId(quizId)
        val question = QuestionTrueFalse(answer, questionId, text)
        val quiz = allQuizzes.find { it.id == quizId }
        if (quiz != null) {
            val updatedQuestions = quiz.questions + question
            allQuizzes[allQuizzes.indexOf(quiz)] = quiz.copy(questions = updatedQuestions)
        }
        return questionId
    }
    override fun getQuestionsByQuizId(quizId: Int): List<Question> {
        return allQuizzes.first { it.id == quizId }.questions
    }
    private fun uniqueId(): Int {
        var id = 1
        while (!allQuizzes.none { it.id == id}) {
            id++
        }
        return id
    }
    private fun uniqueQuestionId(quizId: Int): Int{
        var id = 1
        val quiz = allQuizzes.find { it.id == quizId }
        if (quiz != null) {
            while (!quiz.questions.none { it.id == id}) {
                id++
            }
        }
        return id
    }
    override fun getQuestionByQuizAndQuestionId(quizId: Int, questionId: Int): Question? {
        val quiz = allQuizzes.find { it.id == quizId }
        return quiz?.questions?.find { it.id == questionId }
    }
    override fun updateQuestion(quizId: Int, questionId: Int, newText: String): Boolean {
        val quiz = allQuizzes.find { it.id == quizId }
        val questionIndex = quiz?.questions?.indexOfFirst { it.id == questionId }
        if (quiz != null && questionIndex != null && questionIndex != -1) {
            val updatedQuestions = quiz.questions.toMutableList()
            updatedQuestions[questionIndex] = when (val question = updatedQuestions[questionIndex]) {
                is QuestionMultipleChoice -> question.copy(text = newText)
                is QuestionTrueFalse -> question.copy(text = newText)
                else -> return false // Unsupported question type
            }
            allQuizzes[allQuizzes.indexOf(quiz)] = quiz.copy(questions = updatedQuestions)
            return true
        }
        return false
    }

    // DUMMY DATA

    init {
        // Initialize with dummy data
        val quiz1Id = createQuiz("Quiz 1")
        createMultipleChoiceQuestion(
            quiz1Id,
            listOf("4"),
            listOf("2", "0", "1"),
            "What is 2 + 2?"
        )
        createMultipleChoiceQuestion(
            quiz1Id,
            listOf("True", "True"),
            listOf("False", "False"),
            "What color is the sky?"
        )
        createTrueFalseQuestion(
            quiz1Id,
            true,
            "Is the Earth round?"
        )
        createTrueFalseQuestion(
            quiz1Id,
            true,
            "TRUE is correct"
        )
        createTrueFalseQuestion(
            quiz1Id,
            false,
            "FALSE is correct"
        )


        val quiz2Id = createQuiz("Quiz 2")
        createMultipleChoiceQuestion(
            quiz2Id,
            listOf("Paris"),
            listOf("Bern", "Vienna"),
            "What is the capital of France?"
        )
        createTrueFalseQuestion(
            quiz2Id,
            false,
            "Is Earth flat?"
        )

        val quiz4Id = createQuiz("Quiz 3")
        createMultipleChoiceQuestion(
            quiz4Id,
            listOf("The Danish krone"),
            listOf("Yen", "Euro"),
            "What is the currency of Denmark? ?"
        )
    }
}
