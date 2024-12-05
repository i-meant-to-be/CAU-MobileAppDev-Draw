package com.imeanttobe.drawapplication.data.etc

import com.imeanttobe.drawapplication.data.model.User

data class UserWrapper(
    val id: String,
    val nickname: String,
    val email: String,
    val profilePhotoUri: String,
    val instagramId: String,
    val type: String,
    val postIds: MutableList<String> = mutableListOf(),
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
        profilePhotoUri = user.profilePhotoUri.toString(),
        instagramId = user.instagramId,
        type = user.type.toString(),
        postIds = user.postIds,
        chatSessions = user.chatSessions,
        introduce = user.introduce,
        phoneNumber = user.phoneNumber
    )
}