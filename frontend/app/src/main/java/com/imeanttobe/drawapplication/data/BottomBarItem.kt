package com.imeanttobe.drawapplication.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarItem(
    val index: Int,
    val label: String,
    val icon: ImageVector
) {
    companion object {
        val items = listOf(
            ChatViewItem,
            ExploreViewItem,
            ProfileViewItem,
            DevViewItem
        )
    }

    data object ChatViewItem: BottomBarItem(
        index = 0,
        label = "Chat",
        icon = Icons.AutoMirrored.Rounded.Chat
    )

    data object ExploreViewItem: BottomBarItem(
        index = 1,
        label = "Find",
        icon = Icons.Rounded.Explore
    )

    data object ProfileViewItem: BottomBarItem(
        index = 2,
        label = "Profile",
        icon = Icons.Rounded.Person
    )

    data object DevViewItem: BottomBarItem(
        index = 3,
        label = "Dev",
        icon = Icons.Rounded.DeveloperMode
    )

}