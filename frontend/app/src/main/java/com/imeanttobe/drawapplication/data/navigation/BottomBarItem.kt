package com.imeanttobe.drawapplication.data.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.imeanttobe.drawapplication.R

sealed class BottomBarItem(
    val index: Int,
    val labelId: Int,
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
        labelId = R.string.chat,
        icon = Icons.AutoMirrored.Rounded.Chat
    )

    data object ExploreViewItem: BottomBarItem(
        index = 1,
        labelId = R.string.explore,
        icon = Icons.Rounded.Explore
    )

    data object ProfileViewItem: BottomBarItem(
        index = 2,
        labelId = R.string.profile,
        icon = Icons.Rounded.Person
    )

    data object DevViewItem: BottomBarItem(
        index = 3,
        labelId = R.string.dev,
        icon = Icons.Rounded.DeveloperMode
    )
}