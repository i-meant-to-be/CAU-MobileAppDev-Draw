package com.imeanttobe.drawapplication.view.bottomnav

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.ExploreSearchOption
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.viewmodel.ExploreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreView(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val postsAndUsers = viewModel.postsAndUsers.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(2000)
            viewModel.onPullToRefreshTriggered()
            isRefreshing = false
        }
    }

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ExploreViewSearchBox(
                searchText = viewModel.searchText.value,
                onSearchTextChange = { newValue -> viewModel.setSearchText(newValue) },
                expanded = viewModel.expanded.value,
                setExpanded = { newValue -> viewModel.setExpanded(newValue) },
                setSearchOption = { newValue -> viewModel.setFilterState(newValue) },
                searchOption = viewModel.filterState.value,
                search = viewModel::search
            )
            PullToRefreshBox(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ) {
                ExploreViewGrid(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    isDialogOpen = viewModel.dialogState.value,
                    setDialogState = { newValue -> viewModel.setDialogState(newValue) },
                    postsAndUsers = postsAndUsers.value,
                    viewModel = viewModel,
                    navController= navController
                )
            }
        }
    }
}

@Composable
fun ExploreViewSearchBox(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    setSearchOption: (ExploreSearchOption) -> Unit,
    searchOption: ExploreSearchOption,
    search: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        ExploreViewSearchBoxTextField(
            text = searchText,
            onValueChange = onSearchTextChange,
            expanded = expanded,
            setExpanded = setExpanded,
            setSearchOption = setSearchOption,
            searchOption = searchOption,
            search = search
        )
    }
}

@Composable
fun ExploreViewGrid(
    modifier: Modifier = Modifier,
    isDialogOpen: Boolean,
    setDialogState: (Boolean) -> Unit,
    postsAndUsers: List<Pair<Post,User>>,
    viewModel: ExploreViewModel,
    navController: NavController
) {
    var dialogDescription by rememberSaveable { mutableStateOf("") }
    var dialogImgUri by rememberSaveable { mutableStateOf(Uri.EMPTY) }

    if (postsAndUsers.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Image,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.post_is_empty),
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(postsAndUsers) { postAndUser ->
                ExploreViewGridItem(
                    postAndUser = postAndUser,
                    onImageClick = {
                        dialogDescription = postAndUser.first.description
                        setDialogState(true)
                        dialogImgUri = postAndUser.first.imageUri
                    },
                    navigateTo = { route -> navController.navigate(route) }
                )
            }
        }

        if (isDialogOpen) {
            ExploreViewImageDialog(
                setDialogState = { setDialogState(false) },
                description = dialogDescription,
                imageUri = dialogImgUri
            )
        }
    }
}

