package com.imeanttobe.drawapplication.viewmodel

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.imeanttobe.drawapplication.data.etc.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginDetailViewModel @Inject constructor() : ViewModel() {
    // Values
    private val _loginState = MutableStateFlow<Resource>(Resource.Nothing())
    private val _email = mutableStateOf("aaa@aaa.com")
    private val _pw = mutableStateOf("111111")
    private val _isAllValid = mutableStateOf(true)
    private val _isEmailValid = mutableStateOf(true)
    private val _isPwValid = mutableStateOf(true)

    // Getter
    val loginState = _loginState.asStateFlow()
    val email: State<String> = _email
    val pw: State<String> = _pw
    val isAllValid: State<Boolean> = _isAllValid
    val isEmailValid: State<Boolean> = _isEmailValid
    val isPwValid: State<Boolean> = _isPwValid

    // Methods
    private fun updateAllValid() {
        _isAllValid.value = isEmailValid.value && isPwValid.value
    }

    fun setEmail(value: String) {
        _email.value = value
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(value).matches()
        updateAllValid()
    }

    fun setPw(value: String) {
        _pw.value = value
        _isPwValid.value = value.length > 5
        updateAllValid()
    }

    fun login() {
        _loginState.value = Resource.Loading()
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(_email.value, _pw.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginState.value = Resource.Success()
                    } else {
                        _loginState.value = Resource.Error(task.exception?.message ?: "로그인 실패")
                    }
                }
    }
}