package com.imeanttobe.drawapplication.viewmodel

import androidx.lifecycle.ViewModel
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
    private val referenceName = "session"
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _sessions = MutableStateFlow<List<ChatSession>>(emptyList()) // This is for chat lists received from firebase

    // Getter
    val sessions = _sessions.asStateFlow() // This is for chat lists received from firebase

    // Methods
    fun getSessions() {
        firebaseDatabase
            .getReference(referenceName)
            .get()
    }
}