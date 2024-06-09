package com.example.mastermind.data

open class Question(
    open val id: Int,
    open val text: String
)

data class QuestionMultipleChoice(
    val choicesTrue: List<String>,
    val choicesFalse: List<String>,
    override val id: Int,
    override  val text: String
) : Question(id, text)

data class QuestionTrueFalse(
    val answer: Boolean,
    override val id: Int,
    override val text: String
) : Question(id, text)
