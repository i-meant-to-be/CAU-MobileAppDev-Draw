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
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        else NavItem.LoginTitleViewItem.route

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
                        navController.navigate(NavItem.LoginTitleViewItem.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composableAnimated(
                route = "${NavItem.ChatDetailItem.route}/sessionId={sessionId}",
                arguments = listOf(
                    navArgument("sessionId") { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val sessionId = navBackStackEntry.arguments?.getString("sessionId") ?: ""

                ChatDetailView(
                    navigateUp = { navController.navigateUp() },
                    sessionId = sessionId
                )
            }

            composableAnimated(route = NavItem.LoginTitleViewItem.route) {
                LoginTitleView(
                    navigateToDetail = { navController.navigate(NavItem.LoginDetailViewItem.route) }
                )
            }

            composableAnimated(route = NavItem.RegisterUserAccountViewItem.route) {
                RegisterUserAccountView(
                    returnTo = { navController.popBackStack() },
                    navigateToRegisterProfile = { email, pw ->
                        navController.navigate("/${NavItem.RegisterUserProfileViewItem.route}/email=$email?pw=$pw")
                    }
                )
            }

            composableAnimated(
                route = "${NavItem.RegisterUserProfileViewItem.route}/email={email}?pw={pw}?phone_number={phone_number}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("pw") { type = NavType.StringType },
                    navArgument("phone_number") { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val email = navBackStackEntry.arguments?.getString("email") ?: ""
                val pw = navBackStackEntry.arguments?.getString("pw") ?: ""
                val phoneNumber = navBackStackEntry.arguments?.getString("phone_number") ?: ""

                RegisterUserProfileView(
                    navigateBack = { navController.popBackStack() },
                    navigateToHome = {
                        navController.navigate(NavItem.BottomNavHostViewItem.route) { popUpTo(0) { inclusive = true } }
                    },
                    email = email,
                    password = pw,
                    phoneNumber = phoneNumber
                )
            }

            composableAnimated(route = NavItem.LoginDetailViewItem.route) {
                LoginDetailView(
                    returnTo = { navController.popBackStack() },
                    navigateToReg = { navController.navigate(NavItem.RegisterUserAccountViewItem.route) },
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
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
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