package com.example.mastermind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.mastermind.ui.theme.MasterMindTheme
import com.example.mastermind.view.StartingScreen
import java.lang.Appendable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterMindTheme {
                App(context = this)
            }
        }
    }
}


@Composable
fun App(context: Context) {
    MasterMindTheme {
        Surface {
            Navigator(StartingScreen(context))
        }
        //specified start destination
    }
}

