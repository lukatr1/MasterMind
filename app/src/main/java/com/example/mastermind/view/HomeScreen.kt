package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.utils.SharedPreferencesHelper
import com.example.mastermind.viewModel.SeeQuizzesScreenViewModel
import androidx.compose.material.*


class HomeScreen(private var context: Context) : Screen {
    private fun getContext () : Context {
        return context
    }
    @Composable
    override fun Content() {
        val seeQuizzesViewModel = SeeQuizzesScreenViewModel(getContext())
        val navigator = LocalNavigator.current
        SharedPreferencesHelper.getUsername(getContext())
            ?.let { Text(text = "Welcome " + it + "!", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp)) }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PoleButton(text = "See Quizzes", color = Color.hsl(42f, 1f, 0.5f), onClick = {
                    seeQuizzesViewModel.updateQuizzes()
                    navigator?.push(SeeQuizzesScreen(getContext()))
                })
                PoleButton(text = "Create Quiz", color =Color.hsl(13F, 1F, 0.5F) , onClick = {
                    navigator?.push(CreateQuizScreen(getContext()))
                })
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PoleButton(text = "Bookmarked", color = Color.hsl(165F, 1F, 0.5F), onClick = {
                    navigator?.push(BookmarkedScreen(getContext()))
                })
                PoleButton(text = "About", color = Color.hsl(82F, 1F, 0.5F), onClick = {
                navigator?.push(AboutAppScreen(getContext()))
                })
            }
            Spacer(modifier = Modifier
                .height(15.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .height(8.dp)
            )

            FloatingActionButton(onClick = {
                SharedPreferencesHelper.clearUsername(getContext())
                navigator?.popAll()
                navigator?.replace(StartingScreen(getContext()))
            }) {
                Text("Logout")
            }
            }

    }
}

@Composable
fun PoleButton(text: String, color: Color, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
        containerColor = color,
        ),
        modifier = Modifier
            .size(125.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


