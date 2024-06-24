
package com.example.mastermind.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mastermind.data.Converters
import com.example.mastermind.data.models.*

@Database(entities = [QuizEntity::class, QuestionEntity::class, QuizQuestionCrossRef::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao


}
