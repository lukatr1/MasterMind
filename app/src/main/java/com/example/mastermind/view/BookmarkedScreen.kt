package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.data.models.Quiz
import com.example.mastermind.data.models.log
import com.example.mastermind.viewModel.BookmarkedScreenViewModel
import com.example.mastermind.viewModel.SeeQuizzesScreenViewModel

/*class BookmarkedScreen(private var context: Context) : Screen {
    private fun getContext () : Context {
        return context
    }

    @Composable
    override fun Content() {
        val navigation = LocalNavigator.current
        val viewModel: BookmarkedScreenViewModel = viewModel()
        val bookmarkedQuizzes by viewModel.bookmarkedQuizzes.observeAsState(initial = emptyList())
        val searchText = remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            viewModel.getBookmarkedQuizzes()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = searchText.value,
                onValueChange = { searchText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search bookmarked quizzes by name...") }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (bookmarkedQuizzes.isEmpty()) {
                    Text(text = "No bookmarked quizzes available.")
                } else {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val filteredQuizzes = bookmarkedQuizzes.filter {
                            it.name.contains(searchText.value, ignoreCase = true)
                        }
                        items(filteredQuizzes) { quiz ->
                            QuizItem(
                                quiz = quiz,
                                onClick = { navigation?.push(TakeQuizScreen(quiz.id, getContext())) },
                                viewModelScope.launch {
                                    onUnbookmark = {
                                        viewModel.unbookmarkQuiz(quiz)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navigation?.pop() }) {
                    Text(text = "Go back to HomeScreen")
                }
            }
        }
    }

    @Composable
    private fun QuizItem(quiz: Quiz, onClick: () -> Unit, onUnbookmark: () -> Unit) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = quiz.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onUnbookmark) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Unbookmark Quiz",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}*/