package com.imeanttobe.drawapplication.viewmodel

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserAccountViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private var _password = mutableStateOf("")
    val password: State<String> = _password

    private val _passwordConfirm = mutableStateOf("")
    val passwordConfirm: State<String> = _passwordConfirm

    private val _phoneNumber = mutableStateOf("")
    val phoneNumber: State<String> = _phoneNumber

    private val _isEmailValid = mutableStateOf(false)
    val isEmailValid: State<Boolean> = _isEmailValid

    private val _isPasswordValid = mutableStateOf(false)
    val isPasswordValid: State<Boolean> = _isPasswordValid

    private val _isPasswordConfirmed = mutableStateOf(false)
    val isPasswordConfirmed: State<Boolean> = _isPasswordConfirmed

    private val _isPhoneNumberValid = mutableStateOf(false)
    val isPhoneNumberValid: State<Boolean> = _isPhoneNumberValid

    private val _isAllValid = mutableStateOf(false)
    val isAllValid: State<Boolean> = _isAllValid

    // Methods
    private fun updateAllValid() {
        _isAllValid.value = isEmailValid.value && isPasswordValid.value && isPasswordConfirmed.value && isPhoneNumberValid.value
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()
        updateAllValid()
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        _isPasswordValid.value = newPassword.length > 5
        updateAllValid()
    }

    fun updatePasswordConfirm(newPasswordConfirm: String) {
        _passwordConfirm.value = newPasswordConfirm
        _isPasswordConfirmed.value = newPasswordConfirm == password.value && newPasswordConfirm.isNotEmpty()
        updateAllValid()
    }

    fun updatePhoneNumber(newValue: String) {
        _passwordConfirm.value = newValue
        _isPhoneNumberValid.value = Patterns.PHONE.matcher(newValue).matches()
        updateAllValid()
    }
}