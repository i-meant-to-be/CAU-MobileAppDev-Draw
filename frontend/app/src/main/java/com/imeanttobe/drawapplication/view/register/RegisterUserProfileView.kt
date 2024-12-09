package com.imeanttobe.drawapplication.view.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.Resource
import com.imeanttobe.drawapplication.theme.onKeyColor
import com.imeanttobe.drawapplication.theme.keyColor1
import com.imeanttobe.drawapplication.viewmodel.RegisterUserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserProfileView(
    modifier: Modifier = Modifier,
    viewModel: RegisterUserProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    email: String,
    password: String,
    phoneNumber: String
) {
    val registerState = viewModel.registerState.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val profilePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.setProfilePhotoUri(uri)
            }
        }
    )
    val picturePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.setPictureUri(uri)
            }
        }
    )

    LaunchedEffect(key1 = registerState.value) {
        when (registerState.value) {
            is Resource.Success -> {
                navigateToHome()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.register_profile)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally, // 수직 중앙 정렬
            verticalArrangement = Arrangement.spacedBy(20.dp), // 수평 중앙 정렬
        ) {
            // Profile picture area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = String.format(
                    "%s (%s)",
                    stringResource(id = R.string.profile_img),
                    stringResource(id = R.string.option)
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.register_profile_img),
                style = MaterialTheme.typography.bodyMedium
            )
            if (viewModel.profilePhotoUri.value != Uri.EMPTY) {
                AsyncImage(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable { profilePhotoPickerLauncher.launch("image/*") },
                    model = ImageRequest.Builder(context)
                        .data(viewModel.profilePhotoUri.value)
                        .build(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Profile photo"
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                        .clickable { profilePhotoPickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.add_profile_photo),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Nickname area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = String.format(
                    "%s (%s)",
                    stringResource(id = R.string.nickname),
                    stringResource(id = R.string.necessary)
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.enter_your_nickname),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                value = viewModel.nickname.value,
                onValueChange = { newValue -> viewModel.setNickname(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_nickname)) },
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
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Introduce area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = String.format(
                    "%s (%s)",
                    stringResource(id = R.string.introduce),
                    stringResource(id = R.string.option)
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.enter_your_introduce),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                value = viewModel.introduce.value,
                onValueChange = { newValue -> viewModel.setIntroduce(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_introduce)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.FormatQuote, // 원하는 아이콘으로 변경
                        contentDescription = "한 줄 소개 아이콘" // 접근성을 위한 설명
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Instagram area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = String.format(
                    "%s (%s)",
                    stringResource(id = R.string.instagram_account),
                    stringResource(id = R.string.option)
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.enter_your_instagram_id),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                value = viewModel.instagramId.value,
                onValueChange = { newValue -> viewModel.setInstagramId(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_instagram_id)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.instagram_logo), // 원하는 아이콘으로 변경
                        contentDescription = "인스타그램 아이디 아이콘", // 접근성을 위한 설명
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = keyColor1)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // User type area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = String.format(
                    "%s (%s)",
                    stringResource(id = R.string.usertype),
                    stringResource(id = R.string.necessary)
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.choose_your_usertype),
                style = MaterialTheme.typography.bodyMedium
            )
            RadioButtonSet(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                selectedOption = viewModel.userType.value,
                onChange = { newValue -> viewModel.setUserType(newValue) }
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Picture area
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = String.format(
                    "%s (%s)",
                    stringResource(id = R.string.register_picture),
                    stringResource(id = R.string.necessary)
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.Start),
                text = stringResource(id = R.string.add_your_first_picture),
                style = MaterialTheme.typography.bodyMedium
            )
            if (viewModel.pictureUri.value != Uri.EMPTY) {
                AsyncImage(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable { picturePickerLauncher.launch("image/*") },
                    model = ImageRequest.Builder(context)
                        .data(viewModel.pictureUri.value)
                        .build(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Picture"
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                        .clickable { picturePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.add_picture),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Description area
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(horizontal = 30.dp),
                text = stringResource(id = R.string.picture_description),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(horizontal = 30.dp),
                text = stringResource(id = R.string.enter_picture_description),
                style = MaterialTheme.typography.bodyMedium
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                value = viewModel.description.value,
                onValueChange = { newValue -> viewModel.setDescription(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.example_picture_description)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
                minLines = 1,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Button area
            if (registerState.value is Resource.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        viewModel.signUp(context, email, password, phoneNumber)
                    },
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = keyColor1,
                        contentColor = onKeyColor
                    ),
                    shape = RoundedCornerShape(100.dp),
                    enabled = viewModel.isAllValid.value
                ) {
                    Text(text = stringResource(id = R.string.register))
                }
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
    val context = LocalContext.current
    val radioOptions = listOf(UserType.WEBTOON_ARTIST, UserType.ASSIST_ARTIST)

    Row(modifier = modifier) {
        radioOptions.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (selectedOption == item),
                    onClick = { onChange(item) },
                    colors = RadioButtonDefaults.colors(Color(0xFF0073FF))
                )
                Text(
                    text = when (item) {
                        UserType.WEBTOON_ARTIST -> context.getString(R.string.usertype_webtoon_artist)
                        else -> context.getString(R.string.usertype_assist_artist)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserRegister2View(){
    RegisterUserProfileView(
        navigateBack = {},
        navigateToHome = {},
        email = "",
        password = "",
        phoneNumber = ""
    )
}