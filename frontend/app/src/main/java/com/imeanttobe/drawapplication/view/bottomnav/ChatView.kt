package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.model.ChatSession
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.viewmodel.ChatViewModel

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
    navigateTo: (String) -> Unit
) {
    val chatLists = viewModel.sessions.collectAsState().value

    // This composable is placed on Surface,
    // because this can't be displayed alone but need to be displayed upon Scaffold
    // which contains bottom navigation bar. (BottomNavHostView)
    Surface(modifier = modifier) {
        if (chatLists.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.chat_is_empty),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(chatLists) { index, chatList ->
                    ChatListItem(
                        modifier = Modifier.padding(
                            top = if (index == 0) 10.dp else 0.dp,
                            bottom = if (index == chatLists.lastIndex) 10.dp else 0.dp
                        ),
                        chatSession = chatList,
                        onClick = { navigateTo(NavItem.ChatDetailItem.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatListItem(
    modifier: Modifier = Modifier,
    chatSession: ChatSession,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatListItemProfileImage(imageUrl = "")
        Spacer(modifier = Modifier.padding(end = 10.dp))
        ChatListItemUserDataText(chatList = chatSession)
    }
}

@Composable
fun ChatListItemProfileImage(
    imageUrl: String
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Image(
            // TODO: have to load image here by Coil library
            painter = painterResource(id = R.drawable.joker),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun ChatListItemUserDataText(
    chatList: ChatSession
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                /*
                TODO:
                text = when(chatList.opponentType) {
                    UserType.ADMIN -> stringResource(id = R.string.usertype_admin)
                    UserType.WEBTOON_ARTIST -> stringResource(id = R.string.usertype_webtoon_artist)
                    UserType.ASSIST_ARTIST -> stringResource(id = R.string.usertype_assist_artist)
                },

                 */
                text = stringResource(id = R.string.usertype_admin),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Text(
            text = chatList.lastMessage,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}