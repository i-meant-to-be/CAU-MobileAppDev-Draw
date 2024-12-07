package com.imeanttobe.drawapplication.viewmodel


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.PostWrapper
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.etc.UserWrapper
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.util.StorageUtil.Companion.uploadPictureToStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val userReferenceName = "user"
    private val postReferenceName = "post"

    private val _signOutState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _user = MutableStateFlow<User?>(null)
    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    private val _dialogState = mutableIntStateOf(0)
    private val _currentPictureDescription = mutableStateOf("")
    private val _currentPictureUri = mutableStateOf(Uri.EMPTY)

    // Getter
    val signOutState = _signOutState.asStateFlow()
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
    fun createChatSession(opponentId: String, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("UserProfileViewModel", "No authenticated user")
            onComplete(false)
            return
        }

        val chatReferenceName = "chat_sessions"
        val chatId = FirebaseDatabase.getInstance()
            .getReference(chatReferenceName)
            .push()
            .key // Firebase에서 새로운 키 생성

        if (chatId == null) {
            Log.e("UserProfileViewModel", "Failed to generate chat ID")
            onComplete(false)
            return
        }

        // 채팅 세션 생성
        val newChatSession = mapOf(
            "id" to chatId,
            "user1Id" to currentUser.uid,
            "user2Id" to opponentId,
            "lastMessage" to "",
            "isClosed" to false
        )

        val database = FirebaseDatabase.getInstance()
        val userChatsRef = database.getReference(userReferenceName).child(currentUser.uid).child("chatSessions")
        val opponentChatsRef = database.getReference(userReferenceName).child(opponentId).child("chatSessions")

        database.getReference(chatReferenceName).child(chatId)
            .setValue(newChatSession)
            .addOnSuccessListener {
                // ID 문자열만 저장
                userChatsRef.push().setValue(chatId) // 현재 사용자에게 세션 추가
                opponentChatsRef.push().setValue(chatId) // 상대방에게 세션 추가

                Log.d("UserProfileViewModel", "Chat session created successfully")
                onComplete(true)
            }
    }



/*

*/
}

