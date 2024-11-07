package com.imeanttobe.drawapplication.data.navigation

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

    data object LogindetailViewItem: NavItem(
        route = "/logindetail",
        label = "logindetail"
    )
}