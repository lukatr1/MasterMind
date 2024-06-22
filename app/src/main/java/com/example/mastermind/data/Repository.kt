package com.example.mastermind.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.mastermind.data.models.*
import com.example.mastermind.data.database.*
import com.example.mastermind.viewModel.BookmarkedScreenViewModel

interface QuizRepo {
    suspend fun getAllQuizzes(): List<Quiz>
    suspend fun getQuizById(id: Int): Quiz
    suspend fun createQuiz(name: String):Int
    suspend fun deleteQuiz(id: Int)
    suspend fun removeQuestionFromQuiz(quizId: Int, questionId: Int)
    suspend fun createMultipleChoiceQuestion(quizId: Int, choicesTrue: List<String>, choicesFalse: List<String>, text: String):Int
    suspend fun createTrueFalseQuestion(quizId: Int, answer: Boolean, text: String):Int
    suspend fun getQuestionsByQuizId(quizId: Int): List<Question>
    suspend fun getQuestionByQuizAndQuestionId(quizId: Int, questionId: Int) : Question?
    suspend fun updateQuestion(quizId: Int, questionId: Int, newText: String): Boolean
    suspend fun unbookmarkQuiz(quiz: Quiz)
    fun getBookmarkedQuizzes(): List<Quiz>
}

class GetQuizRepoProvider {
    private var instance: QuizRepo? = null

    fun getsInstance(context: Context): QuizRepo {
        if (instance == null) {
            instance = QuizRepoImpl(getDatabase(context).quizDao())
        }
        return instance!!
    }

    private fun getDatabase(context: Context): QuizDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            QuizDatabase::class.java,
            "quiz_database"
        ).build()
    }

    fun setInstance(repo: QuizRepo) {
        instance = repo
    }
}


class QuizRepoImpl(private val quizDao: QuizDao) : QuizRepo {

    private val allQuizzes = mutableListOf<Quiz>()

    override suspend fun getAllQuizzes(): List<Quiz> {
        return quizDao.getAllQuizzes().map { quizEntity ->
            Quiz(
                id = quizEntity.id,
                name = quizEntity.name,
                questions = quizDao.getQuestionsByQuizId(quizEntity.id).map { questionEntity ->
                    mapQuestionEntityToQuestion(questionEntity)
                }
            )
        }
    }

    override fun getBookmarkedQuizzes(): List<Quiz> {
        return allQuizzes.filter { it.bookmarked }
    }

    override suspend fun getQuizById(id: Int): Quiz {
        val quizEntity = quizDao.getQuizById(id)
        val questions = quizDao.getQuestionsByQuizId(id).map { questionEntity ->
            mapQuestionEntityToQuestion(questionEntity)
        }
        if (quizEntity != null) {
            return Quiz(
                id = quizEntity.id,
                name = quizEntity.name,
                questions = questions
            )
        }
        return Quiz(id = -1, name = "Error", questions = emptyList())
    }

    override suspend fun createQuiz(name: String): Int {
        val quizEntity = QuizEntity(name = name)
        return quizDao.insertQuiz(quizEntity).toInt()
    }

    override suspend fun unbookmarkQuiz(quiz: Quiz) {
        val updatedQuiz = quiz.copy(bookmarked = false)
        allQuizzes[allQuizzes.indexOf(quiz)] = updatedQuiz
    }

    override suspend fun deleteQuiz(id: Int) {
        quizDao.deleteQuizById(id)
    }

    override suspend fun removeQuestionFromQuiz(quizId: Int, questionId: Int) {
        quizDao.removeQuestionFromQuiz(quizId, questionId)
    }

    override suspend fun createMultipleChoiceQuestion(quizId: Int, choicesTrue: List<String>, choicesFalse: List<String>, text: String): Int {
        // Create the base question entity
        val questionEntity = QuestionEntity(
            id = -1,
            text = text,
            quizId = quizId,
            questionType = "multiple_choice",
            choicesTrue = choicesTrue,
            choicesFalse = choicesFalse
        )

        // Insert the question and get its ID
        val questionId = quizDao.insertQuestion(questionEntity).toInt()

        // Link the question to the quiz
        linkQuestionToQuiz(quizId, questionId)

        return questionId
    }

    override suspend fun createTrueFalseQuestion(quizId: Int, answer: Boolean, text: String): Int {
        // Create the base question entity
        val questionEntity = QuestionEntity(
            text = text,
            quizId = quizId,
            questionType = "true_false",
            answer = answer,
            id = -1
        )

        // Insert the question and get its ID
        val questionId = quizDao.insertQuestion(questionEntity).toInt()

        // Link the question to the quiz
        linkQuestionToQuiz(quizId, questionId)

        return questionId
    }

    override suspend fun getQuestionsByQuizId(quizId: Int): List<Question> {
        return quizDao.getQuestionsByQuizId(quizId).map { questionEntity ->
            mapQuestionEntityToQuestion(questionEntity)
        }
    }

    override suspend fun getQuestionByQuizAndQuestionId(quizId: Int, questionId: Int): Question? {
        val questionEntity = quizDao.getQuestionByQuizAndQuestionId(quizId, questionId)
        return questionEntity?.let { mapQuestionEntityToQuestion(it) }
    }

    override suspend fun updateQuestion(quizId: Int, questionId: Int, newText: String): Boolean {
        quizDao.updateQuestionText(questionId, newText)
        return true
    }

    private fun mapQuestionEntityToQuestion(questionEntity: QuestionEntity): Question {
        return when (questionEntity.questionType) {
            "multiple_choice" -> {
                QuestionMultipleChoice(
                    id = questionEntity.id,
                    text = questionEntity.text,
                    choicesTrue = questionEntity.choicesTrue ?: emptyList(),
                    choicesFalse = questionEntity.choicesFalse ?: emptyList(),
                    quizId = questionEntity.quizId
                )
            }
            "true_false" -> {
                QuestionTrueFalse(
                    id = questionEntity.id,
                    text = questionEntity.text,
                    answer = questionEntity.answer ?: false, // Default to false if answer is null
                    quizId = questionEntity.quizId
                )
            }
            else -> {
                // Default case: handle as regular Question
                Question(
                    id = questionEntity.id,
                    text = questionEntity.text,

                )
            }
        }
    }

    private suspend fun linkQuestionToQuiz(quizId: Int, questionId: Int) {
        try {
            // Check if the quizId exists
            val quizExists = quizDao.getQuizById(quizId) != null
            if (!quizExists) {
                throw IllegalArgumentException("Quiz with id $quizId does not exist.")
            }

            // Check if the questionId exists
            val questionExists = quizDao.getQuestionById(questionId) != null
            if (!questionExists) {
                Log.d("TAG", "Question with id $questionId does not exist.")
                return
            }

            // All IDs exist, proceed to link them
            val crossRef = QuizQuestionCrossRef(quizId = quizId, questionId = questionId)
            quizDao.insertQuizQuestionCrossRef(crossRef)

            // Log successful linking
            Log.d("TAG", "Linked quiz with id $quizId to question with id $questionId")
        } catch (e: IllegalArgumentException) {
            // Log the exception and rethrow it for higher-level handling
            Log.e("TAG", "Error linking question to quiz: ${e.message}")
            throw e
        } catch (e: Exception) {
            // Log any unexpected exceptions
            Log.e("TAG", "Unexpected error linking question to quiz: ${e.message}")
            throw e
        }
    }
}






