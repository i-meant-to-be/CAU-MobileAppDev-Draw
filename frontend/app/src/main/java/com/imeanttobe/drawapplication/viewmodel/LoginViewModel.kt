package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    // Values
    private val _username = mutableStateOf("")
    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)

    // Getter
    val username: State<String> = _username
    val state = _state.asStateFlow()

    // Methods
    fun setUsername(value: String) {
        _username.value = value
    }

    fun signIn(email: String, password: String) {
        _state.value = SignInState.Loading
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.value = SignInState.Success
                }
                else {
                    _state.value = SignInState.Error
                }
            }
    }
}

sealed class SignInState {
    object  Nothing: SignInState()
    object  Loading: SignInState()
    object  Success: SignInState()
    object  Error: SignInState()
}