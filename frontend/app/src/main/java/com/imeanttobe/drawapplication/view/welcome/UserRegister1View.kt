package com.imeanttobe.drawapplication.view.welcome

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.theme.onSeed
import com.imeanttobe.drawapplication.theme.seed
import com.imeanttobe.drawapplication.theme.textFieldTransparentContainerColor
import com.imeanttobe.drawapplication.viewmodel.UserRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegister1View(
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel(),
    returnTo : () -> Unit,
    navigateToRegDetail : (String, String) -> Unit
) {
    val email by viewModel.email
    val password by viewModel.password
    val passwordConfirm by viewModel.passwordConfirm
    val isValid by viewModel.isValid1.collectAsState()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = email, key2 = password, key3 = passwordConfirm) {
        viewModel.isValidateInput1()
    }

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
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.email),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = email,
                    onValueChange = {viewModel.updateEmail(it)},
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
                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                    ),
                    colors = textFieldTransparentContainerColor()
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.pw),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = password,
                    onValueChange = {viewModel.updatePassword(it)},
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_pw)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = textFieldTransparentContainerColor()
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.pw_confirm),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = passwordConfirm,
                    onValueChange = { viewModel.updatePasswordConfirm(it) },
                    placeholder = { Text(text = stringResource(id = R.string.confirm_your_password)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    colors = textFieldTransparentContainerColor()
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        value = "",
                        onValueChange = {},
                        placeholder = { Text(text = stringResource(id = R.string.example_phone_number)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.PhoneIphone,
                                contentDescription = "비밀번호 아이콘"
                            )
                        },
                        colors = textFieldTransparentContainerColor()
                    )
                    Spacer(modifier =Modifier.padding(horizontal = 8.dp))
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.wrapContentSize(),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.verification),
//                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.Start),
                    text = stringResource(id = R.string.verification_code),
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        value = "",
                        onValueChange = {},
                        placeholder = { Text(text = stringResource(id = R.string.example_verification_code)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Check, contentDescription = "비밀번호 아이콘")
                        },
                        colors = textFieldTransparentContainerColor()
                    )
                    Spacer(modifier =Modifier.padding(horizontal = 8.dp))
                    OutlinedButton(
                        onClick = {  },
                        modifier = Modifier.wrapContentSize(),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            Button(
                onClick = {
                    viewModel.isValidateInput1()
                          if(isValid){
                              navigateToRegDetail(email, password)
                          }
                          else{
                              Toast.makeText(context, "유효한 입력이 아닙니다.", Toast.LENGTH_SHORT).show()
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
                shape = RoundedCornerShape(8.dp),
                enabled = isValid
            ) {
                Text("프로필 정보 입력")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserRegister1View(){
    UserRegister1View(
        returnTo = { },
        navigateToRegDetail = {} as (String, String) -> Unit
    )
}