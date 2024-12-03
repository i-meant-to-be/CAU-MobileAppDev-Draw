package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.imeanttobe.drawapplication.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : ViewModel() {
    private operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Values
    private val referenceName = "message"
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _messageTextField = mutableStateOf("")
    private val _opponentNickname = mutableStateOf("채팅 상대 닉네임")
    private val _drawerState = mutableStateOf(false)
    private val _messages = MutableStateFlow<List<Message>>(emptyList()) // This is for messages received from firebase

    // Getter
    val messageTextField: State<String> = _messageTextField
    val opponentNickname: State<String> = _opponentNickname
    val drawerState: State<Boolean> = _drawerState
    val messages: StateFlow<List<Message>> = _messages.asStateFlow() // This is for messages received from firebase

    init {
        // TODO: this is for sample messages and have to replaced or removed when firebase is applied
        viewModelScope.launch {
            _messages.emit(
                listOf(
                    Message(
                        id = "",
                        senderId = "",
                        chatSessionId = "",
                        body = "message"
                    ),
                ) * 20
            )
        }
    }

    // Methods
    fun setTextFieldMessage(newValue: String) {
        _messageTextField.value = newValue
    }

    fun setDrawerState(newValue: Boolean) {
        _drawerState.value = newValue
    }

    fun send(sessionId: String) {
        val message = Message(
            id = firebaseDatabase.reference.child(referenceName).push().key!!,
            senderId = firebaseAuth.currentUser!!.uid,
            chatSessionId = sessionId,
            body = _messageTextField.value
        )

        firebaseDatabase.reference.child(referenceName).child(sessionId).push().setValue(message)
    }

    fun listen(
        sessionId: String,
        onError: () -> Unit
    ) {
        firebaseDatabase.reference
            .child(referenceName)
            .child(sessionId)
            .orderByChild("timestamp")
            .addValueEventListener(
                object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<Message>()
                        snapshot.children.forEach { message ->
                            val message = message.getValue(Message::class.java)
                            message?.let { list.add(it) }
                        }

                        _messages.value = list
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onError()
                    }

                }
            )
    }
}