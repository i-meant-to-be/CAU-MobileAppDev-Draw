package com.imeanttobe.drawapplication.view.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.viewmodel.UserRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterView(
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel(),
    returnTo : () -> Unit,
    navigateToRegDetail : () -> Unit
) {
    val email by viewModel.email
    val password by viewModel.password
    val passwordConfirm by viewModel.passwordConfirm
    val isValid by viewModel.isValid

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.register)) },
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // 수직 중앙 정렬
                verticalArrangement = Arrangement.spacedBy(10.dp) // 수평 중앙 정렬
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.email),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it) },
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person, // 원하는 아이콘으로 변경
                            contentDescription = "사용자 아이콘" // 접근성을 위한 설명
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            // Todo
                        }
                    )
                )

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.pw),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = password,
                    onValueChange = {viewModel.updatePassword(it)},
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_pw)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    }
                )

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.pw_confirm),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = passwordConfirm,
                    onValueChange = {viewModel.updatePasswordConfirm(it)},
                    placeholder = { Text(text = stringResource(id = R.string.confirm_your_password)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.phone_number),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.example_phone_number)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.PhoneIphone, contentDescription = "비밀번호 아이콘")
                    }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.verification_code),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.example_verification_code)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "비밀번호 아이콘")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }

            Button(
                onClick = {
                    viewModel.validateInput()
                    if (isValid) { navigateToRegDetail() }
                    else {
                        // Do something
                    }
                },
                modifier = Modifier
                    .align(alignment = BottomCenter) // 버튼을 화면 아래에 배치
                    .padding(bottom = 50.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF0073FF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(id = R.string.get_verification_code))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegister2View(
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel(),
) {
    // var selectedOption by rememberSaveable { mutableStateOf("그림 작가") }
    var selectedOption by rememberSaveable { mutableStateOf(UserType.WEBTOON_ARTIST) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Register Detail Page") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // 수직 중앙 정렬
                verticalArrangement = Arrangement.spacedBy(20.dp) // 수평 중앙 정렬
            ) {
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

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_email)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person, // 원하는 아이콘으로 변경
                            contentDescription = "사용자 아이콘" // 접근성을 위한 설명
                        )
                    }
                )

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = String.format(
                        "%s (%s)",
                        stringResource(id = R.string.instagram_account),
                        stringResource(id = R.string.option)
                    ),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )


                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_pw)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    }
                )

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.choose_your_type),
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
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.register_picture),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                Row(modifier= Modifier.align(alignment = Alignment.Start)) {
                    Box(
                        modifier = Modifier.padding(start = 30.dp)
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
                                .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
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
                onClick = {},
                modifier = Modifier
                    .align(BottomCenter) // 버튼을 화면 아래에 배치
                    .padding(bottom = 50.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF0073FF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
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