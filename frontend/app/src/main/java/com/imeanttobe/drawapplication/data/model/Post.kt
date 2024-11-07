package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val localDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

@Parcelize
data class Post(
    val id: Int = -1,
    val userId: Int,
    val description: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): Parcelable {
    private companion object : Parceler<Post> {
        override fun Post.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeInt(userId)
            parcel.writeString(description)
            parcel.writeString(datetime.format(localDateTimeFormatter))
        }

        override fun create(parcel: Parcel): Post {
            return Post(
                id = parcel.readInt(),
                userId = parcel.readInt(),
                description = parcel.readString() ?: "",
                datetime = LocalDateTime.parse(parcel.readString(), localDateTimeFormatter)
            )
        }
    }
}