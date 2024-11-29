package com.imeanttobe.drawapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.ChatList
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.view.bottomnav.ChatListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

    // Variables
    // TODO: have to get firebase instances here
    private val _chatLists = MutableStateFlow<List<ChatList>>(emptyList()) // This is for chat lists received from firebase

    // Getter
    val chatLists = _chatLists.asStateFlow() // This is for chat lists received from firebase

    init {
        // TODO: this is for sample messages and have to replaced or removed when firebase is applied
        viewModelScope.launch {
            _chatLists.emit(
                listOf(
                    ChatList(
                        id = 0,
                        userName = "나",
                        opponentName = "상대",
                        opponentId = 0,
                        opponentType = UserType.WEBTOON_ARTIST,
                        lastMessage = "내일 10시에 뵈어요.",
                        opponentImageUrl = ""
                    )
                ) * 20
            )
        }
    }

    // Methods

}