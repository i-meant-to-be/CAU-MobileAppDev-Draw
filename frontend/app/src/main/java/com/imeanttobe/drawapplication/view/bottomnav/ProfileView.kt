package com.imeanttobe.drawapplication.view.bottomnav

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.viewmodel.ProfileViewModel
import com.imeanttobe.drawapplication.viewmodel.SignOutState

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLogin : ()->Unit
) {
    // This composable is placed on Surface,
    // because this can't be displayed alone but need to be displayed upon Scaffold
    // which contains bottom navigation bar. (BottomNavHostView)
    Surface(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileCard(modifier=Modifier, viewModel = viewModel, navigateToLogin = navigateToLogin)
            ProfileViewGrid(modifier = Modifier, viewModel=viewModel)
        }
    }
}

@Composable
fun ProfileViewGrid(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 10.dp),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        items(viewModel.imageUris.size) {
            index->
            ProfileViewImageItem(
                imageUri = viewModel.imageUris[index],
                onImageClick = {}
            )
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
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
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
){
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val currentUser = FirebaseAuth.getInstance().currentUser
    var Nickname by remember { mutableStateOf("nickname")}
    var Role by remember { mutableStateOf("assistant")}
    var onesentence by remember { mutableStateOf("drawing is my life")}
    var tempNickname by remember { mutableStateOf("nickname")}
    var tempRole by remember { mutableStateOf("assistant")}
    var temponesentence by remember { mutableStateOf("drawing is my life")}

    val profileImageUri = viewModel.profileImageUri.collectAsState()
    val nickname = viewModel.userNickname.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.addImageUri( result.data?.data)
        }
    }

    val uiState = viewModel.state.collectAsState()

    LaunchedEffect(key1 = uiState.value) {
        if (uiState.value == SignOutState.LoggedOut) {
            navigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = profileImageUri,
//                model = Uri.parse("android.resource://com.example.myapp/drawable/basic_profile"),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp) // 이미지 크기 64dp로 설정
                    .clip(CircleShape) // 이미지를 원형으로 자름
            )
            Spacer(Modifier.height(10.dp))
            Text(text = nickname.value?:"", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp)
            Text(text = Role,style = MaterialTheme.typography.labelMedium, fontSize = 15.sp)
            Spacer(Modifier.height(10.dp))
            Text(text = onesentence,style = MaterialTheme.typography.bodySmall)
        }

        if (true) {
            Button(
                onClick = {showDialog=true},
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
                    .width(58.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                )            ) {
                Text(text = stringResource(id = R.string.modify_information), fontSize = 10.sp)
            }
            if (showDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showDialog = false },
                    title = { Box(modifier=Modifier.fillMaxWidth()
                        , contentAlignment = Alignment.Center){
                        Text("프로필 정보 수정",style = MaterialTheme.typography.labelLarge, fontSize = 18.sp)} },
                    text = {
                        Column() {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Nick Name",
                            fontSize = 15.sp,               // 텍스트 크기
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            value = tempNickname,
                            onValueChange = {tempNickname=it},
                            placeholder = { Text(text = "닉네임을 입력하시오")},
                            leadingIcon = {
                                Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                            })

                            Spacer(Modifier.height(10.dp))
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = "Role",
                                fontSize = 15.sp,               // 텍스트 크기
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                value = tempRole,
                                onValueChange = {tempRole=it},
                                placeholder = { Text(text = "역할을 입력하시오")},
                                leadingIcon = {
                                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                                })
                            Spacer(Modifier.height(10.dp))

                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = "Introduce one sentence",
                                fontSize = 15.sp,               // 텍스트 크기
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                value = temponesentence,
                                onValueChange = {temponesentence=it},
                                placeholder = { Text(text = "한 줄 소개")},
                                leadingIcon = {
                                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                                })
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
                )
            }


            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    launcher.launch(intent)
                },
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomEnd)
                    .width(58.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                )
            ) {
                Text(text = stringResource(id = R.string.register_picture), fontSize = 10.sp)
            }

            Button(
                onClick = {
                    viewModel.signOut()
                },
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopStart)
                    .width(58.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                )
            ) {
                Text(text = stringResource(id = R.string.logout), fontSize = 10.sp)
            }

        }
        else{
            OutlinedButton(onClick = {},modifier = Modifier
                    .padding(10.dp)
                .align(Alignment.TopEnd)
                .width(58.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                )            ) {
                Text(text = stringResource(id = R.string.direct_message), fontSize = 10.sp)
            }
        }
    }
}

