package com.imeanttobe.drawapplication.viewmodel

import android.content.Context
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
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.util.StorageUtil.Companion.uploadPictureToStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
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

    // Initialization
    init {
        // Get user's profile data from FirebaseDatabase
        getUserData()
    }

    // Methods
    fun getUserData() {
        FirebaseAuth.getInstance()
            .currentUser?.let { user ->
                FirebaseDatabase.getInstance()
                    .getReference(userReferenceName)
                    .child(user.uid)
                    .get()
                    .addOnSuccessListener { data ->
                        Log.d("ProfileViewModel", "getUserData Success")
                        _user.value = User(data.getValue(UserWrapper::class.java) as UserWrapper)
                        listenUserPosts()
                    }
            }
    }

    private fun listenUserPosts() {
        // Get user's posts
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(user.value!!.id)
            .child("postIds")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        _userPosts.value = emptyList()

                        snapshot.children.forEach { data ->
                            val postId = data.getValue(String::class.java)
                            postId?.let { postId ->
                                FirebaseDatabase.getInstance()
                                    .getReference(postReferenceName)
                                    .child(postId)
                                    .get()
                                    .addOnSuccessListener { data ->
                                        Log.d("ProfileViewModel", "getUserPosts Success")
                                        _userPosts.value += Post(data.getValue(PostWrapper::class.java) as PostWrapper)
                                        _userPosts.value.sortedByDescending { post -> post.timestamp }
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

    fun addPost(
        uri: Uri,
        description: String,
        context : Context
    ) {
        _user.value?.let { currentUser ->
            uploadPictureToStorage(
                uri,
                context,
                currentUser.id.toString(),
                currentUser.postIds.size
            ) { pictureUri ->
                // ProfileView에서 user의 변경을 인식해야 그림 추가 등록시 grid를 재구성함
                // 따라서 image 추가시 pictureUri를 추가한 user로 아예 바꿈.
                val postId = FirebaseDatabase.getInstance().getReference(postReferenceName).push().key!!
                val post = Post(
                    id = postId,
                    userId = currentUser.id,
                    description = description,
                    imageUri = pictureUri!!
                )
                val updatedPostIds = currentUser.postIds + pictureUri.toString()
                _user.value = currentUser.copy(postIds = updatedPostIds as MutableList<String>)

                // pictureUri를 Firebase Realtime Database에 추가
                FirebaseDatabase.getInstance()
                    .getReference(userReferenceName)
                    .child(_user.value!!.id)
                    .child("postIds")
                    .child(currentUser.postIds.size.toString())
                    .setValue(postId)

                // Add post
                FirebaseDatabase.getInstance()
                    .getReference(postReferenceName)
                    .child(postId)
                    .setValue(PostWrapper(post))
            }
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _signOutState.value = Resource.Success()
    }

    fun updateUser(user: User) {
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName)
            .child(user.id)
            .setValue(UserWrapper(user))
            .addOnSuccessListener {
                getUserData()
            }

    }
}