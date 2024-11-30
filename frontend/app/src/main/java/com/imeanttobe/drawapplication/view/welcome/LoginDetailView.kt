package com.imeanttobe.drawapplication.view.welcome

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.theme.onSeed
import com.imeanttobe.drawapplication.theme.seed
import com.imeanttobe.drawapplication.viewmodel.LoginViewModel
import com.imeanttobe.drawapplication.viewmodel.SignInState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDetailView( //실제로 로그인 및 아이디 비번 회원가입 페이지
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    returnTo: () -> Unit,
    navigateToReg: ()-> Unit,
    navigateHome: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.state.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is SignInState.Success -> {
                navigateHome()
            }
            is SignInState.Error -> {
                Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.login)) },
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
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center, // 수직 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.app_name),
                fontSize = 40.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp)) // 16dp의 간격 추가

            TextField(
                value = email,
                onValueChange = {email = it},
                placeholder = { Text(text = stringResource(id = R.string.enter_your_id)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
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
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed)
            )

            Spacer(modifier = Modifier.height(8.dp)) // 16dp의 간격 추가

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                value = password,
                onValueChange = {password = it},
                placeholder = { Text(text = stringResource(id = R.string.enter_your_pw)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "비밀번호 아이콘"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {focusManager.clearFocus()}
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = seed,
                    contentColor = onSeed
                ),
                shape = RoundedCornerShape(100.dp),
                onClick = {
                    viewModel.signIn(email, password)
                }
            ) {
                Text(text = stringResource(id = R.string.login))
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier.width(500.dp).padding(horizontal = 30.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = {  }) {
                    Text(
                        fontSize = 15.sp,
                        text = stringResource(id = R.string.forgot_your_id),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                TextButton(onClick = {  }) {
                    Text(
                        fontSize = 15.sp,
                        text = stringResource(id = R.string.forgot_your_pw),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                TextButton(onClick = navigateToReg) {
                    Text(
                        fontSize = 15.sp,
                        text = stringResource(id = R.string.register),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginDetailView(){
    LoginDetailView(
        returnTo = {},
        navigateToReg = {},
        navigateHome = {}
    )
}