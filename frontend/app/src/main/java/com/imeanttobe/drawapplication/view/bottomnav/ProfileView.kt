package com.imeanttobe.drawapplication.view.bottomnav

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.theme.onKeyColor
import com.imeanttobe.drawapplication.theme.keyColor1
import com.imeanttobe.drawapplication.theme.keyColor2
import com.imeanttobe.drawapplication.theme.keyColor3
import com.imeanttobe.drawapplication.view.register.RadioButtonSet
import com.imeanttobe.drawapplication.viewmodel.ProfileViewModel

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val posts = viewModel.userPosts.collectAsState()
    val user = viewModel.user.collectAsState()

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileCard(
                modifier = Modifier,
                user = user.value,
                navigateToLogin = navigateToLogin,
                setDialogState = viewModel::setDialogState,
                signOut = viewModel::signOut
            )
            ProfileViewGrid(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                posts = posts.value,
                onImageClick = { postId, imageUri, description ->
                    viewModel.setDialogState(3)
                    viewModel.setPictureDialogData(postId, imageUri, description)
                }
            )
        }
    }

    // Edit user profile dialog
    if (viewModel.dialogState.value == 1) {
        UpdateUserDataDialog(
            onDismiss = {
                viewModel.setDialogState(0)
            },
            user = user.value,
            onUpdate = { newUser ->
                viewModel.updateUser(newUser)
                viewModel.setDialogState(0)
            }
        )
    }

    // Add new picture dialog
    if (viewModel.dialogState.value == 2) {
        NewPictureDialog(
            onDismiss = { viewModel.setDialogState(0) },
            onAddNewPicture = { uri, description ->
                viewModel.addPost(
                    uri = uri,
                    description = description,
                    context = context
                )
            }
        )
    }

    var showOkCancelDialog by rememberSaveable { mutableStateOf(false) }

    // Picture dialog
    if (viewModel.dialogState.value == 3) {
        PictureDialog(
            setDialogState = { newValue -> viewModel.setDialogState(newValue) },
            description = viewModel.currentPictureDescription.value,
            imageUri = viewModel.currentPictureUri.value,
            onDeleteClicked = {showOkCancelDialog = true}
        )
    }

    if(showOkCancelDialog){
        AlertDialog(
            onDismissRequest = { showOkCancelDialog = false },
            title = { Text("그림 삭제") },
            text = { Text("정말로 그림을 삭제하시겠습니까?") },
            confirmButton = {
                Button(onClick = {
                    showOkCancelDialog = false
                    viewModel.deletePost()
                }) {
                    Text("삭제")
                }
            },
            dismissButton = {
                Button(onClick = { showOkCancelDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
fun ProfileViewGrid(
    modifier: Modifier = Modifier,
    posts: List<Post>,
    onImageClick: (String, Uri, String) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        items(items = posts) { post ->
            ProfileViewImageItem(
                post = post,
                onImageClick = {postId, imageUri, description -> onImageClick(postId,imageUri, description) }
            )
        }
    }
}

@Composable
fun ProfileViewImageItem(
    post: Post,
    onImageClick: (String, Uri, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick(post.id, post.imageUri, post.description) }
    ) {
        Log.d("ProfileView", post.imageUri.toString())

        if (post.imageUri != Uri.EMPTY) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(post.imageUri)
                    .build(),
                contentDescription = "Selected image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.paintimage),
                contentDescription = "Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable // 프로필 카드 컴포저블
fun ProfileCard(
    modifier: Modifier,
    user: User?,
    navigateToLogin: ()-> Unit,
    setDialogState: (Int) -> Unit,
    signOut: () -> Unit
) {
    val buttonContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val buttonContentColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Log.d("ProfileView", user?.profilePhotoUri.toString())

        // Profile photo
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(user?.profilePhotoUri)
                .build(),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 10.dp)
                .size(100.dp) // 이미지 크기 64dp로 설정
                .clip(CircleShape), // 이미지를 원형으로 자름
            onError = {
                // 이미지 로딩 실패 시 로그 출력
                Log.e("AsyncImage", "Image load failed $it")
            }
        )
        Spacer(Modifier.height(10.dp))

        // Nickname
        Text(
            text = user?.nickname ?: stringResource(id = R.string.error_nickname),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(0.dp))

        // Introduce
        Text(
            text = user?.introduce ?: stringResource(id = R.string.error_introduce),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Usertype
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            UserTypeLabel(userType = user?.type ?: UserType.UNDEFINED)
            Spacer(modifier = Modifier.width(10.dp))
            InstagramButton(
                instagramId = user?.instagramId ?: "",
                userType = user?.type ?: UserType.UNDEFINED
            )
        }
        Spacer(Modifier.height(24.dp))

        // Profile buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Edit button
            Button(
                onClick = { setDialogState(1) },
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonContentColor
                )
            ) {
                Text(
                    text = stringResource(id = R.string.modify_information)
                )
            }

            // New picture button
            Button(
                onClick = { setDialogState(2) },
                modifier = Modifier.size(width = 110.dp, height = 40.dp),
                shape = RoundedCornerShape(100.dp), // 버튼 모양 설정
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonContentColor
                )
            ) {
                Text(
                    text = stringResource(id = R.string.register_picture)
                )
            }

            // Logout button
            Button(
                onClick = {
                    signOut()
                    navigateToLogin()
                },
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(100.dp), // 버튼 모양 설정
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonContentColor
                )
            ) {
                // Text(text = stringResource(id = R.string.logout), fontSize = 10.sp)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout button"
                )
            }
        }
    }
}

