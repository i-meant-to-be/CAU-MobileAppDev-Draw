package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.imeanttobe.drawapplication.data.enum.ExploreSearchOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {
    // Variables
    private val _searchText = mutableStateOf("")
    private val _dialogState = mutableStateOf(false)
    private val _expanded = mutableStateOf(false)
    private val _filterState = mutableStateOf(ExploreSearchOption.BY_NAME)

    // Getter
    val searchText: State<String> = _searchText
    val dialogState: State<Boolean> = _dialogState
    val expanded: State<Boolean> = _expanded
    val filterState: State<ExploreSearchOption> = _filterState

    // Methods
    fun setSearchText(newValue: String) {
        _searchText.value = newValue
    }

    fun setDialogState(newValue: Boolean) {
        _dialogState.value = newValue
    }

    fun setExpanded(newValue: Boolean) {
        _expanded.value = newValue
    }

    fun setFilterState(newValue: ExploreSearchOption) {
        _filterState.value = newValue
    }

    fun search() {
        if (filterState.value == ExploreSearchOption.BY_NAME) {
            // TODO: Search by name
        } else {
            // TODO: Search by post
        }
    }
}