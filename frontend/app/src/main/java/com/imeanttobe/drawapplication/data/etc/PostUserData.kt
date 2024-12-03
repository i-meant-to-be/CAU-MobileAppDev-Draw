package com.imeanttobe.drawapplication.data.etc

import android.net.Uri
import com.imeanttobe.drawapplication.data.enum.UserType

data class PostUserData(
    val nickname: String,
    val userType: UserType,
    val imageUri: Uri
)