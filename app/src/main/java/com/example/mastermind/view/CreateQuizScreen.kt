package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.viewModel.CreateQuizScreenViewModel

import kotlinx.coroutines.launch
class CreateQuizScreen(private var context: Context) : Screen {
    private fun getContext(): Context {
        return context
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = CreateQuizScreenViewModel(getContext())
        val coroutineScope = rememberCoroutineScope()

        var quizName by remember { mutableStateOf(TextFieldValue("")) }
        var quizId by remember { mutableIntStateOf(-1) }
        var showError by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                        coroutineScope.launch {
                            quizId = viewModel.createQuiz(quizName.text)
                        }
                        navigator?.pop()
                    }
                }) {
                    Text(text = "Create Quiz")
                }
        }
    }
}

enum class QuestionType {
    MultipleChoice, TrueFalse
}