package com.imeanttobe.drawapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.imeanttobe.drawapplication.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : ViewModel() {
    private operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Values
    private val messageReferenceName = "message"
    private val userReferenceName = "user"
    private val chatReferenceName = "chat_session"

    private val _currentUserId = mutableStateOf<String>("")
    private val _messageTextField = mutableStateOf("")
    private val _drawerState = mutableStateOf(false)
    private val _messages = MutableStateFlow<List<Message>>(emptyList()) // This is for messages received from firebase

    // Getter
    val currentUserId: State<String> = _currentUserId
    val messageTextField: State<String> = _messageTextField
    val drawerState: State<Boolean> = _drawerState
    val messages: StateFlow<List<Message>> = _messages.asStateFlow() // This is for messages received from firebase

    // Methods
    // This method sets the text field's string values
    fun setTextFieldMessage(newValue: String) {
        _messageTextField.value = newValue
    }

    fun loadUserId() {
        _currentUserId.value = FirebaseAuth.getInstance().currentUser!!.uid
    }

    // This method sets drawer's state; whether drawer is opened or not
    fun setDrawerState(newValue: Boolean) {
        _drawerState.value = newValue
    }

    // This method sends message to Firebase Database
    // (NOTE: This method is almost same as the method we implemented on eClass)
    fun send(sessionId: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val messageId = FirebaseDatabase.getInstance().getReference(messageReferenceName).child(sessionId).push().key!!

        val message = Message(
            id = messageId,
            senderId = userId,
            chatSessionId = sessionId,
            body = _messageTextField.value
        )

        FirebaseDatabase.getInstance()
            .getReference(messageReferenceName)
            .child(sessionId)
            .child(messageId)
            .setValue(message)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance()
                    .getReference(chatReferenceName)
                    .child(sessionId)
                    .child("lastMessage")
                    .setValue(message.body)
            }
    }

    // This method listens messages
    // (NOTE: This method is almost same as the method we implemented on eClass)
    fun listen(
        sessionId: String,
        onError: () -> Unit
    ) {
        FirebaseDatabase.getInstance()
            .getReference(messageReferenceName)
            .child(sessionId)
            .orderByChild("timestamp")
            .addValueEventListener(
                object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<Message>()

                        snapshot.children.forEach { data ->
                            val message = data.getValue(Message::class.java)
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

    fun exit(sessionId: String) {
        FirebaseDatabase.getInstance()
            .getReference(chatReferenceName)
            .child(sessionId)
            .child("closed")
            .setValue(true)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance()
                    .getReference(userReferenceName)
                    .child(_currentUserId.value)
                    .child("chatSessions")
                    .orderByValue()
                    .equalTo(sessionId)
                    .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (childSnapshot in snapshot.children) {
                                    // 해당 노드를 삭제
                                    childSnapshot.ref.removeValue()
                                        .addOnSuccessListener {
                                            Log.d("ProfileViewModel", "Post successfully removed from user's postIds.")
                                        }
                                        .addOnFailureListener { error ->
                                            Log.d("ProfileViewModel", "Error deleting post in user: ${error.message}")
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("ProfileViewModel", "Error reading from database: ${error.message}")
                            }
                        }
                    )
            }
    }
}