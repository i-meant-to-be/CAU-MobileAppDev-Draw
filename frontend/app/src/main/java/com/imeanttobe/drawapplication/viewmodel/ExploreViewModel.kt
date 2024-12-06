package com.imeanttobe.drawapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
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
    private val _postsAndUsers = MutableStateFlow<List<Pair<Post,User>>>(emptyList())

    // Getter
    val searchText: State<String> = _searchText
    val dialogState: State<Boolean> = _dialogState
    val expanded: State<Boolean> = _expanded
    val filterState: State<ExploreSearchOption> = _filterState
    val postsAndUsers: StateFlow<List<Pair<Post,User>>> = _postsAndUsers

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
        val originalPostsAndUsers = _postsAndUsers.value
        val searchText = _searchText.value
        if(searchText.isNotBlank()) {
            val filteredPostsAndUsers = originalPostsAndUsers.filter { tempItem ->
                when (filterState.value) {
                    ExploreSearchOption.BY_NAME -> tempItem.second.nickname.contains(
                        searchText,
                        ignoreCase = true
                    )

                    ExploreSearchOption.BY_POST -> tempItem.first.description.contains(
                        searchText,
                        ignoreCase = true
                    )

                    else -> false
                }
            }

            _postsAndUsers.value = filteredPostsAndUsers
        }
        else{
            getPosts()
        }
    }

    fun onPullToRefreshTriggered() {
        getPosts()
    }

    private fun getPosts() {

        val tempPostAndUser = mutableListOf<Pair<Post, User>>()

        FirebaseDatabase
            .getInstance()
            .getReference(postReferenceName)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {val postList = mutableListOf<Post>()

                        // 1. 모든 Post 데이터를 수집
                        for (childSnapshot in snapshot.children) {
                            val postWrapper = childSnapshot.getValue(PostWrapper::class.java)
                            if (postWrapper != null) {
                                postList.add(Post(postWrapper))
                            }
                        }

                        // 2. Post와 연관된 User 데이터를 비동기로 가져옴
                        val userFetchTasks = postList.map { post ->
                            FirebaseDatabase.getInstance()
                                .getReference(userReferenceName)
                                .child(post.userId)
                                .get()
                                .continueWith { task ->
                                    if (task.isSuccessful) {
                                        val userWrapper = task.result.getValue(UserWrapper::class.java)
                                        if (userWrapper != null) {
                                            Pair(post, User(userWrapper))
                                        } else {
                                            null
                                        }
                                    } else {
                                        null
                                    }
                                }
                        }

                        // 3. 모든 작업 완료 후 결과 처리
                        Tasks.whenAllComplete(userFetchTasks)
                            .addOnCompleteListener {
                                val result = userFetchTasks.mapNotNull { it.result }
                                tempPostAndUser.addAll(result)
                                _postsAndUsers.value = tempPostAndUser
                            }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ExploreViewModel", "Database error: ${error.message}")
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
//                    Log.d("ExploreViewModel", "getUserFromDB Success: $data")
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