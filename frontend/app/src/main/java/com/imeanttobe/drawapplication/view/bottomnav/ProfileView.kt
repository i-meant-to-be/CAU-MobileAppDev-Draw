package com.imeanttobe.drawapplication.view.bottomnav

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.theme.seed
import com.imeanttobe.drawapplication.viewmodel.ProfileViewModel
import com.imeanttobe.drawapplication.data.etc.Resource

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
                modifier = Modifier,
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
) {
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
    val individualImageUri = viewModel.individualImageUri.collectAsState()
    val nickname = viewModel.userNickname.collectAsState()


    var showDialog by remember { mutableStateOf(false) }


    val launcher1 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.addImageUri( result.data?.data)
        }
    }

    val launcher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.addprofileImageUri( result.data?.data)
        }
    }
    val uiState = viewModel.signOutState.collectAsState()
    LaunchedEffect(key1 = uiState.value) {
        if (uiState.value == Resource.Success()) {
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
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = profileImageUri.value,
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

            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){

            Button(
                onClick = {showDialog=true},
                modifier = Modifier
                    .weight(0.7f)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.White
                )            ) {
                Text(text = stringResource(id = R.string.modify_information), fontSize = 15.sp, maxLines = 1 )
            }

                Button(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        launcher1.launch(intent)
                    },
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                    shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(id = R.string.register_picture), fontSize = 15.sp, maxLines = 1 )
                }

            }
        }
        Button(
            onClick = {
                viewModel.signOut()
            },
            modifier = Modifier
                .padding(10.dp)
                .width(58.dp)
                .align(alignment = Alignment.TopEnd)
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

@Composable
fun UpdateUserDataDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
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