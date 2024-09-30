package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavHostViewModel @Inject constructor() : ViewModel() {
    private val _currentIndex = mutableIntStateOf(0)

    val currentIndex: State<Int> = _currentIndex

    fun setCurrentIndex(newValue: Int) {
        _currentIndex.intValue = newValue
    }
}