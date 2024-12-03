package com.imeanttobe.drawapplication.data.model

import com.imeanttobe.drawapplication.data.enum.UserType

data class UserProfile(
    val instagramId: String,
    val type: UserType,
    val pictureIds: MutableList<String> = mutableListOf(),
    val chatSessions: MutableList<String> = mutableListOf(),
    val introduce: String,
    val phoneNumber: String
)