@Composable
fun ExploreViewImageDialog(
    setDialogState: (Boolean) -> Unit,
    description: String,
    imageUri: Uri
) {
    var scale by rememberSaveable { mutableFloatStateOf(1f) }
    var offsetX by rememberSaveable { mutableFloatStateOf(0f) }
    var offsetY by rememberSaveable { mutableFloatStateOf(0f) }
    var cardWidth by rememberSaveable { mutableFloatStateOf(0f) }
    var cardHeight by rememberSaveable { mutableFloatStateOf(0f) }
    var imageWidth by rememberSaveable { mutableFloatStateOf(0f) }
    var imageHeight by rememberSaveable { mutableFloatStateOf(0f) }
    val backgroundColor = MaterialTheme.colorScheme.surface

    Dialog(
        onDismissRequest = { setDialogState(false) }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(color = backgroundColor)
                        .onGloballyPositioned { coordinates ->
                            cardWidth = coordinates.size.width.toFloat()
                            cardHeight = coordinates.size.height.toFloat()
                        }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 3f)

                                val maxOffsetX = (cardWidth * (scale - 1f) / 2f)
                                val maxOffsetY = (cardHeight * (scale - 1f) / 2f)

                                offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)
                                offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                            }
                        }
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        ),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                            .onGloballyPositioned { coordinates ->
                                imageWidth = coordinates.size.width.toFloat()
                                imageHeight = coordinates.size.height.toFloat()
                            },
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(imageUri)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = backgroundColor)
                        .padding(10.dp),
                    text = description,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ExploreViewSearchBoxTextField(
    text: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    setSearchOption: (ExploreSearchOption) -> Unit,
    searchOption: ExploreSearchOption,
    search: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
    val textFieldColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val filterColor = MaterialTheme.colorScheme.surfaceContainerLowest
    val icon = if (searchOption == ExploreSearchOption.BY_NAME) Icons.Default.PersonSearch else Icons.Default.ImageSearch
    val stringResId = if (searchOption == ExploreSearchOption.BY_NAME) R.string.search_by_name_dot else R.string.search_by_post_dot

    BasicTextField(
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            search()
        }),
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = textFieldColor)
                    .padding(vertical = 5.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = contentColor
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(id = stringResId),
                            style = MaterialTheme.typography.bodyLarge.copy(color = contentColor)
                        )
                    }
                }

                Box() {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(color = filterColor)
                            .clickable { setExpanded(true) }
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter Icon",
                            tint = contentColor
                        )

                        Icon(
                            imageVector = icon,
                            contentDescription = "Filter Icon",
                            tint = contentColor
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { setExpanded(false) }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.search_by_name)) },
                            onClick = {
                                setSearchOption(ExploreSearchOption.BY_NAME)
                                setExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.search_by_post)) },
                            onClick = {
                                setSearchOption(ExploreSearchOption.BY_POST)
                                setExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ExploreViewGridItem(
    postAndUser: Pair<Post, User>,
    onImageClick: () -> Unit,
    navigateTo: (String) -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onSurface
    val containerColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExploreViewUserInfoItem(
                userName = postAndUser.second.nickname,
                userType = postAndUser.second.type,
                userImageUrl = postAndUser.second.profilePhotoUri.toString(),
                contentColor = contentColor,
                onClick = {
                    if (postAndUser.second.id != FirebaseAuth.getInstance().currentUser!!.uid) {
                        navigateTo("${NavItem.UserProfileViewItem.route}/userId=${postAndUser.second.id}")
                    }
                }
            )
            ExploreViewImageItem(
                post = postAndUser.first,
                imageUri = postAndUser.first.imageUri,
                contentColor = contentColor,
                onImageClick = onImageClick
            )
        }
    }
}

@Composable
fun ExploreViewUserInfoItem(
    userName: String,
    userType: UserType,
    userImageUrl: String,
    contentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(userImageUrl)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Column() {
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyLarge.copy(color = contentColor)
            )
            Text(
                text = when(userType) {
                    UserType.ADMIN -> stringResource(id = R.string.usertype_admin)
                    UserType.WEBTOON_ARTIST -> stringResource(id = R.string.usertype_webtoon_artist)
                    UserType.ASSIST_ARTIST -> stringResource(id = R.string.usertype_assist_artist)
                    UserType.UNDEFINED -> stringResource(id = R.string.usertype_undefined)
                },
                style = MaterialTheme.typography.bodySmall.copy(color = contentColor)
            )
        }
    }
}

@Composable
fun ExploreViewImageItem(
    post: Post,
    imageUri: Uri,
    contentColor: Color,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick() }
    ) {
        if (imageUri != Uri.EMPTY) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = MaterialTheme.colorScheme.primary),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .build(),
                contentScale = ContentScale.FillWidth,
                contentDescription = "Image"
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = MaterialTheme.colorScheme.error),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Image",
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier
                        .size(50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(id = R.string.no_image),
                    color = MaterialTheme.colorScheme.onError,
                )
            }
        }
        Text(
            text = post.description,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(color = contentColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}