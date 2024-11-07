package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val localDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

@Parcelize
data class ImageItem(
    val id: Int = -1,
    val postId: Int,
    val imageUrl: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): Parcelable {
    private companion object : Parceler<ImageItem> {
        override fun ImageItem.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeInt(postId)
            parcel.writeString(imageUrl)
            parcel.writeString(datetime.format(localDateTimeFormatter))
        }

        override fun create(parcel: Parcel): ImageItem {
            return ImageItem(
                id = parcel.readInt(),
                postId = parcel.readInt(),
                imageUrl = parcel.readString() ?: "",
                datetime = LocalDateTime.parse(parcel.readString(), localDateTimeFormatter)
            )
        }
    }
}