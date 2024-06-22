package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.QuestionMultipleChoice
import com.example.mastermind.data.models.QuestionTrueFalse
import com.example.mastermind.viewModel.TakeQuizScreenViewModel

data class TakeQuizScreen(val quizId: Int, private var context: Context) : Screen {
    private fun getContext () : Context {
        return context
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: TakeQuizScreenViewModel = viewModel()
        val quiz by viewModel.quiz.observeAsState()

        // Track correct answers
        var correctAnswers by remember { mutableStateOf(0) }
        // Track answered questions count
        var answeredQuestions by remember { mutableStateOf(0) }

        // Show popup when quiz is completed
        var showQuizCompletedPopup by remember { mutableStateOf(false) }

        LaunchedEffect(quizId) {
            viewModel.loadQuiz(quizId)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            quiz?.let { q ->
                Text(
                    text = " ${q.name}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(q.questions) { question ->
                        QuestionItem(
                            question = question,
                            onAnswered = { isCorrect ->
                                if (isCorrect) {
                                    correctAnswers++
                                }
                                answeredQuestions++
                                // Check if all questions are answered
                                if (answeredQuestions == q.questions.size) {
                                    showQuizCompletedPopup = true
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // Popup for quiz completion
        if (showQuizCompletedPopup) {
            AlertDialog(
                onDismissRequest = {
                    showQuizCompletedPopup = false
                },
                title = {
                    Text("Quiz Completed")
                },
                text = {
                    Text("$correctAnswers out of ${quiz?.questions?.size} correct")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            navigator?.pop()
                            navigator?.push(HomeScreen(getContext()))
                            showQuizCompletedPopup = false
                        }
                    ) {
                        Text("Return Home")
                    }
                }
            )
        }
    }

    @Composable
    fun QuestionItem(question: Question, onAnswered: (Boolean) -> Unit) {
        var isAnswered by remember { mutableStateOf(false) }
        var isCorrect by remember { mutableStateOf(false) }

        Column {
            Text(
                text = question.id.toString() + ". " + question.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (question) {
                is QuestionMultipleChoice -> {
                    val shuffledChoices =
                        (question.choicesTrue + question.choicesFalse).shuffled() // shuffle cards
                    shuffledChoices.forEach { choice ->
                        AnswerCard(
                            choice = choice,
                            isCorrect = question.choicesTrue.contains(choice),
                            onClick = {
                                if (!isAnswered) {
                                    isAnswered = true
                                    isCorrect = question.choicesTrue.contains(choice)
                                    onAnswered(isCorrect)
                                }
                            },
                            showResult = isAnswered && isCorrect == question.choicesTrue.contains(choice)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                is QuestionTrueFalse -> {
                    val trueFalseChoices =
                        listOf(question.answer, question.answer.not()).shuffled()

                    AnswerCard(
                        choice = trueFalseChoices[0].toString().uppercase(),
                        isCorrect = trueFalseChoices[0] == question.answer,
                        onClick = {
                            if (!isAnswered) {
                                isAnswered = true
                                isCorrect = trueFalseChoices[0] == question.answer
                                onAnswered(isCorrect)
                            }
                        },
                        showResult = isAnswered && isCorrect == (trueFalseChoices[0] == question.answer)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AnswerCard(
                        choice = trueFalseChoices[1].toString().uppercase(),
                        isCorrect = trueFalseChoices[1] == question.answer,
                        onClick = {
                            if (!isAnswered) {
                                isAnswered = true
                                isCorrect = trueFalseChoices[1] == question.answer
                                onAnswered(isCorrect)
                            }
                        },
                        showResult = isAnswered && isCorrect == (trueFalseChoices[1] == question.answer)
                    )
                }
            }

            if (isAnswered) {
                val resultText = if (isCorrect) "Correct!" else "Incorrect!"
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCorrect) Color.Green else Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun AnswerCard(choice: String, isCorrect: Boolean, onClick: () -> Unit, showResult: Boolean) {
    var backgroundColor by remember { mutableStateOf(Color.hsl(189F, 1F, 0.4F)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !showResult, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = choice,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            if (showResult) {
                val icon = if (isCorrect) Icons.Filled.CheckCircle else Icons.Filled.Close
                Icon(
                    icon,
                    contentDescription = if (isCorrect) "Correct" else "Incorrect",
                    tint = if (isCorrect) Color.Green else Color.Red,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.TopEnd)

                )
            }
        }
    }
}