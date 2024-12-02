package com.imeanttobe.drawapplication.view.register

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.theme.onSeed
import com.imeanttobe.drawapplication.theme.seed
import com.imeanttobe.drawapplication.viewmodel.RegisterUserAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserAccountView(
    modifier: Modifier = Modifier,
    viewModel: RegisterUserAccountViewModel = hiltViewModel(),
    returnTo : () -> Unit,
    navigateToRegDetail : (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally, // 수직 중앙 정렬
            verticalArrangement = Arrangement.spacedBy(10.dp) // 수평 중앙 정렬
        ) {
            // Email area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.enter_your_email),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                isError = !viewModel.isEmailValid.value && viewModel.email.value.isNotEmpty(),
                value = viewModel.email.value,
                onValueChange = { newValue -> viewModel.updateEmail(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_email)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person, // 원하는 아이콘으로 변경
                        contentDescription = "사용자 아이콘" // 접근성을 위한 설명
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {focusManager.moveFocus(FocusDirection.Down)}
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Pw area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.pw),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.pw_length),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                isError = !viewModel.isPasswordValid.value && viewModel.password.value.isNotEmpty(),
                value = viewModel.password.value,
                onValueChange = { newValue -> viewModel.updatePassword(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_pw)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Pw confirm area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.pw_confirm),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.repeat_your_pw),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                isError = !viewModel.isPasswordConfirmed.value && viewModel.passwordConfirm.value.isNotEmpty(),
                value = viewModel.passwordConfirm.value,
                onValueChange = { newValue -> viewModel.updatePasswordConfirm(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_pw)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Cell phone number confirm area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.phone_number),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.enter_your_phone_number),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                isError = !viewModel.isPhoneNumberValid.value && viewModel.phoneNumber.value.isNotEmpty(),
                value = viewModel.phoneNumber.value,
                onValueChange = { newValue -> viewModel.updatePhoneNumber(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_phone_number)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.PhoneIphone, contentDescription = "전화번호 아이콘")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = seed)
            )
            Spacer(modifier = Modifier.height(10.dp))

            /*
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

             */

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 30.dp),
                onClick = {
                    navigateToRegDetail(viewModel.email.value, viewModel.password.value)
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = seed,
                    contentColor = onSeed
                ),
                shape = RoundedCornerShape(100.dp),
                enabled = viewModel.isAllValid.value
            ) {
                Text(
                    text = stringResource(id = R.string.next)
                )
            }
        }
    }
}
