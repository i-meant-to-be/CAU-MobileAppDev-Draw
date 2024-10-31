package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val localDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

@Parcelize
data class Chat(
    val id: Int = -1,
    val chatListId: Int,
    val opponentName: String,
    val userName: String,
    val message: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): Parcelable {
    private companion object : Parceler<Chat> {
        override fun Chat.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeInt(chatListId)
            parcel.writeString(opponentName)
            parcel.writeString(userName)
            parcel.writeString(message)
            parcel.writeString(datetime.format(localDateTimeFormatter))
        }

        override fun create(parcel: Parcel): Chat {
            return Chat(
                id = parcel.readInt(),
                chatListId = parcel.readInt(),
                opponentName = parcel.readString() ?: "",
                userName = parcel.readString() ?: "",
                message = parcel.readString() ?: "",
                datetime = LocalDateTime.parse(parcel.readString(), localDateTimeFormatter)
            )
        }
    }
}