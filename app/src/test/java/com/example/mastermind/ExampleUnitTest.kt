package com.example.mastermind

import org.junit.Test
import com.example.mastermind.data.QuestionRepo
import com.example.mastermind.data.GetQuizRepoProvider
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class QuizRepoUnitTest {
    @Test
    fun createdQuestionIsStoredInRepo() {
        val repo = QuestionRepo()
        repo.createMultipleChoiceQuestion(
            choicesTrue = listOf("a", "b"),
            choicesFalse = listOf("c", "d"),
            text = "What is the capital of France?"
        )
        repo.createTrueFalseQuestion(
            answer = true,
            text = "Is the earth flat?"
        )
        assertEquals(2, repo.getQuestions().size)
    }
    @Test
    fun createdQuizIsStoredInRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        quizRepo.createQuiz("Quiz 1")
        assertEquals( 1, quizRepo.getAllQuizzes().size)
    }
    @Test
    fun QuizIsDeletedFromRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId = quizRepo.createQuiz("Quiz 1")
        quizRepo.deleteQuiz(quizId)
        assertEquals( 0, quizRepo.getAllQuizzes().size)
    }
    @Test
    fun CorrectQuizIsDeletedFromRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId1 = quizRepo.createQuiz("Quiz to delete")
        val quizId2 = quizRepo.createQuiz("Quiz to stay")
        quizRepo.deleteQuiz(quizId1)
        assertEquals( 1, quizRepo.getAllQuizzes().size)
        assertEquals( "Quiz to stay", quizRepo.getAllQuizzes()[0].name)
    }
    @Test
    fun IdsAreUnique() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId1 = quizRepo.createQuiz("Quiz 1")
        val quizId2 = quizRepo.createQuiz("Quiz 2")
        assertNotEquals(quizId1, quizId2)
    }
    @Test
    fun GetQuizByIdReturnsCorrectQuiz() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId = quizRepo.createQuiz("Quiz 1")
        assertEquals( "Quiz 1", quizRepo.getQuizById(quizId).name)
    }
    @Test
    fun QuestionsAreAddedToQuiz() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId = quizRepo.createQuiz("Quiz 1")
        val questionRepo = QuestionRepo()
        questionRepo.createMultipleChoiceQuestion(
            choicesTrue = listOf("a", "b"),
            choicesFalse = listOf("c", "d"),
            text = "What is the capital of France?"
        )
        assertEquals( 1, quizRepo.getQuestionsByQuizId(quizId).size)
    }
}