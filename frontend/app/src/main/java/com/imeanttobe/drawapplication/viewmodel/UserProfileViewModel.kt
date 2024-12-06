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
    val userId = ""

    fun setDialogState(newValue: Int) {
        _dialogState.intValue = newValue
    }
/*
    init {
        getUserdata(userId ="" )
    }

*/
    private fun getUserdata(userId: String){
        val userId = userId //
// Firebase Realtime Database에서 'user' 경로에 접근하여 userId에 해당하는 데이터를 읽기
        val userReference = FirebaseDatabase.getInstance().getReference(userReferenceName).child(userId)
        userReference.get().addOnSuccessListener  { data ->
            Log.d("ProfileViewModel", "getUserData Success")
            _user.value = User(data.getValue(UserWrapper::class.java) as UserWrapper)
        }

    }
}

