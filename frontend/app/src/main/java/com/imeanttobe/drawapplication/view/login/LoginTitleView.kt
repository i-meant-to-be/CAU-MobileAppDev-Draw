package com.imeanttobe.drawapplication.view.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.theme.onSeed
import com.imeanttobe.drawapplication.theme.seed

@Composable //로그인하기 버튼 있는 페이지
fun LoginTitleView(
    modifier: Modifier = Modifier,
    navigateToDetail: () -> Unit
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center, // 수직 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 40.sp,               // 텍스트 크기
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.app_description),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(16.dp)) // 16dp의 간격 추가

            Button(
                modifier = Modifier
                    .width(250.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = seed,
                    contentColor = onSeed
                ),
                onClick = navigateToDetail
            ) {
                Text(text = stringResource(id = R.string.login))
            }
        }
    }
}
