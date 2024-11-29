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

    // Getter
    val username: State<String> = _username

    // Setter
    fun setUsername(value: String) {
        _username.value = value
    }

    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state = _state.asStateFlow()

    fun signIn(email: String, password : String){
        _state.value = SignInState.Loading
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    _state.value = SignInState.Success
                }
                else{
                    _state.value = SignInState.Error
                }
            }
    }

    /*
    *   현재 로그인한 id를 여기서 받아야 할까?
    *   아니면, 쓸때마다 현재 로그인 아이디를 받아오는게 좋을까 흠
    *   후자로 일단 해야겠다
    *
    * */

}

sealed class SignInState {
    object  Nothing: SignInState()
    object  Loading: SignInState()
    object  Success: SignInState()
    object  Error: SignInState()
}