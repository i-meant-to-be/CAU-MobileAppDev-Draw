package com.imeanttobe.drawapplication.data.navigation

sealed class NavItem(
    val route: String,
    val label: String
) {
    companion object {
        val items = listOf(
            BottomNavHostViewItem,
            ChatDetailItem,
            LoginTitleViewItem,
            RegisterUserAccountViewItem
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

    data object LoginTitleViewItem: NavItem(
        route = "/login",
        label = "Login"
    )

    data object RegisterUserAccountViewItem: NavItem(
        route = "/register/account",
        label = "Register account"
    )

    data object LoginDetailViewItem: NavItem(
        route = "/login_detail",
        label = "Login detail"
    )

    data object RegisterUserProfileViewItem: NavItem(
        route = "/register/profile",
        label = "Register profile"
    )
}