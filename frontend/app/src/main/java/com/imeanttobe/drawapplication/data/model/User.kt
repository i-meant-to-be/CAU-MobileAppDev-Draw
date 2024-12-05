package com.imeanttobe.drawapplication.data.model

import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.UserWrapper

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

    constructor(userWrapper: UserWrapper) : this(
        id = userWrapper.id,
        nickname = userWrapper.nickname,
        email = userWrapper.email,
        profilePhotoUri = userWrapper.profilePhotoUri,
        instagramId = userWrapper.instagramId,
        type = UserType.fromString(userWrapper.type),
        pictureIds = userWrapper.pictureIds,
        chatSessions = userWrapper.chatSessions,
        introduce = userWrapper.introduce,
        phoneNumber = userWrapper.phoneNumber
    )
}