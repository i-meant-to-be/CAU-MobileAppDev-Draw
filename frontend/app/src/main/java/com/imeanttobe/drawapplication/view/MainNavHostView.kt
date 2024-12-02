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
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseAuth
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.view.chat.ChatDetailView
import com.imeanttobe.drawapplication.view.login.LoginDetailView
import com.imeanttobe.drawapplication.view.login.LoginTitleView
import com.imeanttobe.drawapplication.view.register.RegisterUserProfileView
import com.imeanttobe.drawapplication.view.register.RegisterUserAccountView

@Composable
fun MainNavHostView(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startRoute =
        if (FirebaseAuth.getInstance().currentUser != null) NavItem.BottomNavHostViewItem.route
        else NavItem.LoginViewItem.route

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composableAnimated(route = NavItem.BottomNavHostViewItem.route) {
                BottomNavHostView(
                    isDevModeEnabled = true,
                    navigateTo = { route -> navController.navigate(route) },
                    navigateBack = { navController.popBackStack() },
                    navigateToLogin = {
                        navController.navigate(NavItem.LoginViewItem.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composableAnimated(route = NavItem.ChatDetailItem.route) {
                ChatDetailView(
                    navigateUp = { navController.navigateUp() }
                )
            }

            composableAnimated(route = NavItem.LoginViewItem.route) {
                LoginTitleView(
                    navigateToDetail = { navController.navigate(NavItem.LogindetailViewItem.route) }
                )
            }

            composableAnimated(route = NavItem.UserRegister1ViewItem.route) {
                RegisterUserAccountView(
                    returnTo = { navController.popBackStack() },
                    navigateToRegDetail = {email, password ->
                        // TODO
                        // sharedViewModel.updateEmail(email)
                        // sharedViewModel.updatePassword(password)
                        navController.navigate(NavItem.UserRegister2ViewItem.route)
                    }
                )
            }

            composableAnimated(route = NavItem.UserRegister2ViewItem.route) {
                RegisterUserProfileView(
                    returnTo = { navController.popBackStack() },
                    navigateToLogin = {
                        navController.navigate(NavItem.LogindetailViewItem.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composableAnimated(route = NavItem.LogindetailViewItem.route) {
                LoginDetailView(
                    returnTo = { navController.popBackStack() },
                    navigateToReg = { navController.navigate(NavItem.UserRegister1ViewItem.route) },
                    navigateHome = {
                        navController.navigate(NavItem.BottomNavHostViewItem.route){
                            popUpTo(0){inclusive = true}
                        }
                    }
                )
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