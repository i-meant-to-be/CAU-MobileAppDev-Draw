package com.imeanttobe.drawapplication.view.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.data.model.ChatItem
import com.imeanttobe.drawapplication.viewmodel.ChatDetailViewModel
import com.imeanttobe.drawapplication.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime

operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

@Composable
fun ChatDetailView(
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val copyMessage = LocalContext.current.resources.getString(R.string.message_copied)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ChatDetailViewTopBar(
                opponentNickname = viewModel.opponentNickname.value,
                navigateUp = navigateUp
            )
        },
        bottomBar = {
            ChatDetailViewBottomBar(
                text = viewModel.message.value,
                onValueChange = viewModel::setMessage,
                onSendClick = viewModel::send
            )
        }
    ) { innerPadding ->
        ChatDetailViewBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            // TODO: chatItems = listOf(),
            showSnackbar = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = copyMessage,
                        withDismissAction = true
                    )
                }
            }
        )
    }
}

@Composable
fun ChatDetailViewBody(
    modifier: Modifier = Modifier,
    chatItems: List<ChatItem> = listOf(
        ChatItem(
            message = "message",
            chatListId = -1,
            opponentName = "",
            userName = "",
            datetime = LocalDateTime.now()
        )
    ) * 10,
    showSnackbar: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(chatItems) { index, item ->
                ChatBubble(
                    message = item.message,
                    isMine = index % 2 == 0,
                    showSnackbar = showSnackbar
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailViewTopBar(
    opponentNickname: String,
    navigateUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = opponentNickname) },
        navigationIcon = {
            IconButton(
                onClick = navigateUp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    message: String,
    isMine: Boolean,
    showSnackbar: () -> Unit
) {
    val backgroundColor = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val contentColor = if (isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(color = backgroundColor)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        clipboardManager.setText(AnnotatedString(message))

                    }
                )
                .padding(10.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(color = contentColor)
            )
        }
    }
}

@Composable
fun ChatDetailViewBottomBar(
    text: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ChatTextField(
                text = text,
                onValueChange = onValueChange
            )
            ChatSendButton(
                onSendClick = onSendClick
            )
        }
    }
}

@Composable
fun ChatTextField(
    text: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .size(width = 320.dp, height = 30.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun ChatSendButton(
    onSendClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(32.dp),
        onClick = onSendClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = null
        )
    }
}