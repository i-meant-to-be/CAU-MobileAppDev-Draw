package com.imeanttobe.drawapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.etc.UserWrapper
import com.imeanttobe.drawapplication.data.model.ChatSession
import com.imeanttobe.drawapplication.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Variables
    private val userReferenceName = "user"
    private val chatReferenceName = "chat_session"

    private val _loadingState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _sessionAndUser = MutableStateFlow<MutableList<Pair<ChatSession, User>>>(mutableListOf()) // This is for chat lists received from firebase

    // Getter
    val loadingState = _loadingState.asStateFlow()
    val sessionAndUser = _sessionAndUser.asStateFlow() // This is for chat lists received from firebase

    // Initializer
    init {
        getSessions()
    }

    // Methods
    private fun getSessions() {
        val newSessionAndUser = mutableListOf<Pair<ChatSession, User>>()
        _loadingState.value = Resource.Loading()

        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { task ->
                val userWrapper = task.getValue(UserWrapper::class.java)
                if (userWrapper != null) {
                    val user = User(userWrapper)

                    viewModelScope.launch {
                        for (sessionId in user.chatSessions) {
                            try {
                                // Step 1: Fetch session from Realtime Database
                                val session = fetchSession(
                                    sessionsRef = FirebaseDatabase.getInstance().getReference(chatReferenceName),
                                    sessionId = sessionId
                                )
                                if (session != null) {
                                    // Step 2: Fetch user from Realtime Database using the session's userId
                                    val opponentId = if (session.user1Id == user.id) session.user2Id else session.user1Id
                                    val user = fetchUser(
                                        usersRef = FirebaseDatabase.getInstance().getReference(userReferenceName),
                                        userId = opponentId
                                    )
                                    if (user != null) {
                                        // Step 3: Pair session and user
                                        newSessionAndUser.add(session to user)
                                    }
                                }
                            } catch (e: Exception) {
                                // Handle exceptions (e.g., document not found, network errors)
                                println("Error fetching data for session $sessionId: ${e.message}")
                            }
                        }

                        _sessionAndUser.emit(newSessionAndUser)
                        _loadingState.value = Resource.Success()
                    }
                }
            }
    }

    // Helper function to fetch a session
    private suspend fun fetchSession(sessionsRef: DatabaseReference, sessionId: String): ChatSession? =
        suspendCancellableCoroutine { cont ->
            sessionsRef.child(sessionId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val session = snapshot.getValue(ChatSession::class.java)
                    cont.resumeWith(Result.success(session))
                }

                override fun onCancelled(error: DatabaseError) {
                    cont.resumeWith(Result.success(null))
                    _loadingState.value = Resource.Error("Error fetching session data")
                }
            })
        }

    // Helper function to fetch a user
    private suspend fun fetchUser(usersRef: DatabaseReference, userId: String): User? =
        suspendCancellableCoroutine { cont ->
            usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userWrapper = snapshot.getValue(UserWrapper::class.java)
                    if (userWrapper != null) {
                        cont.resumeWith(Result.success(User(userWrapper)))
                    } else {
                        cont.resumeWith(Result.success(null))
                        _loadingState.value = Resource.Error("Error fetching user data")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cont.resumeWith(Result.success(null))
                    _loadingState.value = Resource.Error("Error fetching user data")
                }
            })
        }
}