@Composable
fun InstagramButton(
    instagramId: String,
    userType: UserType
) {
    val containerColor = when (userType) {
        UserType.WEBTOON_ARTIST -> keyColor2
        UserType.ASSIST_ARTIST -> keyColor3
        else -> keyColor1
    }
    val contentColor = onKeyColor

    val context = LocalContext.current
    val uri = Uri.parse("http://instagram.com/_u/$instagramId")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.instagram.android")

    Icon(
        painter = painterResource(id = R.drawable.instagram_logo), // 원하는 아이콘으로 변경
        contentDescription = "인스타그램 아이디 아이콘", // 접근성을 위한 설명
        tint = contentColor,
        modifier = Modifier
            .size(30.dp)
            .background(color = containerColor, shape = CircleShape)
            .padding(6.dp)
            .clickable {
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            }
    )
}

@Composable
fun PictureDialog(
    setDialogState: (Int) -> Unit,
    description: String,
    imageUri: Uri?,
    onDeleteClicked: ()->Unit
) {
    var scale by rememberSaveable { mutableFloatStateOf(1f) }
    var offsetX by rememberSaveable { mutableFloatStateOf(0f) }
    var offsetY by rememberSaveable { mutableFloatStateOf(0f) }
    var cardWidth by rememberSaveable { mutableFloatStateOf(0f) }
    var cardHeight by rememberSaveable { mutableFloatStateOf(0f) }
    var imageWidth by rememberSaveable { mutableFloatStateOf(0f) }
    var imageHeight by rememberSaveable { mutableFloatStateOf(0f) }
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Dialog(
        onDismissRequest = { setDialogState(0) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            IconButton(
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.End),
                onClick = onDeleteClicked,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Image",
                    modifier = Modifier
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
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
                        if (imageUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(imageUri)
                                    .build(),
                                contentDescription = "Selected image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .background(color = backgroundColor)
                                    .onGloballyPositioned { coordinates ->
                                        imageWidth = coordinates.size.width.toFloat()
                                        imageHeight = coordinates.size.height.toFloat()
                                    },
                            )
                        } else {
                            Image(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        imageWidth = coordinates.size.width.toFloat()
                                        imageHeight = coordinates.size.height.toFloat()
                                    }
                            )
                        }
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
}

@Composable
fun UserTypeLabel(userType: UserType) {
    val containerColor = when (userType) {
        UserType.WEBTOON_ARTIST -> keyColor2
        UserType.ASSIST_ARTIST -> keyColor3
        else -> keyColor1
    }
    val contentColor = onKeyColor

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
            text = when (userType) {
                UserType.WEBTOON_ARTIST -> stringResource(id = R.string.usertype_webtoon_artist)
                UserType.ASSIST_ARTIST -> stringResource(id = R.string.usertype_assist_artist)
                UserType.ADMIN -> stringResource(id = R.string.usertype_admin)
                else -> stringResource(id = R.string.error_user_type)
            },
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = contentColor
        )
    }
}

