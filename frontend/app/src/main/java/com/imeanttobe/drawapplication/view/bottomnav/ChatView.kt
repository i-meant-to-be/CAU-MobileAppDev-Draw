package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
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
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(10) { index ->
                ChatListItem(
                    image = painterResource(id = R.drawable.joker),
                    username = "사용자 이름",
                    type = "웹툰 작가",
                    lastMessage = "내일 10시에 뵈어요.",
                    isLastItem = index == 9
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    image: Painter,
    username: String,
    type: String,
    lastMessage: String,
    isLastItem: Boolean = false
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = if (isLastItem) 0.dp else 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable {}
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatListItemProfileImage(
            image = image
        )
        Spacer(modifier = Modifier.padding(end = 10.dp))
        ChatListItemUserDataText(
            username = username,
            type = type,
            lastMessage = lastMessage
        )
    }
}

@Composable
fun ChatListItemProfileImage(
    image: Painter
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun ChatListItemUserDataText(
    username: String,
    type: String,
    lastMessage: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = type,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Text(
            text = lastMessage,
            style = MaterialTheme.typography.bodySmall
        )
    }
}