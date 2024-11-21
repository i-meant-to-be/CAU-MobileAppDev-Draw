package com.imeanttobe.drawapplication.view.welcome

import android.graphics.fonts.FontStyle
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.viewmodel.UserRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterView(
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel(),
    returnTo : () -> Unit
    ,navigateToRegDetail : () -> Unit
) {
    // 회원가입 성공 후, '환영합니다' 문구가 표시되는 페이지는 여기서 한 번에 구현하는 게 좋을 것 같습니다.
    // 페이지 하나를 더 만들려고 보니 굳이 그럴 필요가 없을 정도로 간단하다고 생각해요.
    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(text="Register Page")},
                navigationIcon = {
                    IconButton(onClick = returnTo) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }


            )
        }) { innerPadding ->
        Box( modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)){
        Column(
            modifier = Modifier
                .fillMaxSize().padding(top = 30.dp),

            // 수직 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp) // 수평 중앙 정렬
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                text = "이메일",
                fontSize = 20.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )


            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Enter your Email") },
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
                    onNext = {
                        // Todo
                    }
                )
            )

            Text(
                modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                text = "비밀번호",
                fontSize = 20.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )


            TextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                value = "",
                onValueChange = {},
                label = { Text(text = "Enter your Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                })

            Text(
                modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                text = "비밀번호 확인",
                fontSize = 20.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )


            TextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                value = "",
                onValueChange = {},
                label = { Text(text = "Check your Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                })
            Spacer(modifier = Modifier.height(40.dp))


            Text(
                modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                text = "휴대폰 번호",
                fontSize = 20.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )


            TextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                value = "",
                onValueChange = {},
                label = { Text(text = "010-0000-0000") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.PhoneIphone, contentDescription = "비밀번호 아이콘")
                })

            Text(
                modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                text = "인증번호",
                fontSize = 20.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )


            TextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                value = "",
                onValueChange = {},
                label = { Text(text = "0000") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "비밀번호 아이콘")
                })

            Spacer(modifier = Modifier.height(40.dp))


        }
            Button(
                onClick = navigateToRegDetail,
                modifier = Modifier
                    .align(Alignment.BottomCenter) // 버튼을 화면 아래에 배치
                    .padding(bottom = 50.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF0073FF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("인증번호요청")
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
    var selectedoption by rememberSaveable { mutableStateOf("그림작가") }


    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(text="Register Detail Page")}
            )
        }) { innerPadding ->
        Box( modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)){
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(top = 30.dp),

                // 수직 중앙 정렬
                horizontalAlignment = Alignment.CenterHorizontally
                ,
                verticalArrangement = Arrangement.spacedBy(20.dp) // 수평 중앙 정렬
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = "닉네임(필수)",
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )


                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Enter your Email") },
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
                    text = "인스타그램(선택)",
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )


                TextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Enter your Password") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")
                    })

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = "사용자 유형선택",
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )
                RadioButtonSet(modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start)
                    ,selectedoption = selectedoption, onchange = {selectedoption = it})

                Text(
                    modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                    text = "그림 등록",
                    fontSize = 20.sp,               // 텍스트 크기
                    fontWeight = FontWeight.Bold
                )
                Row(modifier= Modifier.align(alignment = Alignment.Start)
                    ){
                    Box(
                        modifier = Modifier.padding(start = 30.dp)
                            .size(150.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clickable {}, // 이미지 선택 Intent 실행
                        contentAlignment = Alignment.Center
                    ){ Image(
                        painter = painterResource(id = R.drawable.joker),
                        contentDescription = "Image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth().background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                    ) }

                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp).size(150.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clickable {}, // 이미지 선택 Intent 실행
                        contentAlignment = Alignment.Center
                    ){Text(text="그림 추가",fontSize = 15.sp, color = Color.White)}

                }







            }

            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter) // 버튼을 화면 아래에 배치
                    .padding(bottom = 50.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF0073FF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("회원가입")
            }
        }
    }
}

@Composable
fun RadioButtonSet(modifier: Modifier,selectedoption : String, onchange : (String) -> Unit){
    val radioOptions = listOf("그림작가","어시","기업")
    Row(modifier = modifier) {
        radioOptions.forEach { i->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = (selectedoption == i), onClick = {onchange(i)},colors= RadioButtonDefaults.colors(Color(0xFF0073FF)))
                Text(i)
            }
        }
    }


}