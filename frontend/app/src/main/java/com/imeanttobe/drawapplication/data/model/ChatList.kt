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
    val opponentType: UserType,
    val opponentImageUrl: String
): Parcelable {
    private companion object : Parceler<ChatList> {
        override fun ChatList.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(userName)
            parcel.writeString(opponentName)
            parcel.writeInt(opponentId)
            parcel.writeString(lastMessage)
            parcel.writeInt(opponentType.ordinal)
            parcel.writeString(opponentImageUrl)
        }

        override fun create(parcel: Parcel): ChatList {
            return ChatList(
                id = parcel.readInt(),
                userName = parcel.readString() ?: "",
                opponentName = parcel.readString() ?: "",
                opponentId = parcel.readInt(),
                lastMessage = parcel.readString() ?: "",
                opponentType = UserType.entries[parcel.readInt()],
                opponentImageUrl = parcel.readString() ?: ""

            )
        }
    }
}