package com.example.mastermind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
                App()
            }
        }
    }
}


@Composable
fun App() {
    MasterMindTheme {
        //specified start destination
        Navigator(StartingScreen())
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MasterMindTheme {
        App()
    }
}