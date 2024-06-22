package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.data.models.Question
import com.example.mastermind.data.models.QuestionMultipleChoice
import com.example.mastermind.data.models.QuestionTrueFalse
import com.example.mastermind.viewModel.EditQuizScreenViewModel

class EditQuizScreen(private val quizId: Int, private var context: Context) : Screen {
    private fun getContext () : Context {
        return context
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: EditQuizScreenViewModel = viewModel()
        val quiz by viewModel.quiz.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadQuiz(quizId)
        }

        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            quiz?.let { quiz ->
                Text("Edit Quiz: ${quiz.name}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))

                quiz.questions.forEach { question ->
                    when (question) {
                        is QuestionMultipleChoice -> EditMultipleChoiceQuestion(question, viewModel::updateQuestion)
                        is QuestionTrueFalse -> EditTrueFalseQuestion(question, viewModel::updateQuestion)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(onClick = {
                    viewModel.saveChanges()
                    navigator?.pop()
                }) {
                    Text("Save")
                }
            } ?: Text("Loading...")
        }
    }
}

@Composable
fun EditMultipleChoiceQuestion(question: QuestionMultipleChoice, updateQuestion: (Question) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue(question.text)) }

    Column {
        Text("Multiple Choice Question")
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                updateQuestion(question.copy(text = newText.text))
            },
            label = { Text("Question Text") }
        )
    }
}

@Composable
fun EditTrueFalseQuestion(question: QuestionTrueFalse, updateQuestion: (Question) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue(question.text)) }

    Column {
        Text("True/False Question")
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                updateQuestion(question.copy(text = newText.text))
            },
            label = { Text("Question Text") }
        )
    }
}