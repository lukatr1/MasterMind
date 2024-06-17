package com.example.mastermind.view

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
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
                    navigator?.push(SeeQuizzesScreen())
                })
                PoleButton(text = "Create Quiz", color =Color.hsl(13F, 1F, 0.5F) , onClick = {
                    navigator?.push(CreateQuizScreen())
                })
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PoleButton(text = "PlaceHolder", color = Color.hsl(165F, 1F, 0.5F), onClick = {
                })
                PoleButton(text = "About", color = Color.hsl(82F, 1F, 0.5F), onClick = {
                    navigator?.push(AboutAppScreen())
                })
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


