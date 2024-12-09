package com.imeanttobe.drawapplication.viewmodel


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    private val _loadingUserDataState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _user = MutableStateFlow<User?>(null)
    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    private val _dialogState = mutableIntStateOf(0)
    private val _currentPictureDescription = mutableStateOf("")
    private val _currentPictureUri = mutableStateOf(Uri.EMPTY)

    // Getter
    val creatingChatSessionState = _creatingChatSessionState.asStateFlow()
    val loadingUserDataState = _loadingUserDataState.asStateFlow()
    val user = _user.asStateFlow()
    val userPosts = _userPosts.asStateFlow()
    val dialogState: State<Int> = _dialogState
    val currentPictureDescription: State<String> = _currentPictureDescription
    val currentPictureUri: State<Uri> = _currentPictureUri

    // Methods
    fun getUserData(
        id: String,
        onFailure: () -> Unit
    ) {
        _loadingUserDataState.value = Resource.Loading()

        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(id)
            .get()
            .addOnSuccessListener { task ->
                val userWrapper = task.getValue(UserWrapper::class.java)
                if (userWrapper != null) {
                    _user.value = User(userWrapper)

                    // @Suppress("SENSELESS_COMPARISON")
                    val postFetchTasks = _user.value!!.postIds.map { postId ->

                            FirebaseDatabase.getInstance()
                                .getReference(postReferenceName)
                                .child(postId)
                                .get()
                                .continueWith { task ->
                                    if (task.isSuccessful) {
                                        val postWrapper = task.result.getValue(PostWrapper::class.java)
                                        if (postWrapper != null) {
                                            Post(postWrapper)
                                        } else {
                                            null
                                        }
                                    } else {
                                        null
                                    }
                                }
                    }

                    Tasks.whenAllComplete(postFetchTasks)
                        .addOnCompleteListener {
                            val posts = postFetchTasks.mapNotNull { it.result }
                            _userPosts.value = posts
                            _loadingUserDataState.value = Resource.Success()
                        }
                        .addOnFailureListener {
                            _loadingUserDataState.value = Resource.Error("Failed to load user data")
                            onFailure()
                        }
                } else {
                    _loadingUserDataState.value = Resource.Error("Failed to load user data")
                    onFailure()
                }
            }

        /*
        // Old one
        _loadingUserDataState.value = Resource.Loading()

        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(id)
            .get()
            .addOnSuccessListener { data ->
                Log.d("UserProfileViewModel", "getUserData Success")
                _user.value = User(data.getValue(UserWrapper::class.java) as UserWrapper)
                getUserPosts(onFailure = onFailure)
            }
            .addOnFailureListener {
                _loadingUserDataState.value = Resource.Error("Failed to load user data")
                onFailure()
            }

         */
    }

    fun getUserPosts(onFailure: () -> Unit) {
        val posts = mutableListOf<Post>()

        if (user.value != null) {
            user.value!!.postIds.forEach { postId ->
                @Suppress("SENSELESS_COMPARISON")
                if (postId != null) {
                    FirebaseDatabase.getInstance()
                        .getReference(postReferenceName)
                        .child(postId)
                        .get()
                        .addOnSuccessListener { data ->
                            Log.d("UserProfileViewModel", "getUserPosts Success")
                            posts.add(Post(data.getValue(PostWrapper::class.java) as PostWrapper))
                            if (posts.size == user.value!!.postIds.size) {
                                posts.sortByDescending { post -> post.timestamp }
                                _userPosts.value = posts
                            }
                        }
                        .addOnFailureListener {
                            _loadingUserDataState.value = Resource.Error("Failed to load user data")
                            onFailure()
                        }
                }
            }
        } else {
            onFailure()
        }
    }


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

    fun createChatSession(
        userId: String,
        onComplete: (Boolean) -> Unit
    ) {
        _creatingChatSessionState.value = Resource.Loading()

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
            user2Id = userId,
            lastMessage = "",
            isClosed = false
        )

        // Push chat sessions on database
        FirebaseDatabase.getInstance()
            .getReference(chatReferenceName)
            .child(chatId)
            .setValue(chatSession)
            .addOnSuccessListener {
                // Get user data of 2 participants
                val userFetchTasks = listOf(userId, currentUser.uid).map { id ->
                    FirebaseDatabase.getInstance()
                        .getReference(userReferenceName)
                        .child(id)
                        .get()
                        .continueWith { task ->
                            if (task.isSuccessful) {
                                val userWrapper = task.result.getValue(UserWrapper::class.java)
                                if (userWrapper != null) {
                                    User(userWrapper)
                                } else {
                                    null
                                }
                            } else {
                                null
                            }
                        }
                }

                // Check whether they are null
                Tasks.whenAllComplete(userFetchTasks)
                    .addOnCompleteListener {
                        val participants = userFetchTasks.mapNotNull { it.result }
                        if (participants.size == 2) {
                            participants.forEach { participant ->
                                participant.chatSessions.add(chatId)
                                FirebaseDatabase.getInstance()
                                    .getReference(userReferenceName)
                                    .child(participant.id)
                                    .setValue(UserWrapper(participant))
                                    .addOnSuccessListener {
                                        onComplete(true)
                                    }
                                    .addOnFailureListener {
                                        _creatingChatSessionState.value = Resource.Error("Failed to create chat session")
                                    }
                            }
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

