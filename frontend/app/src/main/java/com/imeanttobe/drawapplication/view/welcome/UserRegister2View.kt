package com.imeanttobe.drawapplication.view.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
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
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.theme.onSeed
import com.imeanttobe.drawapplication.theme.seed
import com.imeanttobe.drawapplication.theme.textFieldTransparentContainerColor
import com.imeanttobe.drawapplication.viewmodel.UserRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegister2View(
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel(),
    returnTo: ()-> Unit,
    navigateToLogin: () -> Unit,
) {
    // var selectedOption by rememberSaveable { mutableStateOf("그림 작가") }
    var selectedOption by rememberSaveable { mutableStateOf(UserType.WEBTOON_ARTIST) }
    val isValid by viewModel.isValid2.collectAsState()

    val nickname by viewModel.nickname.collectAsState()
    val instaId by viewModel.instaId.collectAsState()

    //그림 바뀔(추가/삭제)때도 가능하도록 해야함
    LaunchedEffect(key1 = nickname) {
        viewModel.isValidateInput2() /* todo*/
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Register Detail Page") },
                navigationIcon = {
                    IconButton(onClick = returnTo) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp, bottom = 130.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally, // 수직 중앙 정렬
                verticalArrangement = Arrangement.spacedBy(20.dp), // 수평 중앙 정렬

            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = String.format(
                        "%s (%s)",
                        stringResource(id = R.string.register_profileImg),
                        stringResource(id = R.string.option)
                    ),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                Row(modifier= Modifier.align(alignment = Alignment.Start)) {
                    Box(
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .size(150.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clickable {

                            }, // 이미지 선택 Intent 실행
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.add_photo),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = String.format(
                        "%s (%s)",
                        stringResource(id = R.string.nickname),
                        stringResource(id = R.string.necessary)
                    ),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = nickname,
                    onValueChange = {viewModel.updateNickname(it)},
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_nickname)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person, // 원하는 아이콘으로 변경
                            contentDescription = "사용자 아이콘" // 접근성을 위한 설명
                        )
                    },
                    colors = textFieldTransparentContainerColor()
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = String.format(
                        "%s (%s)",
                        stringResource(id = R.string.instagram_account),
                        stringResource(id = R.string.option)
                    ),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )


                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = instaId,
                    onValueChange = {viewModel.updateInstaId(it)},
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_instaId)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    },
                    colors = textFieldTransparentContainerColor()
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = String.format(
                        "%s (%s)",
                        stringResource(id = R.string.choose_your_type),
                        stringResource(id = R.string.necessary)
                    ),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                RadioButtonSet(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    selectedOption = selectedOption,
                    onChange = { selectedOption = it }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = String.format(
                        "%s (1개 이상 %s)",
                        stringResource(id = R.string.register_picture),
                        stringResource(id = R.string.necessary)
                    ),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                Row(modifier= Modifier.align(alignment = Alignment.Start)) {
                    Box(
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .size(150.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clickable {}, // 이미지 선택 Intent 실행
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.joker),
                            contentDescription = "Image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(150.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clickable {}, // 이미지 선택 Intent 실행
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.add_picture),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.signUp(){
                        navigateToLogin()
                    }
                },
                modifier = Modifier
                    .align(BottomCenter) // 버튼을 화면 아래에 배치
                    .padding(bottom = 50.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = seed,
                    contentColor = onSeed
                ),
                shape = RoundedCornerShape(100.dp),
                enabled = isValid
            ) {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }
}

@Composable
fun RadioButtonSet(
    modifier: Modifier,
    selectedOption: UserType,
    onChange: (UserType) -> Unit
) {
    val radioOptions = listOf(UserType.WEBTOON_ARTIST, UserType.ASSIST_ARTIST)
    val radioStringResId = when (selectedOption) {
        UserType.WEBTOON_ARTIST -> R.string.usertype_webtoon_artist
        else -> R.string.usertype_assist_artist
    }

    Row(modifier = modifier) {
        radioOptions.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (selectedOption == item),
                    onClick = { onChange(item) },
                    colors = RadioButtonDefaults.colors(Color(0xFF0073FF))
                )
                Text(text = stringResource(id = radioStringResId))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserRegister2View(){
    UserRegister2View(
        returnTo = { },
        navigateToLogin = {},
    )
}