package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    // Values
    private val _username = mutableStateOf("")

    // Getter
    val username: State<String> = _username

    // Setter
    fun setUsername(value: String) {
        _username.value = value
    }
}