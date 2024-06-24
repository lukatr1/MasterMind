package com.example.mastermind.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.log
import com.example.mastermind.viewModel.QuestionsOverviewViewModel

class QuestionsOverviewScreen(private val quizId: Int, private var context: Context) : Screen {
    private fun getContext(): Context {
        return context
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val viewModel = QuestionsOverviewViewModel(quizId = quizId, context = getContext())
        val questions by remember { viewModel.questions }.collectAsState(initial = emptyList())

        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.push(CreateUpdateQuestionScreen(quizId, context = getContext()))
                    }
                ) {
                    Text("+", fontSize = 24.sp)
                }
            }
        ) {
            if (questions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No questions available.", fontSize = 20.sp)
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Questions", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    questions.forEach { question ->
                        QuestionItem(
                            question = question,
                            onClick = {
                                navigator.push(CreateUpdateQuestionScreen(quizId = quizId, question.id, getContext()))
                                log(quizId, "sse")
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun QuestionItem(question: Question, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke() },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = question.text, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Type: TODO", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

