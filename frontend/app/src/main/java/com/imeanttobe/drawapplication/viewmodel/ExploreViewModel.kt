package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val _searchText = mutableStateOf("")
    private val _dialogState = mutableStateOf(false)

    // Getter
    val searchText: State<String> = _searchText
    val dialogState: State<Boolean> = _dialogState

    // Methods
    fun setSearchText(newValue: String) {
        _searchText.value = newValue
    }

    fun setDialogState(newValue: Boolean) {
        _dialogState.value = newValue
    }
}