package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.data.models.Quiz
import com.example.mastermind.data.models.log
import com.example.mastermind.utils.SharedPreferencesHelper
import com.example.mastermind.viewModel.SeeQuizzesScreenViewModel
import android.widget.Toast


class SeeQuizzesScreen(private var context: Context) : Screen {
    private fun getContext(): Context {
        return context
    }

    @Composable
    override fun Content() {
        val navigation = LocalNavigator.current
        val viewModel = SeeQuizzesScreenViewModel(getContext())
        val quizzes by viewModel.quizzes.observeAsState(initial = emptyList())
        val searchText = remember { mutableStateOf("") }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var quizToDelete by remember { mutableStateOf<Quiz?>(null) }
        var showEditDialog by remember { mutableStateOf(false) }
        var quizToEdit by remember { mutableStateOf<Quiz?>(null) }
        var editQuizName by remember { mutableStateOf("") }
        var myQuizzes by remember { mutableStateOf(false) }



        // Fetch quizzes when the screen is first created
        LaunchedEffect(Unit) {
            viewModel.getAllQuizzes()
            log("All Quizzes: $quizzes")
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
                placeholder = { Text("Search quizzes by name...") }
            )

            Row {
                Button(onClick = {
                    myQuizzes = true
                    navigation?.replace(SeeQuizzesScreen(getContext()))

                }) {
                    Text(text = "My Quizzes")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    myQuizzes = false
                    navigation?.replace(SeeQuizzesScreen(getContext()))
                }) {
                    Text(text = "All Quizzes")

                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Display filtered list of quizzes
                if (quizzes.isEmpty()) {
                    Text(text = "No quizzes available.")
                } else {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val filteredQuizzes = quizzes.filter {
                            it.name.contains(searchText.value, ignoreCase = true) && ( !myQuizzes || (!SharedPreferencesHelper.isLoggedIn(getContext() ) && it.createdBy == "Guest")
                                    || (myQuizzes && it.createdBy == SharedPreferencesHelper.getUsername(getContext())))

                        }
                        items(filteredQuizzes) { quiz ->
                            QuizItem(
                                quiz = quiz,
                                onClick = {
                                    if (quiz.questions.isEmpty()) {
                                        Toast.makeText(getContext(), "This quiz has no questions", Toast.LENGTH_SHORT).show()
                                    } else {
                                        navigation?.push(TakeQuizScreen(quiz.id, getContext()))
                                    }
                                },
                                onDelete = {
                                    quizToDelete = quiz
                                    showDeleteDialog = true
                                },
                                onEdit = {
                                    navigation?.push(QuestionsOverviewScreen(quiz.id, getContext()))
                                },
                                onBookmarkButtonPressed = {
                                    // toggle bookmark status and update the UI
                                    navigation?.replace(SeeQuizzesScreen(getContext()))
                                    val newState = !quiz.isBookmarked
                                    quiz.isBookmarked = newState
                                    if (newState) {
                                        viewModel.bookmarkQuiz(quiz)
                                    } else {
                                        viewModel.unbookmarkQuiz(quiz)
                                    }
                                },
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navigation?.replace(BookmarkedScreen(getContext())) }) {
                    Text(text = "Go to Favorites")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navigation?.pop() }) {
                    Text(text = "Go back to HomeScreen")
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Quiz") },
                text = { Text("Are you sure you want to delete this quiz?") },
                confirmButton = {
                    Button(onClick = {
                        quizToDelete?.let { viewModel.deleteQuiz(it.id) }
                        showDeleteDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("No")
                    }
                }
            )
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Quiz") },
                text = {
                    Column {
                        Text("Enter new name for the quiz:")
                        TextField(
                            value = editQuizName,
                            onValueChange = { editQuizName = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        quizToEdit?.let { viewModel.editQuiz(it.id, editQuizName) }
                        showEditDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showEditDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    @Composable
    private fun QuizItem(
        quiz: Quiz,
        onClick: () -> Unit,
        onDelete: () -> Unit,
        onEdit: () -> Unit,
        onBookmarkButtonPressed: () -> Unit
    ) {
        //val navigator = LocalNavigator.current
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = quiz.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,

                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Created by: ${quiz.createdBy}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                    )

                    
                }

                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Quiz",
                        tint = Color.Blue
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Quiz",
                        tint = Color.Red
                    )
                }

                val bookmarkIcon = if (quiz.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                val bookmarkTint = if (quiz.isBookmarked) Color.Red else Color.Gray
                val navigator = LocalNavigator.current

                IconButton(onClick = onBookmarkButtonPressed,

                ) {
                    Icon(
                        imageVector = bookmarkIcon,
                        contentDescription = "Bookmark Quiz",
                        tint = bookmarkTint
                    )
                }
            }
        }
    }
}
