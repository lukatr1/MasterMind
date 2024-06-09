package com.example.mastermind.data
import com.example.mastermind.data.Question
import com.example.mastermind.data.QuestionMultipleChoice

class QuestionRepo {

    private val questions = mutableListOf<Question>()

    private fun createUniqueId(): Int {
        var id = 0
        while (!questions.none { it.id == id}) {
            id++
        }
        return id
    }

    fun createMultipleChoiceQuestion(choicesTrue: List<String>, choicesFalse: List<String>, text: String) {
        val question =  QuestionMultipleChoice(
            choicesTrue = choicesTrue,
            choicesFalse = choicesFalse,
            id = createUniqueId(),
            text = text
        )
        questions.add(question)

    }

    fun createTrueFalseQuestion(answer: Boolean,  text: String) {
        val question = QuestionTrueFalse(
            answer = answer,
            id = createUniqueId(),
            text = text
        )
        questions.add(question)
    }

    fun getQuestions(): List<Question> {
        return (questions)
    }
}