package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator


class StartingScreen(private var context: Context) : Screen {
    private fun getContext () : Context {
        return context
    }
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {navigator?.push(HomeScreen(getContext())) }) {
                Text(text = "This is the StartingScreen")
            }
        }

    }
}
