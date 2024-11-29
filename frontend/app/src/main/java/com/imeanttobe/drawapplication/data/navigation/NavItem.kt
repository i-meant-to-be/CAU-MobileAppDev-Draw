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
            UserRegister1ViewItem,
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

    data object UserRegister1ViewItem: NavItem(
        route = "/register/registerFirst",
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

    data object UserRegister2ViewItem: NavItem(
        route = "/register/registerSecond",
        label = "Register detail"
    )
}