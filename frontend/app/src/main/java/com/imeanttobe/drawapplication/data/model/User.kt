package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import com.imeanttobe.drawapplication.data.enum.UserType
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val imageUrl: String,
    val instagramId: String,
    val type: UserType,
    val phoneNumber: String
)