@Composable
fun NewPictureDialog(
    onDismiss: () -> Unit,
    onAddNewPicture: (Uri, String) -> Unit
) {
    val context = LocalContext.current
    var pictureUri by rememberSaveable { mutableStateOf(Uri.EMPTY) }
    var description by rememberSaveable { mutableStateOf("") }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            pictureUri = uri
        }
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(30.dp)
            ) {
                // Image area
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.add_picture),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.add_your_picture),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (pictureUri != Uri.EMPTY) {
                        AsyncImage(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { imagePicker.launch("image/*") },
                            model = ImageRequest.Builder(context)
                                .data(pictureUri)
                                .build(),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "Picture"
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                                .clickable { imagePicker.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_picture),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

                // Description area
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.picture_description),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.enter_picture_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { newValue -> description = newValue },
                    placeholder = { Text(text = stringResource(id = R.string.example_picture_description)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1),
                    minLines = 1,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Button area
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = keyColor1,
                        contentColor = onKeyColor
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onAddNewPicture(pictureUri, description)
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.add_picture))
                }
            }
        }
    }
}

@Composable
fun UpdateUserDataDialog(
    onDismiss: () -> Unit,
    onUpdate: (User) -> Unit,
    user: User?
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var pictureUri by rememberSaveable { mutableStateOf(user?.profilePhotoUri ?: Uri.EMPTY) }
    var newNickname by rememberSaveable { mutableStateOf(user?.nickname ?: "") }
    var newInstagramId by rememberSaveable { mutableStateOf(user?.instagramId ?: "") }
    var newIntroduce by rememberSaveable { mutableStateOf(user?.introduce ?: "") }
    var newUserType by rememberSaveable { mutableStateOf(user?.type!!) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                pictureUri = uri
            }
        }
    )

    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(30.dp).verticalScroll(scrollState)
            ) {
                // Image area
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.edit_profile_photo),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.edit_your_profile_photo),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (pictureUri != Uri.EMPTY) {
                        AsyncImage(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { imagePicker.launch("image/*") },
                            model = ImageRequest.Builder(context)
                                .data(pictureUri)
                                .build(),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Picture"
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                                .clickable { imagePicker.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_picture),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

                // Nickname area
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.nickname),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.enter_your_nickname),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newNickname,
                    onValueChange = { newValue -> newNickname = newValue },
                    placeholder = { Text(text = stringResource(id = R.string.example_nickname)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1),
                    minLines = 1,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Instagram area
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.instagram_account),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.enter_your_instagram_id),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newInstagramId,
                    onValueChange = { newValue -> newInstagramId = newValue },
                    placeholder = { Text(text = stringResource(id = R.string.instagram_account)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1),
                    minLines = 1,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Introduce area
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.introduce),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.enter_your_introduce),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newIntroduce,
                    onValueChange = { newValue -> newIntroduce = newValue },
                    placeholder = { Text(text = stringResource(id = R.string.example_introduce)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1),
                    minLines = 1,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(30.dp))

                // User type area
                Text(
                    modifier = Modifier
                        .align(alignment = Alignment.Start),
                    text = String.format(
                        "%s",
                        stringResource(id = R.string.usertype)
                    ),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.choose_your_usertype),
                    style = MaterialTheme.typography.bodyMedium
                )
                RadioButtonSet(
                    modifier = Modifier
                        .align(alignment = Alignment.Start),
                    selectedOption = newUserType,
                    onChange = { newValue -> newUserType = newValue }
                )
                Spacer(modifier = Modifier.height(30.dp))


                // Button area
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = keyColor1,
                        contentColor = onKeyColor
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onUpdate(
                            user!!.copy(
                                profilePhotoUri = pictureUri,
                                nickname = newNickname,
                                instagramId = newInstagramId,
                                introduce = newIntroduce,
                                type = newUserType
                            )
                        )
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.edit_profile))
                }
            }
        }
    }
}