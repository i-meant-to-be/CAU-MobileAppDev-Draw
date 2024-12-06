
package com.imeanttobe.drawapplication.view.bottomnav

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.theme.onSeed
import com.imeanttobe.drawapplication.theme.seed
import com.imeanttobe.drawapplication.viewmodel.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileView(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    returnTo: () -> Unit,
    navBackStackEntry: NavBackStackEntry

) {
    val context = LocalContext.current
    val posts = viewModel.userPosts.collectAsState()
    val user = viewModel.user.collectAsState()
    val usernickname = navBackStackEntry.arguments?.getString("usernickname")
    val userType = navBackStackEntry.arguments?.getString("userType")



    LaunchedEffect(usernickname, userType) {
        viewModel.setUserProfileData(usernickname, userType)
    }



    Surface(modifier = modifier) {

        TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = returnTo) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UserProfileCard(
                modifier = Modifier,
                user = user.value
            )
            UserProfileViewGrid(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                posts = posts.value,
                onImageClick = { imageUri, description ->{}}
            )
        }
    }

    if (viewModel.dialogState.value == 3) {
        PictureDialog(
            setDialogState = { newValue -> viewModel.setDialogState(newValue) },
            description = viewModel.currentPictureDescription.value,
            imageUri = viewModel.currentPictureUri.value
        )
    }
}
@Composable
fun UserProfileViewGrid(
        modifier: Modifier = Modifier,
        posts: List<Post>,
        onImageClick: (Uri, String) -> Unit
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            items(items = posts) { post ->
                UserProfileViewImageItem(
                    post = post,
                    onImageClick = { imageUri, description -> onImageClick(imageUri, description) }
                )
            }
        }
    }

    @Composable
    fun UserProfileViewImageItem(
        post: Post,
        onImageClick: (Uri, String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onImageClick(post.imageUri, post.description) }
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
    fun UserProfileCard(
        modifier: Modifier,
        user : User?

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
            if (user != null) {
                Text(
                    text = user.nickname ?: stringResource(id = R.string.error_nickname),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
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
                UserLabel(userType = user?.type ?: UserType.UNDEFINED)
                Spacer(modifier = Modifier.width(10.dp))
                InstagramButton(
                    instagramId = user?.instagramId ?: "",
                    userType = user?.type ?: UserType.UNDEFINED
                )
            }
            Spacer(Modifier.height(24.dp))

            Button(onClick = {},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonContentColor)//containerColor = seed, contentColor = onSeed
            ) {
                Text(text = stringResource(id = R.string.send_message))
            }

            // Profile buttons

            }
        }


    @Composable
    fun UserInstagramButton(
        instagramId: String,
        userType: UserType
    ) {
        val containerColor = when (userType) {
            UserType.WEBTOON_ARTIST -> MaterialTheme.colorScheme.primary
            UserType.ASSIST_ARTIST -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.tertiary
        }
        val contentColor = when (userType) {
            UserType.WEBTOON_ARTIST -> MaterialTheme.colorScheme.onPrimary
            UserType.ASSIST_ARTIST -> MaterialTheme.colorScheme.onSecondary
            else -> MaterialTheme.colorScheme.onTertiary
        }
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
    fun UserPictureDialog(
        setDialogState: (Int) -> Unit,
        description: String,
        imageUri: Uri?
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
            onDismissRequest = { setDialogState(0) }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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

    @Composable
    fun UserLabel(userType: UserType) {
        val containerColor = when (userType) {
            UserType.WEBTOON_ARTIST -> MaterialTheme.colorScheme.primary
            UserType.ASSIST_ARTIST -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.tertiary
        }
        val contentColor = when (userType) {
            UserType.WEBTOON_ARTIST -> MaterialTheme.colorScheme.onPrimary
            UserType.ASSIST_ARTIST -> MaterialTheme.colorScheme.onSecondary
            else -> MaterialTheme.colorScheme.onTertiary
        }

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


