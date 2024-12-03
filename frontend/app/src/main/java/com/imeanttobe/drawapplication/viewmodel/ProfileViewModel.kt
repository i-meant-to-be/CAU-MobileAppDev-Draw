package com.imeanttobe.drawapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.imeanttobe.drawapplication.data.etc.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val _dialogState = mutableStateOf(false)

    // Getter
    val dialogState: State<Boolean> = _dialogState

    // Methods
    fun setDialogState(state: Boolean) {
        _dialogState.value = state
    }

    //ProfileCard




    private val _individualImageUri = MutableStateFlow<Uri?>(null)
    val individualImageUri = _individualImageUri.asStateFlow()

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    private val _userNickname = MutableStateFlow<String?>(null)
    val userNickname = _userNickname.asStateFlow()

    init {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        _profileImageUri.value = firebaseUser?.photoUrl
        _userNickname.value = firebaseUser?.displayName
        Log.d("ProfileViewModel", "profile image ${_profileImageUri.value}")
        Log.d("ProfileViewModel", "profile name ${_userNickname.value}")

    }

    //---------------------------------------------------------------------------
    //ProfileViewGrid

    private val _imageUris = mutableStateListOf<Uri?>()
    val imageUris: List<Uri?> = _imageUris

    fun addImageUri(uri: Uri?) {
        _imageUris.add(uri)
    }

    fun addprofileImageUri(uri: Uri?) {
        _profileImageUri.value = uri
    }

    //---------------------------------------------------------------------------

    private val _signOutState = MutableStateFlow<Resource>(Resource.Nothing())
    val signOutState = _signOutState.asStateFlow()

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _signOutState.value = Resource.Success()
    }

    fun updateUserData() {
        FirebaseAuth.getInstance()
            .currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName("")
                    .setPhotoUri(Uri.EMPTY)
                    .build()
            )
    }
}