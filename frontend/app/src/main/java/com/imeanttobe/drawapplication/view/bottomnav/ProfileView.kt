package com.imeanttobe.drawapplication.view.bottomnav

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.viewmodel.ProfileViewModel

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLogin : ()->Unit
) {
    Surface(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileCard(
                modifier = Modifier,
                viewModel = viewModel,
                navigateToLogin = navigateToLogin
            )
            ProfileViewGrid(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ProfileViewGrid(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user = viewModel.user.collectAsState()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        user.value?.pictureIds?.let {
            items(it.size) {
                index->
                ProfileViewImageItem(
                    imageUri = user.value?.pictureIds?.get(index)?.toUri(),
                    onImageClick = {
                        //todo
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileViewImageItem(
    imageUri: Uri?,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick() }
    ) {
        Log.d("ProfileView", imageUri.toString())

        if (imageUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUri)
                    .build(),
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.paintimage),
                contentDescription = "Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable // 프로필 카드 컴포저블
fun ProfileCard(
    modifier: Modifier,
    viewModel: ProfileViewModel,
    navigateToLogin: ()-> Unit
) {
    val buttonContainerColor = MaterialTheme.colorScheme.surfaceDim
    val buttonContentColor = MaterialTheme.colorScheme.onSurface
    var showDialog by remember { mutableStateOf(false) }
    val user = viewModel.user.collectAsState()
    val signOutState = viewModel.signOutState.collectAsState()
    val context = LocalContext.current

    val newProfilePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // viewModel.updateProfilePhotoUri(uri, context)
        }
    )

    val newPicturePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.addImageUri(uri, context)
        }
    )

    LaunchedEffect(key1 = signOutState.value) {
        if (signOutState.value == Resource.Success()) {
            navigateToLogin()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Log.d("ProfileView", user.value?.profilePhotoUri.toString())

        // Profile photo
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(user.value?.profilePhotoUri)
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
            text = user.value?.nickname ?: stringResource(id = R.string.error_nickname),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(0.dp))

        // Introduce
        Text(
            text = user.value?.introduce ?: stringResource(id = R.string.error_introduce),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Usertype
        UserTypeLabel(userType = user.value?.type ?: UserType.UNDEFINED)
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
                onClick = { showDialog = true },
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
                onClick = { newPicturePickerLauncher.launch("image/*") },
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
                    viewModel.signOut()
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

            if (showDialog) {
                UpdateUserDataDialog(
                    onDismiss = {
                        viewModel.setDialogState(false)
                    },
                    onConfirm = {
                        viewModel.updateUserData()
                        viewModel.setDialogState(false)
                    }
                )
            }
        }
    }
}

@Composable
fun UserTypeLabel(userType: UserType) {
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

@Composable
fun UpdateUserDataDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    // Dialog()
    // AlertDialog()

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        content = {

        }
                /*
        text = {
            Column() {

                Row(verticalAlignment = Alignment.CenterVertically
                    ,horizontalArrangement = Arrangement.spacedBy(50.dp)) {


                    Text(
                        modifier = Modifier,
                        text = "프로필",
                        fontSize = 15.sp,               // 텍스트 크기
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )



                    Box(
                        modifier = Modifier

                            .size(100.dp)
                            .background(
                                Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                                launcher2.launch(intent)}, // 이미지 선택 Intent 실행
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(

                            model = profileImageUri.value
                            ,contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp) // 이미지 크기 64dp로 설정
                                .clip(CircleShape)
                        )
                    }
                }
                Spacer(modifier.height(30.dp))


                Row(verticalAlignment = Alignment.CenterVertically
                    ,horizontalArrangement = Arrangement.spacedBy(0.dp)) {


                    Text(
                        modifier = Modifier.weight(3f),
                        text = "시용자 이름",
                        fontSize = 15.sp,               // 텍스트 크기
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )

                    TextField(
                        modifier = Modifier
                            .weight(7f),
                        value = tempNickname,
                        onValueChange = { tempNickname = it },

                        placeholder = { Text(text = "닉네임을 입력하시오") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed),
                    )
                }

                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically
                    ,horizontalArrangement = Arrangement.spacedBy(0.dp)) {

                    Text(
                        modifier = Modifier.weight(3f),
                        text = "역할",
                        fontSize = 15.sp,               // 텍스트 크기
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )

                    TextField(
                        modifier = Modifier
                            .weight(7f),
                        value = tempRole,
                        onValueChange = { tempRole= it },

                        placeholder = { Text(text = "역할을 입력하시오") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed),
                    )
                }
                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically
                    ,horizontalArrangement = Arrangement.spacedBy(0.dp)) {

                    Text(
                        modifier = Modifier.weight(3f),
                        text = "소개",
                        fontSize = 15.sp,               // 텍스트 크기
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )

                    TextField(
                        modifier = Modifier
                            .weight(7f),
                        value = temponesentence,
                        onValueChange = { temponesentence = it },

                        placeholder = { Text(text = "소개를 입력하시오") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed),
                    )
                }


            }

            // 정보 입력 폼 추가
            // 예: OutlinedTextField, Button 등
        },
        confirmButton = {
            Button(onClick = {
                onesentence=temponesentence
                Role=tempRole
                Nickname=tempNickname

                // 정보 저장 로직 추가
                showDialog = false // dialog 닫기
            }) {
                Text("저장")
            }
        },
        dismissButton = {
            Button(onClick = { showDialog = false }) {
                Text("취소")
            }
        }

                 */
    )
}