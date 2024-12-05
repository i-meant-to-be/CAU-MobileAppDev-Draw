package com.imeanttobe.drawapplication.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.PostWrapper
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.etc.UserWrapper
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.util.StorageUtil.Companion.uploadPictureToStorage
import com.imeanttobe.drawapplication.util.StorageUtil.Companion.uploadProfilePhotoToStorage
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
    private val _description = mutableStateOf("")
    private val _userType = mutableStateOf(UserType.UNDEFINED)
    private val _profilePhotoUri = mutableStateOf(Uri.EMPTY)
    private val _pictureUri = mutableStateOf(Uri.EMPTY)
    private val _isAllValid = mutableStateOf(false)

    // Getter
    val registerState = _registerState.asStateFlow()
    val nickname: State<String> = _nickname
    val instagramId: State<String> = _instagramId
    val introduce: State<String> = _introduce
    val description: State<String> = _description
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

    fun setDescription(newValue: String) {
        _description.value = newValue
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
        context: Context,
        email: String,
        pw: String,
        phoneNumber: String
    ) {
        _registerState.value = Resource.Loading()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.user != null) {
                    val userId = task.result.user!!.uid

                    //프로필 사진 저장하고 그 uri 가져와서 저장.
                    uploadProfilePhotoToStorage(_profilePhotoUri.value, context, userId) { photoUri ->
                        uploadPictureToStorage(_pictureUri.value, context, userId, 0) { pictureUri -> //여기서 0은 register시에 pictureIds의 첫번째를 뜻하는 거
                            val user = User(
                                id = userId,
                                nickname = _nickname.value,
                                email = email,
                                profilePhotoUri = photoUri!!,
                                instagramId = _instagramId.value,
                                type = _userType.value,
                                introduce = _introduce.value,
                                phoneNumber = phoneNumber,
                            )
                            val postId = FirebaseDatabase.getInstance().getReference("post").push().key!!
                            val post = Post(
                                id = postId,
                                userId = userId,
                                description = _description.value,
                                imageUri = pictureUri!!
                            )

                            Log.d(
                                "RegisterUserProfileViewModel",
                                "signUp: before add pictureId. loadPictureUri: $pictureUri"
                            )
                            user.postIds.add(postId)

                            Log.d(
                                "RegisterUserProfileViewModel",
                                "signUp: after add pictureId"
                            )
                            FirebaseDatabase.getInstance()
                                .getReference("user")
                                .child(userId)
                                .setValue(UserWrapper(user))

                            Log.d(
                                "RegisterUserProfileViewModel",
                                "signUp: after add user data on Firebase Database"
                            )
                            FirebaseDatabase.getInstance()
                                .getReference("post")
                                .child(postId)
                                .setValue(PostWrapper(post))

                            _registerState.value = Resource.Success()
                        }
                    }
                } else {
                    _registerState.value =
                        Resource.Error(task.exception?.message ?: "Unknown Error")
                }
            }
    }
}