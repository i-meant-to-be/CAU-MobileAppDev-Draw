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
import kotlin.random.Random

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val userReferenceName = "user"
    private val postReferenceName = "post"

    private val _user = MutableStateFlow<User?>(null)
    private val _userPosts = MutableStateFlow<MutableList<Post>>(mutableListOf<Post>())
    private val _dialogState = mutableIntStateOf(0)
    private val _currentPictureDescription = mutableStateOf("")
    private val _currentPictureUri = mutableStateOf(Uri.EMPTY)
    private val _currentPostId = mutableStateOf("")

    // Getter
    val user = _user.asStateFlow()
    val userPosts = _userPosts.asStateFlow()
    val dialogState: State<Int> = _dialogState
    val currentPictureDescription: State<String> = _currentPictureDescription
    val currentPictureUri: State<Uri> = _currentPictureUri
    val currentPostId: State<String> = _currentPostId

    // Initialization
    init {
        // Get user's profile data from FirebaseDatabase
        getUserData()
    }

    // Methods
    private fun getUserData() {
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
                        // 새 리스트 추가
                        // _userPosts.value = mutableListOf<Post>()
                        val newPosts = mutableListOf<Post>()

                        snapshot.children.forEach { data ->
                            val postId = data.getValue(String::class.java)
                            postId?.let { postId ->
                                FirebaseDatabase.getInstance()
                                    .getReference(postReferenceName)
                                    .child(postId)
                                    .get()
                                    .addOnSuccessListener { data ->
                                        Log.d("ProfileViewModel", "getUserPosts Success")
                                        newPosts.add(Post(data.getValue(PostWrapper::class.java) as PostWrapper))
                                        if (newPosts.size == snapshot.childrenCount.toInt()) {
                                            newPosts.sortByDescending { post -> post.timestamp }
                                            _userPosts.value = newPosts
                                        }
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )

    }

    fun setDialogState(newValue: Int) {
        _dialogState.intValue = newValue
    }

    fun setPictureDialogData(
        postId: String,
        uri: Uri,
        description: String
    ) {
        _currentPostId.value = postId
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
                Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE)
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

    fun deletePost(){
        FirebaseDatabase.getInstance()
            .getReference(userReferenceName) // 'userReferenceName' 경로 참조
            .child(_user.value?.id.toString()) // 사용자의 'id'로 자식 경로 설정
            .child("postIds") // 'postIds' 하위 경로로 이동
            .orderByValue() // 값으로 정렬 (값을 기준으로 쿼리)
            .equalTo(_currentPostId.value) // '_currentPostId.value' 값을 가진 노드를 찾기
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // 해당 값을 가진 노드를 찾았을 때
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
            })

        FirebaseDatabase.getInstance()
            .getReference(postReferenceName)
            .child(_currentPostId.value)
            .removeValue()
            .addOnSuccessListener{
                _dialogState.intValue = 0
                getUserData()
            }
            .addOnFailureListener { error ->
                Log.d("ProfileViewModel", "Error deleting post: ${error.message}")
            }
    }

}