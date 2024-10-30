package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import com.imeanttobe.drawapplication.data.enum.UserType
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val userImageUrl: String,
    val instagramId: String = "",
    val type: UserType
) : Parcelable {
    private companion object : Parceler<User> {
        override fun User.write(parcel: Parcel, flags: Int) {
            // TODO
        }

        override fun create(parcel: Parcel): User {
            // TODO
        }
    }
}