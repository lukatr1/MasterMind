package com.example.mastermind.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.data.models.Question
import com.example.mastermind.viewModel.CreateQuestionViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mastermind.data.models.QuestionMultipleChoice
import com.example.mastermind.data.models.QuestionTrueFalse
import com.example.mastermind.data.models.log


class CreateUpdateQuestionScreen(
    private val quizId: Int,
    private val questionId: Int? = null,
    private val context: Context
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = remember { CreateQuestionViewModel(context, quizId) }

        // State for question data
        var questionText by remember { mutableStateOf(TextFieldValue("")) }
        var trueChoices by remember { mutableStateOf(TextFieldValue("")) }
        var falseChoices by remember { mutableStateOf(TextFieldValue("")) }
        var trueFalseAnswer by remember { mutableStateOf(true) }
        var questionType by remember { mutableStateOf<QuestionType?>(null) }
        var showError by remember { mutableStateOf(false) }
        var question by remember { mutableStateOf<Question?>(null) }

        // Load question if editing
        LaunchedEffect(questionId) {
            if (questionId != null) {
                viewModel.getQuestionById(questionId)?.let { loadedQuestion ->
                    question = loadedQuestion
                    questionText = TextFieldValue(loadedQuestion.text)
                    questionType = when (loadedQuestion.questionType) {
                        "MultipleChoice" -> QuestionType.MultipleChoice
                        "TrueFalse" -> QuestionType.TrueFalse
                        else -> QuestionType.MultipleChoice
                    }
                    when (questionType) {
                        QuestionType.MultipleChoice -> {
                            val multipleChoiceQuestion = loadedQuestion as QuestionMultipleChoice
                            trueChoices = TextFieldValue(multipleChoiceQuestion.choicesTrue.joinToString(", "))
                            falseChoices = TextFieldValue(multipleChoiceQuestion.choicesFalse.joinToString(", "))
                        }
                        QuestionType.TrueFalse -> {
                            val trueFalseQuestion = loadedQuestion as QuestionTrueFalse
                            trueFalseAnswer = trueFalseQuestion.answer
                        }

                        else -> {navigator?.pop()}
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (questionId == null) "Add Question" else "Edit Question",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
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
                        onValueChange = {
                            questionText = it
                            if (it.text.isNotEmpty()) {
                                showError = false
                            }
                        },
                        label = { Text("Question Text") },
                        isError = showError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showError) {
                        Text(
                            text = "Question text cannot be empty.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
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
                    Column(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround
                        ) {
                        Button(onClick = {
                            if (questionText.text.isNotEmpty()) {
                                if (questionId == null) {
                                    when (type) {
                                        QuestionType.MultipleChoice -> {
                                            viewModel.createMultipleChoiceQuestion(
                                                quizId,
                                                trueChoices.text.split(",").map { it.trim() },
                                                falseChoices.text.split(",").map { it.trim() },
                                                questionText.text
                                            ) {
                                                navigator?.pop()
                                            }
                                        }
                                        QuestionType.TrueFalse -> {
                                            viewModel.createTrueFalseQuestion(
                                                quizId,
                                                trueFalseAnswer,
                                                questionText.text
                                            ) {
                                                navigator?.pop()
                                            }
                                        }
                                    }
                                } else {
                                    viewModel.updateQuestion(
                                        quizId,
                                        questionId,
                                        questionText.text
                                    ) {
                                        navigator?.pop()
                                    }
                                }
                            } else {
                                showError = true
                            }
                        }) {
                            Text(if (questionId == null) "Save Question" else "Update Question")
                        }
                        if (questionId == null) {
                            Button(onClick = {
                                if (questionText.text.isNotEmpty()) {
                                    when (type) {
                                        QuestionType.MultipleChoice -> {
                                            viewModel.createMultipleChoiceQuestion(
                                                quizId,
                                                trueChoices.text.split(",").map { it.trim() },
                                                falseChoices.text.split(",").map { it.trim() },
                                                questionText.text
                                            ) {
                                                questionText = TextFieldValue("")
                                                trueChoices = TextFieldValue("")
                                                falseChoices = TextFieldValue("")
                                                trueFalseAnswer = true
                                                questionType = null
                                            }
                                        }
                                        QuestionType.TrueFalse -> {
                                            viewModel.createTrueFalseQuestion(
                                                quizId,
                                                trueFalseAnswer,
                                                questionText.text
                                            ) {
                                                questionText = TextFieldValue("")
                                                trueChoices = TextFieldValue("")
                                                falseChoices = TextFieldValue("")
                                                trueFalseAnswer = true
                                                questionType = null
                                            }
                                        }
                                    }
                                } else {
                                    showError = true
                                }
                            }) {
                                Text("Save & Add Another")
                            }
                        }}
                        Button(onClick = {
                            navigator?.pop()
                        }) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

