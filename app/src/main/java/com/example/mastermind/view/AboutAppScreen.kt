package com.example.mastermind.view

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class AboutAppScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Occupy remaining space
                contentAlignment = Alignment.Center
            ) {
                Text(text = "This is the AboutAppScreen")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Occupy remaining space
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navigator?.pop() }) {
                    Text(text = "Go back to HomeScreen")
                }
            }
        }
    }
}