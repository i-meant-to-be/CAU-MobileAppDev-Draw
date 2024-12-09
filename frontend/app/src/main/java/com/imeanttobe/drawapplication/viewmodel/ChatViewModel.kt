package com.imeanttobe.drawapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
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
    private val messageReferenceName = "message"

    private val _user = MutableStateFlow<User?>(null)
    private val _loadingState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _sessionAndUser = MutableStateFlow<MutableList<Pair<ChatSession, User>>>(mutableListOf()) // This is for chat lists received from firebase

    // Getter
    val loadingState = _loadingState.asStateFlow()
    val sessionAndUser = _sessionAndUser.asStateFlow() // This is for chat lists received from firebase

    // Initializer
    init {
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { task ->
                val userWrapper = task.getValue(UserWrapper::class.java)
                if (userWrapper != null) {
                    _user.value = User(userWrapper)
                    updateSessionAndUser(isFirst = true)
                    listen()
                }
            }
    }

    // Methods
    private fun updateUserData() {
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { task ->
                val userWrapper = task.getValue(UserWrapper::class.java)
                if (userWrapper != null) {
                    _user.value = User(userWrapper)
                    updateSessionAndUser()
                }
            }
    }

    private fun listen() {
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(_user.value!!.id)
            .child("chatSessions")
            .addChildEventListener(
                object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        updateUserData()
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        updateUserData()
                    }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {                        }
                    override fun onCancelled(error: DatabaseError) {}
                }
            )

        for (sessionId in _user.value!!.chatSessions) {
            FirebaseDatabase.getInstance()
                .getReference(messageReferenceName)
                .child(sessionId)
                .addChildEventListener(
                    object : ChildEventListener {
                        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                            updateUserData()
                        }
                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                        override fun onChildRemoved(snapshot: DataSnapshot) {}
                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {                        }
                        override fun onCancelled(error: DatabaseError) {}
                    }
                )
        }
    }

    private fun updateSessionAndUser(
        isFirst: Boolean = false
    ) {
        val newSessionAndUser = mutableListOf<Pair<ChatSession, User>>()
        _sessionAndUser.value.clear()
        if (isFirst) _loadingState.value = Resource.Loading()

        viewModelScope.launch {
            for (sessionId in _user.value!!.chatSessions) {
                try {
                    // Step 1: Fetch session from Realtime Database
                    val session = fetchSession(
                        sessionsRef = FirebaseDatabase.getInstance().getReference(chatReferenceName),
                        sessionId = sessionId
                    )
                    if (session != null && session.closed == false) {
                        // Step 2: Fetch user from Realtime Database using the session's userId
                        val opponentId = if (session.user1Id == _user.value!!.id) session.user2Id else session.user1Id
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

            _sessionAndUser.value = newSessionAndUser
            _loadingState.value = Resource.Success()
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