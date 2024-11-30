package com.imeanttobe.drawapplication.view.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.model.ChatItem
import com.imeanttobe.drawapplication.viewmodel.ChatDetailViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatDetailView(
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val messages = viewModel.messages.collectAsState()

    // Variables for drawer implementation
    val deviceWidth = LocalConfiguration.current.screenWidthDp.dp
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val drawerOffset = animateDpAsState(
        targetValue = if (viewModel.drawerState.value) 200.dp else 0.dp,
        label = "ChatDetailViewDrawerOffset"
    )
    val drawerAlpha = animateFloatAsState(
        targetValue = if (viewModel.drawerState.value) 0.5f else 0f,
        label = "ChatDetailViewDrawerAlpha"
    )

    // Drawer wrapper
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { viewModel.setDrawerState(false) }
            )
    ) {
        // Main content
        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {
                ChatDetailViewBottomBar(
                    text = viewModel.textFieldMessage.value,
                    onValueChange = viewModel::setTextFieldMessage,
                    focusRequester = focusRequester,
                    onSendClick = {
                        viewModel.send()
                        viewModel.setTextFieldMessage("")
                    }
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
                    setDrawerState = {
                        viewModel.setDrawerState(true)
                        focusManager.clearFocus()
                    }
                )

                ChatDetailViewBody(
                    modifier = Modifier.fillMaxSize(),
                    chatItems = messages.value
                )
            }
        }

        // Box for screen hiding when drawer is open
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background.copy(alpha = drawerAlpha.value))
        )

        // Drawer content
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(min = 200.dp)
                .offset(x = deviceWidth - drawerOffset.value)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = viewModel.drawerState.value,
                    onClick = { }
                )
                .windowInsetsPadding(WindowInsets.systemBars)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            ChatDetailViewDrawer(
                onExit = {}
            )
        }
    }
}

@Composable
fun ChatDetailViewDrawer(
    onExit: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(id = R.string.menu),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(20.dp)
        )

        Surface(
            modifier = Modifier
                .widthIn(max = 200.dp)
                .fillMaxWidth()
                .clickable {}
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = null
                )

                Text(
                    text = stringResource(id = R.string.exit_chat)
                )
            }
        }
    }
}

@Composable
fun ChatDetailViewBody(
    modifier: Modifier = Modifier,
    chatItems: List<ChatItem>
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
                    isMine = index % 2 == 0 // TODO: have to set the logic that determines whether the message is mine or not
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
    onSendClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        ChatTextField(
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
            ,
            text = text,
            onValueChange = onValueChange
        )
        ChatSendButton(
            modifier = Modifier.padding(horizontal = 5.dp),
            onSendClick = onSendClick
        )
    }
}

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit
) {
    // Colors for text field's content and its background
    val contentColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest

    BasicTextField(
        modifier = Modifier
            .heightIn(min = 36.dp)
            .then(modifier),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        // Defines color of blinking bar on the text field (looks like |)
        // Since cursor is also a part of the text field, it is better to set its color as content's color
        cursorBrush = SolidColor(contentColor),
        // Define text field's outfit on this variable
        // Composable function 'innerTextField()' is text field and you can wrap it with other composables
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = backgroundColor)
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun ChatSendButton(
    modifier: Modifier = Modifier,
    onSendClick: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onBackground

    IconButton(
        modifier = modifier.size(32.dp),
        onClick = onSendClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = null,
            tint = contentColor
        )
    }
}