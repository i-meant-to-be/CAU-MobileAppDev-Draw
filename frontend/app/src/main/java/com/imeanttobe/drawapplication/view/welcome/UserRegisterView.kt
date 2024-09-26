package com.imeanttobe.drawapplication.view.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.viewmodel.ExploreViewModel

@Composable
fun UserRegisterView(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "This is UserRegisterView")
    }
}