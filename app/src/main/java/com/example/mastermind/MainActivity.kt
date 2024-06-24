package com.example.mastermind

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.mastermind.ui.theme.MasterMindTheme
import com.example.mastermind.utils.SharedPreferencesHelper
import com.example.mastermind.view.HomeScreen
import com.example.mastermind.view.StartingScreen

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
            if(SharedPreferencesHelper.isLoggedIn(context)){
            Navigator(HomeScreen(context))}
            else{

                Navigator(StartingScreen(context))}
        }
    }
}