package com.imeanttobe.drawapplication.data.etc

import com.imeanttobe.drawapplication.data.model.User

data class UserWrapper(
    val id: String,
    val nickname: String,
    val email: String,
    val profilePhotoUri: String,
    val instagramId: String,
    val type: String,
    val pictureIds: MutableList<String> = mutableListOf(),
    val chatSessions: MutableList<String> = mutableListOf(),
    val introduce: String,
    val phoneNumber: String
) {
    constructor() : this(
        id = "",
        nickname = "",
        email = "",
        profilePhotoUri = "",
        instagramId = "",
        type = "",
        introduce = "",
        phoneNumber = ""
    )

    constructor(user: User) : this(
        id = user.id,
        nickname = user.nickname,
        email = user.email,
        profilePhotoUri = user.profilePhotoUri,
        instagramId = user.instagramId,
        type = user.type.toString(),
        pictureIds = user.pictureIds,
        chatSessions = user.chatSessions,
        introduce = user.introduce,
        phoneNumber = user.phoneNumber
    )
}