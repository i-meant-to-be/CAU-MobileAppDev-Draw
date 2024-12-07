package com.imeanttobe.drawapplication.data.navigation

sealed class NavItem(
    val route: String,
    val label: String
) {
    companion object {
        val items = listOf(
            ChatDetailItem,
            LoginDetailViewItem,
            RegisterUserAccountViewItem,
            UserProfileViewItem

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

    data object UserProfileViewItem: NavItem(
        route = "/explore/userprofile",
        label = "explore userprofile"
    )
    data object ChatView: NavItem(
        route = "/chatviewe",
        label = "Chat view"
    )
}