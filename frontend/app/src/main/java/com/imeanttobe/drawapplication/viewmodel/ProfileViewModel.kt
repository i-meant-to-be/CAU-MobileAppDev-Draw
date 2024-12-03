package com.imeanttobe.drawapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val referenceName = "user_data"
    private val _dialogState = mutableStateOf(false)
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    private val _profileImageUri = mutableStateOf(Uri.EMPTY)
    private val _nickname = mutableStateOf("")

    // Getter
    val dialogState: State<Boolean> = _dialogState
    val nickname: State<String> = _nickname
    val profileImageUri: State<Uri> = _profileImageUri
    val userProfile = _userProfile.asStateFlow()

    // Methods
    fun setDialogState(state: Boolean) {
        _dialogState.value = state
    }

    //ProfileCard
    private val _individualImageUri = MutableStateFlow<Uri?>(null)
    val individualImageUri = _individualImageUri.asStateFlow()
    
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

    // Initialization
    init {
        // Get data from FirebaseAuth
        firebaseAuth.currentUser?.let { user ->
            _profileImageUri.value = user.photoUrl
            _nickname.value = user.displayName!!
        }

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
                    _userProfile.value = data.getValue(UserProfile::class.java)
                }
        }
    }

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