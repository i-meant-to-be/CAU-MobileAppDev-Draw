package com.imeanttobe.drawapplication.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.data.navigation.BottomBarItem
import com.imeanttobe.drawapplication.view.bottomnav.ChatView
import com.imeanttobe.drawapplication.view.bottomnav.ExploreView
import com.imeanttobe.drawapplication.view.bottomnav.ProfileView
import com.imeanttobe.drawapplication.viewmodel.BottomNavHostViewModel

@Composable
fun BottomNavHostView(
    viewModel: BottomNavHostViewModel = hiltViewModel(),
    isDevModeEnabled: Boolean = false,
    navigateTo: (String) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavHostAppBar(
                index = viewModel.currentIndex.value,
                onClick = { newValue -> viewModel.setCurrentIndex(newValue) },
                isDevModeEnabled = isDevModeEnabled
            )
        }
    ) { innerPadding ->
        when(viewModel.currentIndex.value) {
            0 -> ChatView(modifier = Modifier.padding(innerPadding).fillMaxSize())
            1 -> ExploreView(modifier = Modifier.padding(innerPadding).fillMaxSize())
            2 -> ProfileView(modifier = Modifier.padding(innerPadding).fillMaxSize())
            3 -> DevView(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                navigateBack = navigateBack,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
fun BottomNavHostAppBar(
    index: Int,
    onClick: (Int) -> Unit,
    isDevModeEnabled: Boolean
) {
    BottomAppBar {
        BottomBarItem.items.subList(0, if (isDevModeEnabled) 4 else 3).forEach { item ->
            NavigationBarItem(
                selected = item.index == index,
                onClick = { onClick(item.index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) }
            )
        }
    }
}