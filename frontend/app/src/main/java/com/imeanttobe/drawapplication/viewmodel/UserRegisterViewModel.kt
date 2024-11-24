package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class UserRegisterViewModel @Inject constructor() : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private var _password = mutableStateOf("")
    val password: State<String> = _password

    private val _passwordConfirm = mutableStateOf("")
    val passwordConfirm: State<String> = _passwordConfirm

    private val _isValid = mutableStateOf(false)
    val isValid: State<Boolean> = _isValid


    fun updateEmail(newEmail: String) {
        _email.value = newEmail // value 속성을 사용하여 상태 업데이트
    }
    fun updatePassword(newPassword: String){
        _password.value = newPassword // value 속성을 사용하여 상태 업데이트
    }
    fun updatePasswordConfirm(newPasswordConfirm: String){
        _passwordConfirm.value = newPasswordConfirm // value 속성을 사용하여 상태 업데이트
    }


    fun validateInput() {
        _isValid.value = isValidEmail(email.value) && isValidPassword(password.value)&& isPasswordConfirmed()
    }

    // 유효성 검사 함수
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // 비밀번호 길이 검사 로직 (필요에 따라 추가)
        return true // 임시로 true 반환
    }


    fun isPasswordConfirmed(): Boolean {
        return password.value == passwordConfirm.value
    }



}