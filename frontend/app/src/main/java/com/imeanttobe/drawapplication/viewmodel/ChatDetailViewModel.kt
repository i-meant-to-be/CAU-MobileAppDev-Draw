package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : ViewModel() {
    // Values
    private val _currentMessage = mutableStateOf("")

    // Getter
    val currentMessage: State<String> = _currentMessage

    // Setter
    fun setCurrentMessage(newValue: String) {
        _currentMessage.value = newValue
    }
}