package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import com.imeanttobe.drawapplication.data.enum.UserType
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatList(
    val id: Int,
    val userName: String,
    val opponentName: String,
    val opponentId: Int,
    val lastMessage: String,
    val opponentType: UserType
): Parcelable {
    private companion object : Parceler<ChatList> {
        override fun ChatList.write(parcel: Parcel, flags: Int) {
            TODO("Not yet implemented")
        }

        override fun create(parcel: Parcel): ChatList {
            TODO("Not yet implemented")
        }
    }
}