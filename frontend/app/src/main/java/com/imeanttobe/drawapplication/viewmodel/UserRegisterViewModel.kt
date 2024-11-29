package com.imeanttobe.drawapplication.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModel
import com.imeanttobe.drawapplication.data.enum.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class UserRegisterViewModel @Inject constructor() : ViewModel() {
    //--------------------------------------------------------------------------------
    //register1view

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private var _password = mutableStateOf("")
    val password: State<String> = _password

    private val _passwordConfirm = mutableStateOf("")
    val passwordConfirm: State<String> = _passwordConfirm

    private val _isValid1 = MutableStateFlow<Boolean>(false)
    val isValid1 = _isValid1.asStateFlow()

    //---------------------------------------------------------------------------------
    //register2view

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _instaId = MutableStateFlow("")
    val instaId = _instaId.asStateFlow()

    private val _userType = MutableStateFlow(UserType.WEBTOON_ARTIST)
    val userType = _userType.asStateFlow()

    private val _isValid2 = MutableStateFlow<Boolean>(false)
    val isValid2 = _isValid2.asStateFlow()

    private val _photoUri = MutableStateFlow<Uri>(Uri.parse("android.resource://com.example.myapp/drawable/basic_profile"))
    val photoUri = _photoUri


    //---------------------------------------------------------------------------------
    //register1view function

    fun updateEmail(newEmail: String) { _email.value = newEmail  }
    fun updatePassword(newPassword: String){ _password.value = newPassword }
    fun updatePasswordConfirm(newPasswordConfirm: String){ _passwordConfirm.value = newPasswordConfirm }

    fun isValidateInput1() {
        _isValid1.value = isValidEmail(email.value) && isValidPassword(password.value)&& isPasswordConfirmed()
    }

    // 유효성 검사 함수
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // 비밀번호 길이 검사 로직 (필요에 따라 추가)
        return password.isNotEmpty() && password.isNotBlank()
    }

    fun isPasswordConfirmed(): Boolean {
        return password.value == passwordConfirm.value
    }

    //---------------------------------------------------------------------------------
    //register2view function

    fun isValidateInput2() {
        _isValid2.value = nickname.value.isNotBlank() //&& 이미지 1개 이상 들어와 있는지 체크
    }

    fun updateNickname(nickname: String){ _nickname.value = nickname}
    fun updateInstaId(instaId: String){_instaId.value = instaId }
    fun updateUserType(userType: UserType){_userType.value = userType}

    //---------------------------------------------------------------------------------
    //firebase function

    fun signUp(onSuccess : () -> Unit){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    task.result.user?.let{
                        it.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(nickname.value)
                                .setPhotoUri(photoUri.value)
                                .build()
                        ).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // 프로필 업데이트가 성공적으로 완료되면 네비게이션 호출
                                Log.d("UserRegisterViewModel", "sign up success")
                                onSuccess()
                            } else {
                                // 프로필 업데이트 실패 처리
                                // 예: 에러 메시지 표시
                            }
                        }
                    }
                }
                else{
                    Log.d("UserRegisterViewModel", "sign up fail")
                }
            }
    }
}