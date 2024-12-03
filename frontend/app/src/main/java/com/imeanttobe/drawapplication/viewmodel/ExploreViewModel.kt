package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imeanttobe.drawapplication.data.enum.ExploreSearchOption
import com.imeanttobe.drawapplication.data.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {
    operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Variables
    private val _searchText = mutableStateOf("")
    private val _dialogState = mutableStateOf(false)
    private val _expanded = mutableStateOf(false)
    private val _filterState = mutableStateOf(ExploreSearchOption.BY_NAME)
    private val _posts = MutableStateFlow<List<Post>>(emptyList()) // This is for the posts received from firebase

    // Getter
    val searchText: State<String> = _searchText
    val dialogState: State<Boolean> = _dialogState
    val expanded: State<Boolean> = _expanded
    val filterState: State<ExploreSearchOption> = _filterState
    val posts: StateFlow<List<Post>> = _posts // This is for the posts received from firebase

    init {
        // TODO: this is for sample messages and have to replaced or removed when firebase is applied
        viewModelScope.launch {
            _posts.emit(
                listOf(
                    Post(
                        userId = 0,
                        description = "This is a description.",
                        imageUrl = ""
                    ),
                ) * 20
            )
        }
    }

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

        // TODO: And we have to refresh the variable 'posts' to display search result
    }
}