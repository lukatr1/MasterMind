package com.example.mastermind.data.models

import androidx.room.*
import com.example.mastermind.data.Converters

@Entity(tableName = "questions")
open class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "quiz_id") var quizId: Int,
    @ColumnInfo(name = "question_type") var questionType: String,
    // Nullable properties for multiple choice questions
    @ColumnInfo(name = "choices_true")
@TypeConverters(Converters::class)
var choicesTrue: List<String>? = null,

@ColumnInfo(name = "choices_false")
@TypeConverters(Converters::class)
var choicesFalse: List<String>? = null,

// Nullable properties for true/false questions
@ColumnInfo(name = "answer")
var answer: Boolean? = null
) {



}




open class Question(
    open val id: Int,
    open val text: String
)

data class QuestionMultipleChoice(
    val choicesTrue: List<String>,
    val choicesFalse: List<String>,
    override val id: Int,
    override  val text: String,
    val quizId: Int
) : Question(id, text)

data class QuestionTrueFalse(
    val answer: Boolean,
    override val id: Int,
    override val text: String,
    val quizId: Int
) : Question(id, text)
