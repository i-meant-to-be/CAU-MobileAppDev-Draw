package com.imeanttobe.drawapplication.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String,
    val label: String
) {
    companion object {
        val items = listOf(
            BottomNavHostViewItem,
            ChatDetailItem,
            LoginViewItem,
            UserRegisterViewItem,
            SplashViewItem
        )
    }

    data object BottomNavHostViewItem: NavItem(
        route = "/bottomNavHost",
        label = "Main"
    )

    data object ChatDetailItem: NavItem(
        route = "/chat",
        label = "Chat"
    )

    data object LoginViewItem: NavItem(
        route = "/login",
        label = "Login"
    )

    data object UserRegisterViewItem: NavItem(
        route = "/register",
        label = "Register"
    )

    data object SplashViewItem: NavItem(
        route = "/",
        label = "Splash"
    )
}