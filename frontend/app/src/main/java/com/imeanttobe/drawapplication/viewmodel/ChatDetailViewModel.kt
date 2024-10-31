package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : ViewModel() {
    // Values
    private val _message = mutableStateOf("")
    private val _opponentNickname = mutableStateOf("채팅 상대 닉네임")

    // Getter
    val message: State<String> = _message
    val opponentNickname: State<String> = _opponentNickname

    // Setter
    fun setMessage(newValue: String) {
        _message.value = newValue
    }
    fun setOpponentNickname(newValue: String) {
        _opponentNickname.value = newValue
    }

    // Methods
    fun send() {

    }
}