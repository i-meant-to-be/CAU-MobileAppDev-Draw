package com.imeanttobe.drawapplication.view

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.view.chat.ChatDetailView
import com.imeanttobe.drawapplication.view.welcome.LoginView
import com.imeanttobe.drawapplication.view.welcome.LogindetailView
import com.imeanttobe.drawapplication.view.welcome.UserRegisterView
import com.imeanttobe.drawapplication.viewmodel.MainNavHostViewModel

@Composable
fun MainNavHostView(
    modifier: Modifier = Modifier,
    viewModel: MainNavHostViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavItem.BottomNavHostViewItem.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composableAnimated(route = NavItem.BottomNavHostViewItem.route) {
                BottomNavHostView(
                    isDevModeEnabled = true,
                    navigateTo = { route -> navController.navigate(route) },
                    navigateBack = { navController.popBackStack() }
                )
            }

            composableAnimated(route = NavItem.ChatDetailItem.route) {
                ChatDetailView()
            }

            composableAnimated(route = NavItem.LoginViewItem.route) {
                LoginView(navigateToDetail = { navController.navigate(NavItem.LogindetailViewItem.route) }
                )
            }

            composableAnimated(route = NavItem.SplashViewItem.route) {
                SplashView()
            }

            composableAnimated(route = NavItem.UserRegisterViewItem.route) {
                UserRegisterView()
            }
            composableAnimated(route = NavItem.LogindetailViewItem.route) {
                LogindetailView()
            }
        }
    }
}

private fun NavGraphBuilder.composableAnimated(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        }
    ) { navBackStackEntry ->
        content(navBackStackEntry)
    }
}