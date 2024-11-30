package com.imeanttobe.drawapplication.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

fun loginUser(email: String, password: String) {
    val auth = FirebaseAuth.getInstance()
    Log.d("LoginPage", "loginUser")
    auth.signInWithEmailAndPassword(email, password)
}

fun logoutUser(){
    val auth = FirebaseAuth.getInstance()
    Log.d("LoginPage", "logoutUser")
    auth.signOut()
}

fun registerUser(email: String, password: String) {
    val auth = FirebaseAuth.getInstance()
    Log.d("registerPage", "registerUser")
    auth.createUserWithEmailAndPassword(email, password)
}