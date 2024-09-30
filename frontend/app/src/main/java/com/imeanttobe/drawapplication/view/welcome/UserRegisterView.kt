package com.imeanttobe.drawapplication.view.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.viewmodel.UserRegisterViewModel

@Composable
fun UserRegisterView(
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel()
) {
    // 회원가입 성공 후, '환영합니다' 문구가 표시되는 페이지는 여기서 한 번에 구현하는 게 좋을 것 같습니다.
    // 페이지 하나를 더 만들려고 보니 굳이 그럴 필요가 없을 정도로 간단하다고 생각해요.
    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "This is UserRegisterView")
        }
    }
}