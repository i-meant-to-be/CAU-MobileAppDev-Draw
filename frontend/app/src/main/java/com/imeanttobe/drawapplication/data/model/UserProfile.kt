package com.imeanttobe.drawapplication.data.model

import com.imeanttobe.drawapplication.data.enum.UserType

data class UserProfile(
    val instagramId: String,
    val type: UserType,
    val phoneNumber: String,
    val pictureIds: MutableList<String> = mutableListOf(),
    val chatSessions: MutableList<String> = mutableListOf(),
)