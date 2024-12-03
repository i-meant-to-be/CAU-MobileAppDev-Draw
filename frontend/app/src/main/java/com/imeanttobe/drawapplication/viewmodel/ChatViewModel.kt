package com.imeanttobe.drawapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.imeanttobe.drawapplication.data.model.ChatSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Variables
    private val referenceName = "user_data"
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _sessions = MutableStateFlow<List<ChatSession>>(emptyList()) // This is for chat lists received from firebase

    // Getter
    val sessions = _sessions.asStateFlow() // This is for chat lists received from firebase

    // Initializer
    init {
        getSessions()
    }

    // Methods
    private fun getSessions() {
        firebaseAuth.currentUser?.let { user ->
            firebaseDatabase
                .getReference(referenceName)
                .child(user.uid)
                .child("sessions")
                .get()
                .addOnSuccessListener { task ->
                    val sessions = mutableListOf<ChatSession>()
                    
                    for (snapshot in task.children) {
                        val session = ChatSession(
                            id = snapshot.key!!,
                            user1Id = snapshot.getValue(String::class.java)!!,
                            user2Id = snapshot.getValue(String::class.java)!!,
                            lastMessage = snapshot.getValue(String::class.java)!!
                        )
                    }
                }
        }
    }
}