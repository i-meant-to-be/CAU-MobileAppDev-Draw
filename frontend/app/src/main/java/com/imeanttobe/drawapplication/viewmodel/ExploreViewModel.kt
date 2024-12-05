package com.imeanttobe.drawapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.imeanttobe.drawapplication.data.enum.ExploreSearchOption
import com.imeanttobe.drawapplication.data.etc.PostWrapper
import com.imeanttobe.drawapplication.data.etc.UserWrapper
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {
    operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Variables
    private val postReferenceName = "post"
    private val userReferenceName = "user"

    private val _searchText = mutableStateOf("")
    private val _dialogState = mutableStateOf(false)
    private val _expanded = mutableStateOf(false)
    private val _filterState = mutableStateOf(ExploreSearchOption.BY_NAME)
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    private val _refreshState = MutableStateFlow(false)

    // Getter
    val searchText: State<String> = _searchText
    val dialogState: State<Boolean> = _dialogState
    val expanded: State<Boolean> = _expanded
    val filterState: State<ExploreSearchOption> = _filterState
    val posts: StateFlow<List<Post>> = _posts
    val refreshState = _refreshState.asStateFlow()

    // Initialization
    init {
        getPosts()
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

    fun onPullToRefreshTriggered() {
        _refreshState.update { true }
        getPosts()
        _refreshState.update { false }
    }

    private fun getPosts() {
        val posts = mutableListOf<Post>()

        FirebaseDatabase
            .getInstance()
            .getReference(postReferenceName)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (childSnapshot in snapshot.children) {
                            val post = Post(childSnapshot.getValue(PostWrapper::class.java) as PostWrapper)
                            posts.add(post)
                        }
                        _posts.value = posts
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
            )
    }

    fun getUserFromDB(post: Post, onResult: (User?) -> Unit){
        try{
            FirebaseDatabase.getInstance()
                .getReference(userReferenceName)
                .child(post.userId)
                .get()
                .addOnSuccessListener { data ->
                    Log.d("ExploreViewModel", "getUserFromDB Success: $data")
                    onResult(User(data.getValue(UserWrapper::class.java) as UserWrapper))
                }.addOnFailureListener { e ->
                    Log.e("ExploreViewModel", "Error fetching user: ${e.message}")
                    onResult(null)
                }
        } catch (e: Exception) {
            Log.e("ExploreViewModel", "Unexpected error: ${e.message}")
            onResult(null)
        }
    }

}