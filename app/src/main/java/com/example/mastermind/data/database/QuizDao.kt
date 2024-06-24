package com.example.mastermind.data.database

import androidx.room.*
import com.example.mastermind.data.models.QuestionEntity
import com.example.mastermind.data.models.QuizEntity
import com.example.mastermind.data.models.QuizQuestionCrossRef

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: QuizEntity): Long

    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzes(): List<QuizEntity>

    @Query("SELECT * FROM quizzes WHERE id = :id")
    suspend fun getQuizById(id: Int): QuizEntity?

    @Query("DELETE FROM quizzes WHERE id = :id")
    suspend fun deleteQuizById(id: Int)

    @Insert
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): QuestionEntity?

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Query("SELECT * FROM questions WHERE quiz_Id = :quizId")
    suspend fun getQuestionsByQuizId(quizId: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id = :questionId AND  quiz_Id = :quizId")
    suspend fun getQuestionByQuizAndQuestionId(quizId: Int, questionId: Int): QuestionEntity?

    @Query("DELETE FROM questions WHERE quiz_id = :quizId")
    suspend fun removeQuestionFromQuiz(quizId: Int)


    @Query("UPDATE questions SET text = :newText WHERE id = :questionId")
    suspend fun updateQuestionText(questionId: Int, newText: String)

    @Insert
    suspend fun insertQuizQuestionCrossRef(crossRef: QuizQuestionCrossRef)

    @Query("SELECT * FROM quizzes WHERE isBookmarked = 1")
    suspend fun getBookmarkedQuizzes(): List<QuizEntity>

    @Query("UPDATE quizzes SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateQuizBookmarkStatus(id: Int, isBookmarked: Boolean)

    @Query("SELECT choices_true FROM questions WHERE id = :questionId")
    suspend fun getAllChoicesTrue(questionId: Int): List<String>

}

