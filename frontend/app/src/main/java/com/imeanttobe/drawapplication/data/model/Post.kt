package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Post(
    val id: Int,
    val userId: Int,
    val description: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): Parcelable {
    private companion object : Parceler<Post> {
        override fun Post.write(parcel: Parcel, flags: Int) {
            // TODO
        }

        override fun create(parcel: Parcel): Post {
            // TODO
        }
    }
}