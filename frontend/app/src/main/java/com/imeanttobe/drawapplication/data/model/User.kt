package com.imeanttobe.drawapplication.data.model

import android.net.Uri
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.etc.UserWrapper

data class User(
    val id: String,
    val nickname: String,
    val email: String,
    val profilePhotoUri: Uri,
    val instagramId: String,
    val type: UserType,
    val postIds: MutableList<String> = mutableListOf(),
    val chatSessions: MutableList<String> = mutableListOf(),
    val introduce: String,
    val phoneNumber: String
) {
    constructor() : this(
        id = "",
        nickname = "",
        profilePhotoUri = Uri.EMPTY,
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
        profilePhotoUri = Uri.parse(userWrapper.profilePhotoUri),
        instagramId = userWrapper.instagramId,
        type = UserType.fromString(userWrapper.type),
        postIds = userWrapper.postIds,
        chatSessions = userWrapper.chatSessions,
        introduce = userWrapper.introduce,
        phoneNumber = userWrapper.phoneNumber
    )
}