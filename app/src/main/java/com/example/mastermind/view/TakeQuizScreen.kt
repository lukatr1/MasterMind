package com.example.mastermind.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.QuestionMultipleChoice
import com.example.mastermind.data.models.QuestionTrueFalse
import com.example.mastermind.viewModel.TakeQuizScreenViewModel

data class TakeQuizScreen(val quizId: Int) : Screen {

    @Composable
    override fun Content() {
        val viewModel: TakeQuizScreenViewModel = viewModel()
        val quiz by viewModel.quiz.observeAsState()

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
                        QuestionItem(question = question)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun QuestionItem(question: Question) {
        Column {
            Text(
                text = question.id.toString() + ". " + question.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (question) {
                is QuestionMultipleChoice -> {
                    val shuffledChoices = (question.choicesTrue + question.choicesFalse).shuffled() // shuffle cards
                    shuffledChoices.forEach { choice ->
                        AnswerCard(choice = choice, isCorrect = question.choicesTrue.contains(choice))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                is QuestionTrueFalse -> {
                    val trueFalseChoices = listOf("True", "False").shuffled() // shuffle cards
                    AnswerCard(choice = trueFalseChoices[0], isCorrect = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    AnswerCard(choice = trueFalseChoices[1], isCorrect = false)
                }
            }
            }
        }
    }

    @Composable
    fun AnswerCard(choice: String, isCorrect: Boolean) {
        var backgroundColor by remember { mutableStateOf(Color.hsl(189F, 1F, 0.4F)) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    backgroundColor = if (isCorrect) Color.Green else Color.Red
                },
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
            }
    }
}