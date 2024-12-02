package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imeanttobe.drawapplication.data.enum.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserProfileViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val _nickname = mutableStateOf("")
    private val _instagramId = mutableStateOf("")
    private val _userType = mutableStateOf(UserType.UNDEFINED)
    private val _photoUri = mutableStateOf("")
    private val _isAllValid = mutableStateOf(false)

    // Getter
    val nickname: State<String> = _nickname
    val instagramId: State<String> = _instagramId
    val userType: State<UserType> = _userType
    val photoUri: State<String> = _photoUri
    val isAllValid: State<Boolean> = _isAllValid

    // Methods
    private fun checkAllValid() {
        _isAllValid.value = _nickname.value.isNotEmpty() && _userType.value != UserType.UNDEFINED
    }

    fun setNickname(newValue: String) {
        _nickname.value = newValue
    }

    fun setInstagramId(newValue: String) {
        _instagramId.value = newValue
    }

    fun setUserType(newValue: UserType) {
        _userType.value = newValue
    }

    fun setPhotoUri(newValue: String) {
        _photoUri.value = newValue
    }
}