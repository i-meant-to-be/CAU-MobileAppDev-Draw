package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.viewmodel.ChatViewModel

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    // This composable is placed on Surface,
    // because this can't be displayed alone but need to be displayed upon Scaffold
    // which contains bottom navigation bar. (BottomNavHostView)
    Surface(modifier = modifier) {
        LazyColumn {
            items(10) {
                ChatListItem(
                    image = Icons.Default.Dining,
                    username = "username",
                    type = "type",
                    lastMessage = "lastMessage"
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    image: ImageVector,
    username: String,
    type: String,
    lastMessage: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ChatListItemProfileImage(
            image = image
        )
        ChatListItemUserDataText(
            username = username,
            type = type,
            lastMessage = lastMessage
        )
    }
}

@Composable
fun ChatListItemProfileImage(
    image: ImageVector
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
    ) {
        Image(
            imageVector = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ChatListItemUserDataText(
    username: String,
    type: String,
    lastMessage: String
) {
    Column() {
        Text(
            text = username,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = type,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Text(
            text = lastMessage,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}