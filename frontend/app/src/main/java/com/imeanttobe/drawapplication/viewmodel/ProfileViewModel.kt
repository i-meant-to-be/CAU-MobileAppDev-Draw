package com.imeanttobe.drawapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    //ProfileCard

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



    //---------------------------------------------------------------------------

    private val _state = MutableStateFlow<SignOutState>(SignOutState.Nothing)
    val state = _state.asStateFlow()

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        _state.value = SignOutState.LoggedOut
    }
}

sealed class SignOutState{
    object Nothing: SignOutState()
    object LoggedOut: SignOutState()
}