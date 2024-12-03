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
import com.imeanttobe.drawapplication.data.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterUserProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val _registerState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _nickname = mutableStateOf("")
    private val _instagramId = mutableStateOf("")
    private val _introduce = mutableStateOf("")
    private val _userType = mutableStateOf(UserType.UNDEFINED)
    private val _profilePhotoUri = mutableStateOf(Uri.EMPTY)
    private val _pictureUri = mutableStateOf(Uri.EMPTY)
    private val _isAllValid = mutableStateOf(false)

    // Getter
    val registerState = _registerState.asStateFlow()
    val nickname: State<String> = _nickname
    val instagramId: State<String> = _instagramId
    val introduce: State<String> = _introduce
    val userType: State<UserType> = _userType
    val profilePhotoUri: State<Uri> = _profilePhotoUri
    val pictureUri: State<Uri> = _pictureUri
    val isAllValid: State<Boolean> = _isAllValid

    // Methods
    private fun checkAllValid() {
        _isAllValid.value = _nickname.value.isNotEmpty() && _userType.value != UserType.UNDEFINED && _pictureUri.value != Uri.EMPTY
    }

    fun setNickname(newValue: String) {
        _nickname.value = newValue
        checkAllValid()
    }

    fun setInstagramId(newValue: String) {
        _instagramId.value = newValue
    }

    fun setIntroduce(newValue: String) {
        _introduce.value = newValue
    }

    fun setUserType(newValue: UserType) {
        _userType.value = newValue
        checkAllValid()
    }

    fun setProfilePhotoUri(newValue: Uri) {
        _profilePhotoUri.value = newValue
    }

    fun setPictureUri(newValue: Uri) {
        _pictureUri.value = newValue
        checkAllValid()
    }

    fun signUp(
        email: String,
        pw: String,
        phoneNumber: String
    ) {
        _registerState.value = Resource.Loading()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.user != null) {
                    val user = task.result.user!!
                    user.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(_nickname.value)
                            .setPhotoUri(_profilePhotoUri.value)
                            .build()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userProfile = UserProfile(
                                instagramId = _instagramId.value,
                                type = _userType.value,
                                phoneNumber = phoneNumber,
                                introduce = _introduce.value,
                            )
                            userProfile.pictureIds.add(_pictureUri.value.toString())
                            FirebaseDatabase.getInstance()
                                .getReference("user_data")
                                .child(user.uid)
                                .setValue(userProfile)
                        }
                        _registerState.value = Resource.Success()
                    }
                } else {
                    _registerState.value =
                        Resource.Error(task.exception?.message ?: "Unknown Error")
                }
            }
    }
}