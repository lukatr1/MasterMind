package com.example.mastermind.data.models

import android.util.Log
fun log(content: Any, optional : String = "Debugger") {
    Log.d(optional, content.toString())
}