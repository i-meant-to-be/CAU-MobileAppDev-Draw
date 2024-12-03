package com.imeanttobe.drawapplication.data.model

import com.imeanttobe.drawapplication.data.enum.UserType

data class User(
    val id: String,
    val nickname: String,
    val email: String,
    val profilePhotoUri: String,
    val instagramId: String,
    val type: UserType,
    val pictureIds: MutableList<String> = mutableListOf(),
    val chatSessions: MutableList<String> = mutableListOf(),
    val introduce: String,
    val phoneNumber: String
) {
    constructor() : this(
        id = "",
        nickname = "",
        profilePhotoUri = "",
        instagramId = "",
        type = UserType.UNDEFINED,
        introduce = "",
        phoneNumber = "",
        email = ""
    )
}