package com.imeanttobe.drawapplication.view.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.data.model.ChatItem
import com.imeanttobe.drawapplication.viewmodel.ChatDetailViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

operator fun <T> Iterable<T>.times(count: Int): List<T> = List(count) { this }.flatten()

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatDetailView(
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val drawerPadding = animateDpAsState(
        targetValue = if (viewModel.drawerState.value) (LocalConfiguration.current.screenWidthDp).dp else 0.dp,
        label = "DrawerPadding"
    )

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            ChatDetailViewBottomBar(
                text = viewModel.message.value,
                onValueChange = viewModel::setMessage,
                onSendClick = viewModel::send
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            ChatDetailViewTopBar(
                opponentNickname = viewModel.opponentNickname.value,
                navigateUp = navigateUp,
                setDrawerState = { viewModel.setDrawerState(true) }
            )

            ChatDetailViewBody(
                modifier = Modifier.fillMaxSize(),
                // TODO: chatItems = listOf(),
            )
        }
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
    ) * 30
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        listState.scrollToItem(listState.layoutInfo.totalItemsCount - 1)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
            state = listState,
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(chatItems) { index, item ->
                ChatBubble(
                    modifier = Modifier.padding(
                        top = if (index == 0) 10.dp else 0.dp,
                        bottom = if (index == chatItems.lastIndex) 10.dp else 0.dp
                    ),
                    message = item.message,
                    isMine = index % 2 == 0
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailViewTopBar(
    modifier: Modifier = Modifier,
    opponentNickname: String,
    navigateUp: () -> Unit,
    setDrawerState: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
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
                onClick = { setDrawerState() }
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
    modifier: Modifier = Modifier,
    message: String,
    isMine: Boolean
) {
    val backgroundColor = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = modifier
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

@ExperimentalLayoutApi
@Composable
fun ChatDetailViewBottomBar(
    text: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val imeIncludedPadding = animateDpAsState(
        targetValue = if (WindowInsets.isImeVisible) 0.dp else 10.dp,
        label = "ChatDetailViewBottomBarImePadding"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = imeIncludedPadding.value)
            .padding(10.dp)
            .imePadding(),
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

@Composable
fun ChatTextField(
    text: String,
    onValueChange: (String) -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.surfaceDim

    BasicTextField(
        textStyle = MaterialTheme.typography.bodyLarge,
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(contentColor),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .size(width = 320.dp, height = 40.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = backgroundColor)
                    .padding(vertical = 5.dp, horizontal = 15.dp),
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
    val contentColor = MaterialTheme.colorScheme.onBackground

    IconButton(
        modifier = Modifier.size(32.dp),
        onClick = onSendClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = null,
            tint = contentColor
        )
    }
}