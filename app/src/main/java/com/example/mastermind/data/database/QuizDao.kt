/*
package com.example.mastermind.data.database
import androidx.room.Dao
import androidx.room.Query
import com.example.mastermind.data.models.Quiz

@Dao
interface QuizDao {
    @Query("INSERT INTO quiz VALUES (:quiz)")
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quiz")
    suspend fun getQuizzes(): List<Quiz>

    @Query("SELECT * FROM quiz WHERE id = :id")
    suspend fun getQuizById(id: Int): Quiz


}*/