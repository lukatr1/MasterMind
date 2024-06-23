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

    @Query("SELECT * FROM questions WHERE id IN (SELECT questionId FROM quiz_questions WHERE quizId = :quizId)")
    suspend fun getQuestionsByQuizId(quizId: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id = :questionId AND id IN (SELECT questionId FROM quiz_questions WHERE quizId = :quizId)")
    suspend fun getQuestionByQuizAndQuestionId(quizId: Int, questionId: Int): QuestionEntity?

//    @Query("DELETE FROM quiz_questions WHERE quizId = :quizId AND questionId = :questionId")
  //  suspend fun removeQuestionFromQuiz(quizId: Int, questionId: Int)

    //@Query("DELETE FROM questions JOIN quiz_questions ON questions.quiz_id = quiz_questions.quizID INNER JOIN quizzes ON quiz_questions.quizID = quizzes.id WHERE quizzes.id = :quizId AND questions.quiz_id = :quizID")
    //suspend fun removeQuestionFromQuiz(quizId: Int, questionId: Int)

    @Query("DELETE FROM questions WHERE quiz_id = :quizId")
    suspend fun removeQuestionFromQuiz(quizId: Int)


    @Query("UPDATE questions SET text = :newText WHERE id = :questionId")
    suspend fun updateQuestionText(questionId: Int, newText: String)

    @Insert
    suspend fun insertQuizQuestionCrossRef(crossRef: QuizQuestionCrossRef)
}

