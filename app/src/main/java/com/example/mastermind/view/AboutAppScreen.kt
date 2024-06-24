package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.data.GetQuizRepoProvider
import com.example.mastermind.viewModel.SeeQuizzesScreenViewModel


class AboutAppScreen(private var context: Context) : Screen {
    private fun getContext () : Context {
        return context
    }
    @Composable
    override fun Content() {
        val quizRepoProvider = GetQuizRepoProvider()
        val quizRepo = quizRepoProvider.getsInstance(getContext())
        val seeScreenViewModel: SeeQuizzesScreenViewModel = viewModel()
        val navigator = LocalNavigator.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navigator?.pop() }) {
                    Text(text = "Go back to HomeScreen")
                }
            }
        }
    }
}