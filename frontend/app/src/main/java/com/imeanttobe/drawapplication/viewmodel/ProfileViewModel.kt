package com.imeanttobe.drawapplication.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.model.User
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
    private val _signOutState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _user = MutableStateFlow<User?>(null)
    private val _dialogState = mutableStateOf(false)
    private val _profilePhotoUri = mutableStateOf(Uri.EMPTY)
    private val _nickname = mutableStateOf("")
    private val _usertype = mutableStateOf(UserType.UNDEFINED)
    private val _information= mutableStateOf("")

    // Getter
    val signOutState = _signOutState.asStateFlow()
    val user = _user.asStateFlow()
    val dialogState: State<Boolean> = _dialogState
    val profilePhotoUri: State<Uri> = _profilePhotoUri
    val nickname: State<String> = _nickname
    val userType: State<UserType> = _usertype
    val information: State<String> = _information


    // Initialization
    init {
        // Get data from FirebaseAuth
        firebaseAuth.currentUser?.let { user ->
            _profilePhotoUri.value = user.photoUrl
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
                    _user.value = data.getValue(User::class.java)
                }
        }
    }
    fun setNickname(newValue: String) {
        _nickname.value = newValue
    }

    fun setUserType(newValue: UserType) {
        _usertype.value = newValue

    }

    fun setDialogState(state: Boolean) {
        _dialogState.value = state
    }

    fun addImageUri(uri: Uri?) {
        _user.value?.pictureIds?.add(uri.toString())
    }

    fun addProfileImageUri(uri: Uri?) {
        _profilePhotoUri.value = uri
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