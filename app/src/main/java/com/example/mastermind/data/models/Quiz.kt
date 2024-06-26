package com.example.mastermind.data.models
import androidx.room.*

data class Quiz(
    val id: Int,
    val name: String,
    val questions: List<Question>,
    var isBookmarked: Boolean = false,
    var createdBy: String
)
@Entity(tableName = "quizzes")
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo("IsBookmarked") val isBookmarked: Boolean = false,
    @ColumnInfo("createdBy") val createdBy: String = "Guest"
)

@Entity(tableName = "quiz_questions",
    primaryKeys = ["quizId", "questionId"],
    foreignKeys = [
        ForeignKey(
            entity = QuizEntity::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)    data class QuizQuestionCrossRef(
    val quizId: Int,
    val questionId: Int
)
