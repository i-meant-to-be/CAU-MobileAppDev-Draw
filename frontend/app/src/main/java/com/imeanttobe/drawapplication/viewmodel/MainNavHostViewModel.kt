package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavHostViewModel @Inject constructor() : ViewModel() {
    private val _bottomNavBarIndex = mutableIntStateOf(0)

    val bottomNavBarIndex: State<Int> = _bottomNavBarIndex

    fun setBottomNavBarIndex(newValue: Int) {
        _bottomNavBarIndex.intValue = newValue
    }
}