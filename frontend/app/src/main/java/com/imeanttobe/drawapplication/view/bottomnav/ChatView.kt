package com.imeanttobe.drawapplication.view.bottomnav

import android.net.Uri
import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.data.model.ChatSession
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.theme.keyColor1
import com.imeanttobe.drawapplication.viewmodel.ChatViewModel

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
    navigateTo: (String) -> Unit
) {
    val sessionAndUserList = viewModel.sessionAndUser.collectAsState()
    val loadingState = viewModel.loadingState.collectAsState()

    Surface(modifier = modifier) {
        if (loadingState.value is Resource.Loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    color = keyColor1
                )
            }
        }
        else if (sessionAndUserList.value.isEmpty() || loadingState.value is Resource.Error) {
            // If chat list is empty, show empty chat view
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
            // If chat list is not empty, show enable chat sessions
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(sessionAndUserList.value) { index, sessionAndUser ->
                    ChatSessionItem(
                        modifier = Modifier.padding(
                            top = if (index == 0) 10.dp else 0.dp,
                            bottom = if (index == sessionAndUserList.value.lastIndex) 10.dp else 0.dp
                        ),
                        sessionAndUser = sessionAndUser,
                        onClick = { navigateTo("${NavItem.ChatDetailItem.route}/sessionId=${sessionAndUser.first.id}/opponentNickname=${sessionAndUser.second.nickname}") }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatSessionItem(
    modifier: Modifier = Modifier,
    sessionAndUser: Pair<ChatSession, User>,
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
        ChatSessionItemProfileImage(profilePhotoUri = sessionAndUser.second.profilePhotoUri)
        Spacer(modifier = Modifier.padding(end = 10.dp))
        ChatListItemUserDataText(
            userName = sessionAndUser.second.nickname,
            chatSession = sessionAndUser.first,
            opponentUserType = sessionAndUser.second.type
        )
    }
}

@Composable
fun ChatSessionItemProfileImage(profilePhotoUri: Uri) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(profilePhotoUri)
                .build(),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onError = {
                // 이미지 로딩 실패 시 로그 출력
                Log.e("AsyncImage", "Image load failed $it")
            }
        )
    }
}

@Composable
fun ChatListItemUserDataText(
    chatSession: ChatSession,
    opponentUserType: UserType,
    userName: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = when(opponentUserType) {
                    UserType.WEBTOON_ARTIST -> stringResource(id = R.string.usertype_webtoon_artist)
                    UserType.ASSIST_ARTIST -> stringResource(id = R.string.usertype_assist_artist)
                    else -> stringResource(id = R.string.usertype_undefined)
                },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Text(
            text = if (chatSession.lastMessage.isEmpty()) stringResource(id = R.string.no_message) else chatSession.lastMessage,
            maxLines = 1,
            minLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}