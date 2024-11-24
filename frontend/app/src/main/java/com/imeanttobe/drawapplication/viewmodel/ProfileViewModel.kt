package com.imeanttobe.drawapplication.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _imageUris = mutableStateListOf<Uri?>()
    val imageUris: List<Uri?> = _imageUris

    fun addImageUri(uri: Uri?) {
        _imageUris.add(uri)
    }

}