package com.imeanttobe.drawapplication.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.imeanttobe.drawapplication.data.navigation.BottomBarItem
import com.imeanttobe.drawapplication.theme.onKeyColor
import com.imeanttobe.drawapplication.theme.keyColor1
import com.imeanttobe.drawapplication.view.bottomnav.ChatView
import com.imeanttobe.drawapplication.view.bottomnav.ExploreView
import com.imeanttobe.drawapplication.view.bottomnav.ProfileView

@Composable
fun BottomNavHostView(
    index: Int,
    onChangeIndex: (Int) -> Unit,
    isDevModeEnabled: Boolean = false,
    navigateTo: (String) -> Unit,
    navigateToLogin: () -> Unit,
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavHostAppBar(
                index = index,
                onClick = { newValue -> onChangeIndex(newValue) },
                isDevModeEnabled = isDevModeEnabled
            )
        }
    ) { innerPadding ->
        when(index) {
            0 -> ChatView(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                navigateTo = navigateTo
            )
            1 -> ExploreView(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
                navController = navController )
            2 -> ProfileView(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                navigateToLogin = navigateToLogin
            )
            3 -> DevView(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
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
    BottomAppBar() {
        BottomBarItem.items.subList(0, if (isDevModeEnabled) 4 else 3).forEach { item ->
            NavigationBarItem(
                selected = item.index == index,
                onClick = { onClick(item.index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.labelId)
                    )
                },
                label = { Text(text = stringResource(id = item.labelId)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = onKeyColor,
                    indicatorColor = keyColor1
                )
            )
        }
    }
}