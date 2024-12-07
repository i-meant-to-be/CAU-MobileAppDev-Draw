package com.imeanttobe.drawapplication.viewmodel


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.imeanttobe.drawapplication.data.etc.PostWrapper
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.etc.UserWrapper
import com.imeanttobe.drawapplication.data.model.ChatSession
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val userReferenceName = "user"
    private val postReferenceName = "post"
    private val chatReferenceName = "chat_session"
    private val msgReferenceName = "message"

    private val _creatingChatSessionState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _user = MutableStateFlow<User?>(null)
    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    private val _dialogState = mutableIntStateOf(0)
    private val _currentPictureDescription = mutableStateOf("")
    private val _currentPictureUri = mutableStateOf(Uri.EMPTY)

    // Getter
    val creatingChatSessionState = _creatingChatSessionState.asStateFlow()
    val user = _user.asStateFlow()
    val userPosts = _userPosts.asStateFlow()
    val dialogState: State<Int> = _dialogState
    val currentPictureDescription: State<String> = _currentPictureDescription
    val currentPictureUri: State<Uri> = _currentPictureUri


    fun setDialogState(newValue: Int) {
        _dialogState.intValue = newValue
    }

    fun setPictureDialogData(
        uri: Uri,
        description: String
    ) {
        _currentPictureUri.value = uri
        _currentPictureDescription.value = description
    }

    fun setUserProfileData(userId: String?) {
        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference(userReferenceName)
            val userRef = usersRef.child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userdata = User(snapshot.getValue(UserWrapper::class.java)as UserWrapper)
                    _user.value = userdata
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserProfileViewModel", "Failed to get user data: ${error.message}")
                }
            })
        }
    }

    fun setUserPostData(userId: String?) {
            // Get user's posts
        if (userId != null) {
            FirebaseDatabase.getInstance()
                .getReference(userReferenceName)
                .child(userId)
                .child("postIds")
                .orderByChild("timestamp")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Clear before load new data
                            _userPosts.value = emptyList()

                            for (data in snapshot.children) {
                                val postId = data.getValue(String::class.java)
                                postId?.let { postId ->
                                    FirebaseDatabase.getInstance()
                                        .getReference(postReferenceName)
                                        .child(postId)
                                        .get()
                                        .addOnSuccessListener { data ->
                                            Log.d("ProfileViewModel", "getUserPosts Success")
                                            _userPosts.value += Post(data.getValue(PostWrapper::class.java) as PostWrapper)
                                        }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }
                )
        }
    }

    fun createChatSession(
        opponentId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _creatingChatSessionState.value = Resource.Error("User not logged in")
            return
        }
        val chatId = FirebaseDatabase.getInstance().getReference(chatReferenceName).push().key
        if (chatId == null) {
            _creatingChatSessionState.value = Resource.Error("Failed to create chat session")
            return
        }

        val chatSession = ChatSession(
            id = chatId,
            user1Id = currentUser.uid,
            user2Id = opponentId,
            lastMessage = "",
            isClosed = false
        )

        // Add new session to user 1
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(currentUser.uid)
            .child("chatSessions")
            .push()
            .setValue(chatId)
            .addOnSuccessListener {
                // Add new session to user 2
                FirebaseDatabase.getInstance()
                    .getReference(userReferenceName)
                    .child(currentUser.uid)
                    .child("chatSessions")
                    .push()
                    .setValue(chatId)
                    .addOnSuccessListener {
                        // Add chat session on database
                        FirebaseDatabase.getInstance()
                            .getReference(chatReferenceName)
                            .child(chatId)
                            .setValue(chatSession)
                            .addOnSuccessListener {
                                _creatingChatSessionState.value = Resource.Success()
                            }
                    }
                    .addOnFailureListener {
                        _creatingChatSessionState.value = Resource.Error("Failed to create chat session")
                    }
            }
            .addOnFailureListener {
                _creatingChatSessionState.value = Resource.Error("Failed to create chat session")
            }
    }
}

