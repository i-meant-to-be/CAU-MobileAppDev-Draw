package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imeanttobe.drawapplication.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : ViewModel() {
    private operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Values
    // TODO: have to get firebase instances here
    private val _textFieldMessage = mutableStateOf("")
    private val _opponentNickname = mutableStateOf("채팅 상대 닉네임")
    private val _drawerState = mutableStateOf(false)
    private val _messages = MutableStateFlow<List<Message>>(emptyList()) // This is for messages received from firebase

    // Getter
    val textFieldMessage: State<String> = _textFieldMessage
    val opponentNickname: State<String> = _opponentNickname
    val drawerState: State<Boolean> = _drawerState
    val messages: StateFlow<List<Message>> = _messages.asStateFlow() // This is for messages received from firebase

    init {
        // TODO: this is for sample messages and have to replaced or removed when firebase is applied
        viewModelScope.launch {
            _messages.emit(
                listOf(
                    Message(
                        senderId = "",
                        chatSessionId = "",
                        body = "message",
                        datetime = LocalDateTime.now()
                    ),
                ) * 20
            )
        }
    }

    // Setter
    fun setTextFieldMessage(newValue: String) {
        _textFieldMessage.value = newValue
    }
    fun setOpponentNickname(newValue: String) {
        _opponentNickname.value = newValue
    }
    fun setDrawerState(newValue: Boolean) {
        _drawerState.value = newValue
    }

    // Methods
    fun send() {
        // TODO: have to implemented
    }
}