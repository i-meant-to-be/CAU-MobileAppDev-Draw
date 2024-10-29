package com.imeanttobe.drawapplication.view.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.viewmodel.ChatDetailViewModel

@Composable
fun ChatDetailView(
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn {
                items(10) {
                    ChatBubble(
                        message = "message",
                        isMine = it % 2 == 0
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: String,
    isMine: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                .padding(5.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ChatTextFieldContainer(
    text: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        ChatTextField()
        ChatSendButton()
    }
}

@Composable
fun ChatTextField() {

}

@Composable
fun ChatSendButton() {

}