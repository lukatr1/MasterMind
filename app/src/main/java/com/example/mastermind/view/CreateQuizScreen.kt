package com.example.mastermind.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.viewModel.CreateQuizScreenViewModel
class CreateQuizScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: CreateQuizScreenViewModel = viewModel()

        var quizName by remember { mutableStateOf(TextFieldValue("")) }
        var questionType by remember { mutableStateOf<QuestionType?>(null) }
        var questionText by remember { mutableStateOf(TextFieldValue("")) }
        var trueChoices by remember { mutableStateOf(TextFieldValue("")) }
        var falseChoices by remember { mutableStateOf(TextFieldValue("")) }
        var trueFalseAnswer by remember { mutableStateOf(true) }
        var quizId by remember { mutableIntStateOf(-1) }
        var showError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (quizId == -1) {
                Text(
                    text = "Create a New Quiz",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = quizName,
                    onValueChange = {
                        quizName = it
                        showError = false
                    },
                    label = { Text("Quiz Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError
                )
                if (showError) {
                    Text(
                        text = "Quiz Name shouldn't be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (quizName.text.isBlank()) {
                        showError = true
                    } else {
                        quizId = viewModel.createQuiz(quizName.text)
                    }
                }) {
                    Text(text = "Continue")
                }
            } else {
                Text(
                    text = "Add Questions to ${quizName.text}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { questionType = QuestionType.MultipleChoice }) {
                        Text(text = "Multiple Choice")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { questionType = QuestionType.TrueFalse }) {
                        Text(text = "True/False")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                questionType?.let { type ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = questionText,
                            onValueChange = { questionText = it },
                            label = { Text("Question Text") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        when (type) {
                            QuestionType.MultipleChoice -> {
                                OutlinedTextField(
                                    value = trueChoices,
                                    onValueChange = { trueChoices = it },
                                    label = { Text("Correct Answers (comma separated)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = falseChoices,
                                    onValueChange = { falseChoices = it },
                                    label = { Text("Incorrect Answers (comma separated)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            QuestionType.TrueFalse -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = trueFalseAnswer,
                                        onClick = { trueFalseAnswer = true }
                                    )
                                    Text("True")
                                    Spacer(modifier = Modifier.width(16.dp))
                                    RadioButton(
                                        selected = !trueFalseAnswer,
                                        onClick = { trueFalseAnswer = false }
                                    )
                                    Text("False")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            when (type) {
                                QuestionType.MultipleChoice -> viewModel.createMultipleChoiceQuestion(
                                    quizId,
                                    trueChoices.text.split(",").map { it.trim() },
                                    falseChoices.text.split(",").map { it.trim() },
                                    questionText.text
                                )
                                QuestionType.TrueFalse -> viewModel.createTrueFalseQuestion(
                                    quizId,
                                    trueFalseAnswer,
                                    questionText.text
                                )
                            }
                            questionText = TextFieldValue("")
                            trueChoices = TextFieldValue("")
                            falseChoices = TextFieldValue("")
                            trueFalseAnswer = true
                            questionType = null
                        }) {
                            Text("Add Question")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navigator?.pop() }) {
                            Text(text = "Finish")
                        }
                    }
                }
            }
        }
    }
}

enum class QuestionType {
    MultipleChoice, TrueFalse
}