package com.example.mastermind.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "MasterMind"
    }
    val text: LiveData<String> = _text

}