package com.imeanttobe.drawapplication.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.etc.UserWrapper
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.util.StorageUtil.Companion.uploadPictureToStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val referenceName = "user"
    private val _signOutState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _user = MutableStateFlow<User?>(null)
    private val _dialogState = mutableStateOf(false)

    // Getter
    val signOutState = _signOutState.asStateFlow()
    val user = _user.asStateFlow()
    val dialogState: State<Boolean> = _dialogState

    // Initialization
    init {
        // Get user's profile data from FirebaseDatabase
        getUserData()
    }

    // Methods
    private fun getUserData() {
        firebaseAuth.currentUser?.let { user ->
            firebaseDatabase
                .getReference(referenceName)
                .child(user.uid)
                .get()
                .addOnSuccessListener { data ->
                    Log.d("ProfileViewModel", "getUserData Success")
                    _user.value = User(data.getValue(UserWrapper::class.java) as UserWrapper)
                }
        }
    }

    fun setDialogState(state: Boolean) {
        _dialogState.value = state
    }

    fun addImageUri(
        uri: Uri?,
        context : Context
    ) {
        uri?.let { newUri ->
            _user.value?.let { currentUser ->
                uploadPictureToStorage(
                    newUri,
                    context,
                    currentUser.id.toString(),
                    currentUser.pictureIds.size
                ) { pictureUri ->
                    // ProfileView에서 user의 변경을 인식해야 그림 추가 등록시 grid를 재구성함
                    // 따라서 image 추가시 pictureUri를 추가한 user로 아예 바꿈.
                    val updatedPictureIds = currentUser.pictureIds + pictureUri.toString()
                    _user.value = currentUser.copy(pictureIds = updatedPictureIds as MutableList<String>)

                    // pictureUri를 Firebase Realtime Database에 추가
                    firebaseDatabase.reference.child("user")
                        .child(_user.value!!.id)
                        .child("pictureIds")
                        .child(currentUser.pictureIds.size.toString())
                        .setValue(pictureUri.toString())
                }
            }
        }
    }

    fun addProfileImageUri(uri: Uri?) {
        //
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _signOutState.value = Resource.Success()
    }

    fun updateUserData() {
        FirebaseAuth.getInstance()
            .currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
//                    .setDisplayName("")
//                    .setPhotoUri(Uri.EMPTY)
                    .build()
            )
    }
}