package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.ImageItem
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
import com.imeanttobe.drawapplication.viewmodel.ExploreViewModel
import com.imeanttobe.drawapplication.viewmodel.ProfileViewModel

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // This composable is placed on Surface,
    // because this can't be displayed alone but need to be displayed upon Scaffold
    // which contains bottom navigation bar. (BottomNavHostView)
    Surface(modifier = modifier) {
        Column(modifier=Modifier.fillMaxSize()) {
            profilecard(modifier=Modifier)
            ProfileViewGrid(modifier = Modifier)


            }
        }
    }
@Composable
fun ProfileViewGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 20.dp),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        items(20) {
            ProfileViewGridItem(
                post = Post(userId = 0, description =""),
                image = ImageItem(postId = 0, imageUrl = ""),
                user = User(name = "Username", email = "", type = UserType.ASSIST_ARTIST, userImageUrl = "", password = "", instagramId = "")
            )
        }
    }
}
@Composable
fun ProfileViewGridItem(
    post: Post,
    image: ImageItem,
    user: User,
    onImageClick: () -> Unit = {}
) {
    /*
    val seed = Random.nextInt(4)
    val containerColor = when (seed) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.tertiary
    }
    val contentColor = when (seed) {
        0 -> MaterialTheme.colorScheme.onPrimary
        1 -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onTertiary
    }

     */

    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

            ProfileViewImageItem(
                post = post,
                imageItem = image,
                contentColor = contentColor,
                onImageClick = onImageClick
            )
        }


@Composable
fun ProfileViewImageItem(
    post: Post,
    imageItem: ImageItem,
    contentColor: Color,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.paintimage),
            contentDescription = "Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

    }
}


@Composable // 프로필 카드 컴포저블
fun profilecard(modifier: Modifier){
    Card(modifier= Modifier
        .fillMaxWidth()
        .padding(20.dp)) {

        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Image(
                painter = painterResource(id = R.drawable.joker),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp) // 이미지 크기 64dp로 설정
                    .clip(CircleShape) // 이미지를 원형으로 자름
                    .background(Color.LightGray, shape = CircleShape)
            )
            Text(text = "Nick Name", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp)
            Text(text = "Assistant",style = MaterialTheme.typography.labelMedium, fontSize = 15.sp)
            Spacer(Modifier.height(10.dp))
            Text(text = "Drawing is my life",style = MaterialTheme.typography.bodySmall)


        }
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
                    .width(58.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                border = BorderStroke(0.5.dp, Color(0xFF9E9D9D)), // 테두리 설정
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "정보수정", fontSize = 10.sp)
            }
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomEnd)
                    .width(58.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp)),// 배경색 설정
                shape = RoundedCornerShape(5.dp), // 버튼 모양 설정
                border = BorderStroke(0.5.dp, Color(0xFF9E9D9D)), // 테두리 설정
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "그림등록", fontSize = 10.sp)
            }
}}}
