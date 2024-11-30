package com.imeanttobe.drawapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Do we need to add a field for determining whether the message is mine or not?

data class Message(
    val id: Int = -1,
    val senderId: String,
    val chatSessionId: String,
    val body: String,
    val datetime: LocalDateTime = LocalDateTime.now()
)

/*
    val id: String = "",
    val senderId: String = "",
    val body: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val senderName: String = ""
 */