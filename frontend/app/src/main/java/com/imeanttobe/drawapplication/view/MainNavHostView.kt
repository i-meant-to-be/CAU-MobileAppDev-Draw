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
import com.imeanttobe.drawapplication.data.navigation.NavItem
import com.imeanttobe.drawapplication.view.chat.ChatDetailView
import com.imeanttobe.drawapplication.view.welcome.LoginDetailView
import com.imeanttobe.drawapplication.view.welcome.LoginView
import com.imeanttobe.drawapplication.view.welcome.UserRegister2View
import com.imeanttobe.drawapplication.view.welcome.UserRegister1View
import com.imeanttobe.drawapplication.viewmodel.MainNavHostViewModel
import com.imeanttobe.drawapplication.viewmodel.UserRegisterViewModel

@Composable
fun MainNavHostView(
    modifier: Modifier = Modifier,
    viewModel: MainNavHostViewModel = hiltViewModel(),
    sharedViewModel: UserRegisterViewModel = hiltViewModel()
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
                LoginView(
                    navigateToDetail = { navController.navigate(NavItem.LogindetailViewItem.route) }
                )
            }

            composableAnimated(route = NavItem.SplashViewItem.route) {
                SplashView()
            }

            navigation( //viewmodel을 1과 2가 공유하기 위해서 설정한 것. (sharedViewModel)
                startDestination = NavItem.UserRegister1ViewItem.route,
                route = "register_graph"
            ) {
                composableAnimated(route = NavItem.UserRegister1ViewItem.route) {
                    UserRegister1View(
                        viewModel = sharedViewModel,
                        returnTo = { navController.popBackStack() },
                        navigateToRegDetail = {email, password ->
                            sharedViewModel.updateEmail(email)
                            sharedViewModel.updatePassword(password)
                            navController.navigate(NavItem.UserRegister2ViewItem.route)
                        }
                    )
                }

                composableAnimated(route = NavItem.UserRegister2ViewItem.route) {
                    UserRegister2View(
                        viewModel = sharedViewModel,
                        returnTo = { navController.popBackStack() },
                        navigateToLogin = {
                            navController.navigate(NavItem.LogindetailViewItem.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
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