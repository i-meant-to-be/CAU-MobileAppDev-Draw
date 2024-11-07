package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {
    // Variables
    var searchText by mutableStateOf("")
        private set

    // Methods
    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }
}