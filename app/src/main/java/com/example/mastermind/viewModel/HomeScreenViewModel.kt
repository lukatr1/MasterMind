package com.example.mastermind.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel(context: Context) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "MasterMind"
    }
    val text: LiveData<String> = _text

}