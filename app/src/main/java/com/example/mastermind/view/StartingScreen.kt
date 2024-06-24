package com.example.mastermind.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.mastermind.utils.SharedPreferencesHelper
import com.example.mastermind.view.HomeScreen

class StartingScreen(private val context: Context) : Screen {

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val (username, setUsername) = remember { mutableStateOf("") }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { setUsername(it) },
                    label = { Text("Enter Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (username.isNotBlank()) {
                            SharedPreferencesHelper.saveUsername(context, username)
                            navigator?.replace(HomeScreen(context))
                        }
                    },
                    enabled = username.isNotBlank(),
                    modifier = Modifier.padding(top = 16.dp)  // Adding padding for spacing
                ) {
                    Text(text = "Login")
                }

                Button(
                    onClick = {
                        navigator?.replace(HomeScreen(context)) },
                    modifier = Modifier.padding(top = 16.dp)  // Adding padding for spacing
                ) {
                    Text(text = "Guest Mode")
                }
            }
        }
    }
}
