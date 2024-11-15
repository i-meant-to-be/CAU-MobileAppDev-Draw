package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
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
            Card() {
                Column() {
                    Image(
                        painter = painterResource(id = R.drawable.joker),
                        contentDescription = "Image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth().background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Text(text = "Profile")
                    Text(text = "Profile")

                }

            }
        }
    }
}