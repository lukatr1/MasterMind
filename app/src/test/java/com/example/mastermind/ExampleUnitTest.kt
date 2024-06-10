package com.example.mastermind

import org.junit.Test
import org.junit.Before

import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.QuizRepo
import com.example.mastermind.data.QuizRepoImpl
import com.example.mastermind.data.models.QuestionTrueFalse
import org.junit.Assert.*



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class QuizRepoUnitTest {
    @Test
    fun createdQuizIsStoredInRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        quizRepo.createQuiz("Quiz 1")
        assertEquals( 1, quizRepo.getAllQuizzes().size)
    }
    @Test
    fun createdQuizHasCorrectName() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId = quizRepo.createQuiz("Quiz 1")
        assertEquals("Quiz 1", quizRepo.getQuizById(quizId).name)
    }

    @Test
    fun quizIsDeletedFromRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId = quizRepo.createQuiz("Quiz 1")
        quizRepo.deleteQuiz(quizId)
        assertEquals( 0, quizRepo.getAllQuizzes().size)
    }
    @Test
    fun correctQuizIsDeletedFromRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId1 = quizRepo.createQuiz("Quiz to delete")
        quizRepo.createQuiz("Quiz to keep")
        quizRepo.deleteQuiz(quizId1)
        assertEquals( 1, quizRepo.getAllQuizzes().size)
        assertEquals( "Quiz to keep", quizRepo.getAllQuizzes()[0].name)
    }
    @Test
    fun deletingNonexistentQuizDoesNotAffectRepo() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        quizRepo.createQuiz("Quiz 1")
        quizRepo.deleteQuiz(99)
        assertEquals(1, quizRepo.getAllQuizzes().size)
    }
    @Test
    fun idsAreUnique() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId1 = quizRepo.createQuiz("Quiz 1")
        val quizId2 = quizRepo.createQuiz("Quiz 2")
        assertNotEquals(quizId1, quizId2)
    }
    @Test
    fun getQuizByIdReturnsCorrectQuiz() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getInstance()
        val quizId = quizRepo.createQuiz("Quiz 1")
        assertEquals( "Quiz 1", quizRepo.getQuizById(quizId).name)
    }
}

class QuizRepoQuestionsTest {

    private lateinit var quizRepo: QuizRepo

    @Before
    fun setup() {
        quizRepo = QuizRepoImpl()
    }
    @Test
    fun getQuestionByQuizAndQuestionIdReturnsCorrectQuestion() {
        val quizId = quizRepo.createQuiz("Quiz with a question")
        val questionId = quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Sample Question"
        )
        val question = quizRepo.getQuestionByQuizAndQuestionId(quizId, questionId)
        assertEquals("Sample Question", question?.text)
    }
    @Test
    fun questionIsRemovedFromQuiz() {
        val quizId = quizRepo.createQuiz("Quiz with questions")
        val questionId = quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Sample Question"
        )
        quizRepo.removeQuestionFromQuiz(quizId, questionId)
        assertEquals(0, quizRepo.getQuestionsByQuizId(quizId).size)
    }

    @Test
    fun createMultipleChoiceQuestionAddsQuestionToQuiz() {
        val quizId = quizRepo.createQuiz("Quiz with multiple choice question")
        quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Sample Multiple Choice Question"
        )
        val questions = quizRepo.getQuestionsByQuizId(quizId)
        assertEquals(1, questions.size)
        assertEquals("Sample Multiple Choice Question", questions[0].text)
    }

    @Test
    fun createTrueFalseQuestionAddsQuestionToQuiz() {
        val quizId = quizRepo.createQuiz("Quiz with true/false question")
        quizRepo.createTrueFalseQuestion(
            quizId,
            answer = true,
            text = "Sample True/False Question"
        )
        val questions = quizRepo.getQuestionsByQuizId(quizId)
        assertEquals(1, questions.size)
        assertEquals("Sample True/False Question", questions[0].text)
        assertEquals(true, (questions[0] as QuestionTrueFalse).answer)
    }

    @Test
    fun getQuestionsByQuizIdReturnsCorrectQuestions() {
        val quizId = quizRepo.createQuiz("Quiz with multiple questions")
        quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Sample Multiple Choice Question"
        )
        quizRepo.createTrueFalseQuestion(
            quizId,
            answer = false,
            text = "Sample True/False Question"
        )
        val questions = quizRepo.getQuestionsByQuizId(quizId)
        assertEquals(2, questions.size)
    }

    @Test
    fun uniqueIdGeneratesUniqueIds() {
        val quizId1 = quizRepo.createQuiz("Quiz 1")
        val quizId2 = quizRepo.createQuiz("Quiz 2")
        assertNotEquals(quizId1, quizId2)
    }

    @Test
    fun uniqueQuestionIdGeneratesUniqueQuestionIds() {
        val quizId = quizRepo.createQuiz("Quiz with unique questions")
        val questionId1 = quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Sample Question 1"
        )
        val questionId2 = quizRepo.createTrueFalseQuestion(
            quizId,
            answer = true,
            text = "Sample Question 2"
        )
        assertNotEquals(questionId1, questionId2)
    }
    @Test
    fun updateQuestion_Successful() {
        val quizId = quizRepo.createQuiz("Quiz with questions")
        val questionId = quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Original Question Text"
        )

        val updated = quizRepo.updateQuestion(quizId, questionId, "Updated Question Text")

        assertTrue(updated)
        val updatedQuestion = quizRepo.getQuestionByQuizAndQuestionId(quizId, questionId)
        assertEquals("Updated Question Text", updatedQuestion?.text)
    }

    @Test
    fun updateQuestion_InvalidQuizId() {
        val quizId = quizRepo.createQuiz("Quiz with questions")
        val questionId = quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Original Question Text"
        )

        val updated = quizRepo.updateQuestion(999, questionId, "Updated Question Text")

        assertFalse(updated)
        val originalQuestion = quizRepo.getQuestionByQuizAndQuestionId(quizId, questionId)
        assertEquals("Original Question Text", originalQuestion?.text)
    }

    @Test
    fun updateQuestion_InvalidQuestionId() {
        val quizId = quizRepo.createQuiz("Quiz with questions")
        quizRepo.createMultipleChoiceQuestion(
            quizId,
            choicesTrue = listOf("Correct answer"),
            choicesFalse = listOf("Wrong answer 1", "Wrong answer 2"),
            text = "Original Question Text"
        )

        val updated = quizRepo.updateQuestion(quizId, 999, "Updated Question Text")

        assertFalse(updated)
    }
}


