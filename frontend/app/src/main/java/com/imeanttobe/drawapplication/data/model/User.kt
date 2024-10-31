package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import com.imeanttobe.drawapplication.data.enum.UserType
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val userImageUrl: String,
    val instagramId: String = "",
    val type: UserType
) : Parcelable {
    private companion object : Parceler<User> {
        override fun User.write(parcel: Parcel, flags: Int) {
            parcel.writeString(id.toString())
            parcel.writeString(name)
            parcel.writeString(email)
            parcel.writeString(password)
            parcel.writeString(userImageUrl)
            parcel.writeString(instagramId)
            parcel.writeInt(type.ordinal)
        }

        override fun create(parcel: Parcel): User {
            return User(
                id = UUID.fromString(parcel.readString()),
                name = parcel.readString() ?: "",
                email = parcel.readString() ?: "",
                password = parcel.readString() ?: "",
                userImageUrl = parcel.readString() ?: "",
                instagramId = parcel.readString() ?: "",
                type = UserType.entries[parcel.readInt()]
            )
        }
    }
}