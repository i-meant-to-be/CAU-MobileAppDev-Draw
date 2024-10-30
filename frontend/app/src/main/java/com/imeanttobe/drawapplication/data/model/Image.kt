package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Image(
    val id: Int,
    val postId: Int,
    val imageUrl: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): Parcelable {
    private companion object : Parceler<Image> {
        override fun Image.write(parcel: Parcel, flags: Int) {
            // TODO
        }

        override fun create(parcel: Parcel): Image {
            // TODO
        }
    }
}