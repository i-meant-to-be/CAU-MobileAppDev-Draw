package com.imeanttobe.drawapplication.view.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.viewmodel.LoginViewModel




@Composable //로그인하기 버튼 있는 페이지
fun LoginView(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
    ,navigateToDetail: () -> Unit
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center, // 수직 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
        ) {
            Text(text = "Draw",fontSize = 40.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold)
            Text(text = "창작가들을 위한 공간",fontSize = 15.sp,)

            Spacer(modifier = Modifier.height(16.dp)) // 16dp의 간격 추가

            Button(modifier = Modifier.width(257.dp).height(37.dp),
                shape = RoundedCornerShape(8.dp),


                        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF0073FF),
                        contentColor =Color.White)
                ,onClick = navigateToDetail){ Text("로그인하기")}
            }
        }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogindetailView( //실제로 로그인 및 아이디 비번 회원가입 페이지
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    returnTo: () -> Unit,
    navigateToReg: ()-> Unit

) {
    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
            title = {Text("Login page")},
            navigationIcon = {
                IconButton(onClick = returnTo) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                }
            }


        )
        },) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center, // 수직 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 30.dp).align(alignment = Alignment.Start),
                text = "DRAW",
                fontSize = 40.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp)) // 16dp의 간격 추가



            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Enter your ID") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person, // 원하는 아이콘으로 변경
                        contentDescription = "사용자 아이콘" // 접근성을 위한 설명
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp)) // 16dp의 간격 추가


            TextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                value = "",
                onValueChange = {},
                label = { Text(text = "Enter your Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "비밀번호 아이콘")})

            Spacer(modifier = Modifier.height(16.dp))



            Button(modifier = Modifier.height(50.dp).fillMaxWidth().padding(horizontal = 30.dp),

                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF0073FF),
                    contentColor =Color.White),
                shape = RoundedCornerShape(8.dp)
                ,onClick = {}){ Text("로그인하기")}

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.width(500.dp).padding(horizontal = 30.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly,){TextButton(onClick ={}) { Text(fontSize = 15.sp,text="아이디 찾기")}
                TextButton(onClick ={}) { Text(fontSize = 15.sp,text="비밀번호 찾기")}
                    TextButton(onClick =navigateToReg) { Text(fontSize = 15.sp,text="회원가입")}


            }


        }
    }
}