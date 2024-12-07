
package com.imeanttobe.drawapplication.view.bottomnav

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.viewmodel.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileView(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    returnTo: () -> Unit,
    navBackStackEntry: NavBackStackEntry,
    navController: NavHostController

) {
    val context = LocalContext.current
    val posts = viewModel.userPosts.collectAsState()
    val user = viewModel.user.collectAsState()
    val userId = navBackStackEntry.arguments?.getString("userId")

    LaunchedEffect(userId) {
        viewModel.setUserProfileData(userId)
        viewModel.setUserPostData(userId)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
                onImageClick = { imageUri, description ->
                    viewModel.setDialogState(1)
                    viewModel.setPictureDialogData(imageUri, description)
                }
            )
        }
    }

    if (viewModel.dialogState.value == 1) {
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
            UserTypeLabel(userType = user?.type ?: UserType.UNDEFINED)
            Spacer(modifier = Modifier.width(10.dp))
            InstagramButton(
                instagramId = user?.instagramId ?: "",
                userType = user?.type ?: UserType.UNDEFINED
            )
        }
        Spacer(Modifier.height(24.dp))

        // Chat
        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(40.dp),
            onClick = {

            },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonContainerColor,
                contentColor = buttonContentColor
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = stringResource(id = R.string.send_message))
            }
        }
    }
}

